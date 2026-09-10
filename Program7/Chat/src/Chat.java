import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
/*
Group 2 - NetworkChat Program
John Gerega (ger9822@pennwest.edu
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu)
This is a NetworkChat program that is used to connect a client and a server to allow communication between the two
It makes use GridBagLayout to set up the GUI of the program and uses a thread to keep track and run the prograam
There are client and server sockets that are connected via a ServerSocket
The program can be used as both a client and a server itself
There is a textarea that outputs the current status and state of the program
Message are labled properly as out or in depending on the instance of the program running
 */
public class Chat extends Frame implements Runnable, ActionListener, WindowListener{

    //data initialization
    private BufferedReader br;

    private PrintWriter pr;

    protected final static boolean auto_flush = true;

    private Button ChangePortButton = new Button(" Change Port");
    private Button SendButton = new Button(" Send");
    private Button ServerButton = new Button("Start Server");
    private Button ClientButton = new Button(" Connect ");
    private Button DisconnectButton = new Button ("Disconnect ");
    private Button ChangeHostButton = new Button("Change Host");

    private Label PortLabel = new Label("Port:");
    private Label HostLabel = new Label("host:");

    private TextField ChatText = new TextField(70);
    private TextField PortText = new TextField(10);
    private TextField HostText = new TextField(10);

    private Frame DispFrame;

    private Thread TheThread;

    private TextArea DialogScreen = new TextArea("", 10, 80);
    private TextArea MessageScreen = new TextArea("", 3, 80);

    private Socket Client;
    private Socket Server;
    private ServerSocket listen_socket;

    private String host = "";

    private int DEFAULT_PORT = 44004;
    private int port = DEFAULT_PORT;
    private int service = 0;
    private static int timeout = 1000;

    private boolean more = true;

    public static void main(String[] args) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); //Opens new buffered reader
        String line;        //string to read line
        int dec;            //integer storage
        System.out.println("Enter an integer: ");   //user prompt
        line = stdin.readLine();                    //read line
        try
        {
            dec = Integer.parseInt(line);	//tries converting string into integer
        }
        catch (NumberFormatException e)
        {
            dec = timeout;                  //otherwise,set to default timeout value
        }
        Chat n = new Chat(dec);   //instantiate new instance of NetworkChat()
    }

    Chat(int n)
    {
        DispFrame = this;
        timeout = n;    //timeout set to n
        service = 0;    //service set to 0
        more = true;    //more set to true
        GridBagConstraints c = new GridBagConstraints();    //gridbaglayout and gridbagconstraints initialized
        GridBagLayout displ = new GridBagLayout();
        DispFrame.setBounds(0, 0, 1500, 860);
        DispFrame.setTitle("Chat");
        double colWeight[] = {1, 1, 1, 1, 1, 1};// each column cell gets a weight
        double rowWeight[] = {1, 1, 1, 1, 1, 1, 1, 1, 1}; // last four get no weight, dont change on resize also if not assign a row a weight it defaults to 0
        int colWidth[] = {1, 2, 2, 1}; // 6 cell wide of columns
        int rowHeight[] = {5, 1, 1, 1, 1}; // 10 cell high of rows
        displ.rowHeights = rowHeight;
        displ.columnWidths = colWidth;
        displ.columnWeights = colWeight;
        displ.rowWeights = rowWeight;

        DispFrame.setLayout(displ);      //sets layout
        c.fill = GridBagConstraints.BOTH;       //sets fill to BOTH, meaning it stretches to fill horizontally and vertically
        //////////////////////////setup list
        c.gridx = 0;        //start in uppper left corner at 0,0
        c.gridy = 0;
        c.gridwidth = 6;//all 6 column cells are in list
        c.gridheight = 5; // only first five rows are contained in lit
        displ.setConstraints(DialogScreen, c);  //set constraints for list
        this.add(DialogScreen);     //add list

        c.gridx = 0;    //start at x = 0
        c.gridy = 5;    //start at y = 5
        c.gridwidth = 3;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(ChatText, c);       //set constraints for Chattext
        DispFrame.add(ChatText);                  //add ChatText

        c.gridx = 3;    //start at x = 1
        c.gridy = 5;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(SendButton, c);       //set constraints for Send Button
        DispFrame.add(SendButton);                  //add Send Button

        c.gridx = 0;    //start at x = 0
        c.gridy = 6;    //start at y = 5
        //c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(HostLabel, c);       //set constraints for HostLabel
        DispFrame.add(HostLabel);                  //add HostLable

        c.gridx = 1;    //start at x = 0
        c.gridy = 6;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(HostText, c);       //set constraints for HostText
        DispFrame.add(HostText);                  //add HostText

        c.gridx = 2;    //start at x = 0
        c.gridy = 6;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(ChangeHostButton, c);       //set constraints for ChangeHostButton
        DispFrame.add(ChangeHostButton);                  //add ChangeHostButton

        c.gridx = 3;    //start at x = 0
        c.gridy =6;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(ServerButton, c);       //set constraints for ServerButton
        DispFrame.add(ServerButton);                  //add ServerButton

        c.gridx = 0;    //start at x = 0
        c.gridy = 7;    //start at y = 5
        //c.gridwidth = 1;    //take up 1 column
        //c.gridheight = 1;   //take up 1 row
        displ.setConstraints(PortLabel, c);       //set constraints for PortLabel
        DispFrame.add(PortLabel);                  //add PortLabel

        c.gridx = 1;    //start at x = 0
        c.gridy = 7;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(PortText, c);       //set constraints for PortText
        DispFrame.add(PortText);                  //add PortText

        c.gridx = 2;    //start at x = 0
        c.gridy = 7;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(ChangePortButton, c);       //set constraints for ChangePortButton
        DispFrame.add(ChangePortButton);                  //add ChangePortButton

        c.gridx = 3;    //start at x = 3
        c.gridy = 7;    //start at y = 7
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(ClientButton, c);       //set constraints for ClientButton
        DispFrame.add(ClientButton);                  //add ClientButton

        c.gridx = 3;    //start at x = 0
        c.gridy = 8;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(DisconnectButton, c);       //set constraints for DisconnectButton
        DispFrame.add(DisconnectButton);                  //add DisconnectButton

        c.gridx = 0;    //start at x = 0
        c.gridy = 9;    //start at y = 5
        c.gridwidth = 6;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(MessageScreen, c);       //set constraints for MessageScreen
        DispFrame.add(MessageScreen);                  //add MessageScreen


        DispFrame.pack();       //pack frame, set visible, and add windowlistener
        DispFrame.setVisible(true);
        DispFrame.addWindowListener(this);

        SendButton.addActionListener(this); //add actionlisteners and enable buttons
        SendButton.setEnabled(true);
        ChangeHostButton.addActionListener(this);
        ChangeHostButton.setEnabled(true);
        ChangePortButton.addActionListener(this);
        ChangePortButton.setEnabled(true);
        ClientButton.addActionListener(this);
        ClientButton.setEnabled(false);
        ServerButton.addActionListener(this);
        ServerButton.setEnabled(true);
        DisconnectButton.addActionListener(this);
        DisconnectButton.setEnabled(true);
        ChatText.addActionListener(this);
        PortText.addActionListener(this);
        HostText.addActionListener(this);
        start();        //start function call

    }

    public void start()
    {
        //create the thread if not started
        if (TheThread == null)
        {
            TheThread = new Thread(this);
            TheThread.start();
        }
    }

    public void run()
    {
        TheThread.setPriority(Thread.MAX_PRIORITY); //set priority of thread to max
        //messageDisplay("In thread");               //write to message Display function
        while(more) //main while loop
        {
            //create loop
            try
            {
                if(br != null)
                {
                    String line = br.readLine();  //try to read a line from buffered reader
                    if (line != null) {
                        DialogScreen.append("in: " + line + "\n");    //if there is a line, append to text area
                    } else {
                        more = false;   //if line was null, which means Socket is closed, change more flag
                    }
                }
                else
                {
                    more = false;
                }
            }
            catch(IOException ex)
            {
                messageDisplay("Nothing to read");   //catch statement
            }
        }
        messageDisplay("Waiting");//Display status message
        close(); //close socket, reader, and writer and reset to initial state (close)

    }

    public void stop()
    {

        close();    //call close method
        if (TheThread != null)
        {
            TheThread.setPriority(Thread.MIN_PRIORITY); //set thread priority to minimum
        }
        SendButton.removeActionListener(this);  //remove action listeners
        ClientButton.removeActionListener(this);
        ServerButton.removeActionListener(this);
        ChangePortButton.removeActionListener(this);
        DisconnectButton.removeActionListener(this);
        ChangeHostButton.removeActionListener(this);
        HostText.removeActionListener(this);
        PortText.removeActionListener(this);
        ChatText.removeActionListener(this);
        DispFrame.removeWindowListener(this);    //remove window listener
        dispose();
        System.exit(0);
    }


    public void close()     //close method
    {
        try
        {
            if (Server != null) //if the server is not null
            {
                if(pr != null)  //if printwriter is not null
                {
                    pr.print("");   //print null
                }
                Server.close(); //close server
                Server = null;  //set server to null
            }
        }
        catch (IOException e){
            messageDisplay("Couldn't close Server");
        }
        try
        {
            if(Client != null)  //if client is not null
            {
                if (pr != null) //if printwriter is not null
                {
                    pr.print("");   //print null
                }
                Client.close(); //close client
                Client = null;  //set client to null
            }

        } catch (IOException e)
        {
            messageDisplay("Couldn't close Client");
        }
        try
        {
            if(listen_socket != null)   //if listen_socket is not null
            {
                if (pr != null) //if printwriter is not null
                {
                    pr.print("");   //print null
                }
                listen_socket.close();  //close listen_socket
                listen_socket = null;   //set socket to null
            }
        } catch (IOException e)
        {
            messageDisplay("Couldn't close listen socket");
        }
        SendButton.setEnabled(true);        //reset buttons
        ChangeHostButton.setEnabled(true);
        ChangePortButton.setEnabled(true);
        ClientButton.setEnabled(false);
        ServerButton.setEnabled(true);
        DisconnectButton.setEnabled(true);
        service = 0;    //reset service to 0
        TheThread = null;   //null the thread
        HostText.setText("");   //reset host text

    }

    public void messageDisplay(String s)
    {
        //determine if machine state is server or client
        if (service == 1)   //server
        {
            MessageScreen.append("Server: " + s + "\n");    //add message in Server mode
            ChatText.requestFocus();    //request focus back to ChatText
        }
        else if (service == 2)  //client
        {
            MessageScreen.append("Client: " + s+ "\n"); //add message in client mode
            ChatText.requestFocus();    //request focus back to Chattext
        }
        else    //default case
        {
            MessageScreen.append("No Mode: " + s + "\n");
            ChatText.requestFocus();    //request focus back to Chattext
        }
    }



    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub
        stop();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Object source = e.getSource();      //get source
        if(source == SendButton || source == ChatText)
        {
            String data = ChatText.getText();
            DialogScreen.append("out: " +data+ "\n");   //print the out message
            pr.println(data);           //print the string
            ChatText.setText("");   //reset text
            //clear field
        }
        if(source == ServerButton)
        {
            service = 1;    //set service to one
            messageDisplay("In server mode");   //send message
            ServerButton.setEnabled(false);         //disable buttons
            ClientButton.setEnabled(false);
            try
            {
                if(listen_socket != null)   //if listen_socket is not null
                {
                    listen_socket.close();  //close the socket and null it
                    listen_socket = null;
                }
                //send message to status message text area
                messageDisplay("Opening Socket");
                //Set serversocket port
                listen_socket = new ServerSocket(port);     //create new serversocket with specified port
                //send message to status message text area
                messageDisplay("Socket Opened");
                //Set timeout for the Server to wait for a connection, this should be longer than the client request timer
                listen_socket.setSoTimeout(10*timeout);
                if(Client != null)  //if client is not null
                {
                    Client.close(); //clsoe client and null it
                    Client = null;
                }

            }
            catch (IOException er)
            {
                messageDisplay("IO error encountered"); //print error
                close();    //close socket
            }
            try
            {
                messageDisplay("Listening for connection"); //status message
                Client = listen_socket.accept();            //have client accept connection
                DispFrame.setTitle("Server");                    //set title
                messageDisplay("connection from " + Client.getInetAddress());   //status message
            }
            catch (IOException er)
            {
                messageDisplay("IO error encountered"); //error message
                close();    //close
            }
            try
            {
                br = new BufferedReader(new InputStreamReader(Client.getInputStream()));    //try opening bufferedreader and printwriter
                pr = new PrintWriter(Client.getOutputStream(), auto_flush);
                service = 1;    //set service to 1
                ChatText.setEnabled(true);  //set the chattext enabled to true
                more = true;    //set more to true
                start();  //start thread
            }
            catch (IOException er)
            {
                messageDisplay("IO error encountered"); //error message
                close();    //close
            }
        }
        if (source == ClientButton)
        {
            //send message to the status message TextArea
            service = 2;
            messageDisplay("in client mode");
            try
            {
                ServerButton.setEnabled(false);     //disable buttons
                ClientButton.setEnabled(false);
                if (Server != null) //if server is not null, close and null it
                {
                    Server.close();
                    Server = null;
                }
                Server = new Socket();  //initialize Server to a new Socket
                //send message to the status message TextArea
                messageDisplay("Opening new socket");
                Server.setSoTimeout(timeout);   //set timeout for client to wait for connection
            }
            catch (IOException er)
            {
                messageDisplay("IO error encountered"); //error message
                close();    //close
            }
            try
            {
                //send message to the status message TextArea
                Server.connect(new InetSocketAddress(host, port));  //try to connect with specified host on specified port
                DispFrame.setTitle("Client");    //setTitle
                //send updated status message to the status message TextArea that connection has been made
                messageDisplay("Connection made to " + Server.getInetAddress());

                br = new BufferedReader(new InputStreamReader(Server.getInputStream()));    //try to make new bufferedreader and printwriter
                pr = new PrintWriter(Server.getOutputStream(), auto_flush); //create printwriter
                service = 2;    //set service to 2
                ChatText.setEnabled(true);  //enable chat text field
                more = true;    //set more to true
                start();        //start
            }
            catch (IOException er)
            {
                messageDisplay("IO error encountered");     //error message
                close();    //close
            }
        }
        if (source == DisconnectButton)
        {
            //send status message to the status display TextArea
            messageDisplay("Disconnected");
            //Send null to the connection
            ChatText.setText("");   //reset chat text
            TheThread.interrupt();  //interrupt TheThread
            close();    //close
        }
        if(source == HostText || source == ChangeHostButton)
        {
            String hostString = HostText.getText(); //get text from host
            if(hostString != null)
            {
                ClientButton.setEnabled(true);      //as long as text from host is not null, enable the client button
            }
        }
        if(source == PortText || source == ChangePortButton)
        {
            String portString = PortText.getText(); //get text from porttext and store in portString
            int i = port;       //temporary integer variable
            try
            {
                i = Integer.parseInt(portString);   //try converting portString into integer
                port = i;   //set port equal to i
                ClientButton.setEnabled(true);  //set connect button to true
                messageDisplay("Port changed");     //status message
            } catch (NumberFormatException ex)
            {
                messageDisplay("Invalid port entry");   //status message
            }
        }
        ChatText.requestFocus();    //request focus from chat text

    }



}