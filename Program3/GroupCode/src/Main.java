/*
Group 3 - GUI File Copy
John Gerega (ger9822@pennwest.edu
Colleen Bucher (buc4883@pennwest.edu)
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu)
Purpose of this program is to use a GUI application to navigate a directory structure
Main will check the command line for a directory, if nothing is found, the current directory of the program will be used
A list will represent the directory, and the current directory will be the title
Directory will contain .. unless current directory is the root, and a + if a child directory contains another directory
When .. is selected, the directory will be updated to the parent
When a file is selected, the path will be in the source label, and will continue to happen until the target button is selected
The target button, which is initially not enabled, will become enabled if a path is in the source label
When the target button is selected, the target path label will be updated to contain current path
The third line, the file name, is the file to which the source file wil be copied to
The user can either select from the list or type in a file
If the target file exists, it will be overwritten, otherwise a new file will be created
Error messages will be displayed if information is left blank or cannot be found
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.lang.*;
//import statements

public class Main      //class name
{
    public static void main(String args[]) throws IOException       //main method
    {
        //System.out.println("in 3");
        String name = "";      //initalize empty string
        switch(args.length)
        {
            case 0:
                Window mywindow = new Window(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));
                break;      //case 0 extracts current directory, meaning no command line parameters entered, and passes it into window constructor
            case 1:
                name = args[0];     //if there is a command line parameter, store it in empty string
                File test = new File(name);     //create a new file with the name
                if (test.isDirectory())     //test if the file is a directory
                {
                    Window mywindow2 = new Window(new File(test.getAbsolutePath())); //passes directory into constructor if true
                }
                else
                {
                    System.out.print("Invalid Directory entry"); //error message
                }
                break;
            default:
                System.out.println("No directory found");
                break;
        }
    }
}



class Window extends Frame implements WindowListener, ActionListener {
    boolean OutFilebool = false;        //set flags as false to begin
    boolean Sourcebool = false;
    boolean Targetbool = false;

    private static final long serialVersionUID = 1L;
    List list = new List(13);    //create 13 visible row list

    Button Target = new Button("Target:");      //create Target Button
    Label direction = new Label(); // for target display

    Label Sourcetitle = new Label();        //Create Source title lable
    Label Sourcefield = new Label();        //create sourcefield label

    Label filenametitle = new Label("File Name: ");     //create filenametitle label
    TextField FileName = new TextField(80);     //create filename textfieldf label

    Button OK = new Button("OK");       //create OK button

    Label messageLabel = new Label();       //creare messagelabel label

    File curDir = new File("");     //create current directory file
    File originalDirectory = new File("");  //create original directory file

    Window(File directory) // constructor for window object
    {

        curDir = new File(String.valueOf(directory.getAbsolutePath()));// sets curDir to directory passed in
        originalDirectory = curDir;
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout displ = new GridBagLayout();
        this.setBounds(0, 0, 1500, 860);
        double colWeight[] = {1, 1, 1, 1, 1, 1};// each column cell gets a weight
        double rowWeight[] = {1, 1, 1, 1, 1, 0, 0, 0, 0}; // last four get no weight, dont change on resize also if not assign a row a weight it defaults to 0
        int colWidth[] = {1, 2, 2, 1}; // 6 cell wide of columns
        int rowHeight[] = {5, 1, 1, 1, 1}; // 10 cell high of rows
        displ.rowHeights = rowHeight; //uses above variables
        displ.columnWidths = colWidth; //
        displ.columnWeights = colWeight;
        displ.rowWeights = rowWeight;

        this.setLayout(displ);  //chooses gridbag over other layout options
        this.setTitle(curDir.getAbsolutePath());        //sets window title to directory extracted from main
        this.setBounds(0, 0, 300, 900);     //sets window size
        c.fill = GridBagConstraints.BOTH;       //sets fill to BOTH, meaning it stretches to fill horizontally and vertically
        //////////////////////////setup list
        c.gridx = 0;        //start in uppper left corner at 0,0
        c.gridy = 0;
        c.gridwidth = 6;//all 6 column cells are in list
        c.gridheight = 5; // only first five rows are contained in lit
        displ.setConstraints(list, c);  //set constraints for list
        this.add(list);     //add list

        c.gridx = 0;    //start at x = 0
        c.gridy = 5;    //start at y = 5
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        Sourcetitle.setText("Source: ");    //set our source title text
        displ.setConstraints(Sourcetitle, c);       //set constraints for sourcetitle
        this.add(Sourcetitle);                  //add sourcetitle

        c.gridx = 1;    //start at x = 1
        c.gridy = 5;    //start at y = 5
        c.gridwidth = 4;    //take up 4 columns
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(Sourcefield, c);   //set constraints for source field
        this.add(Sourcefield);      //add source field

        c.gridx = 0;        //start at x = 0
        c.gridy = 6;        //start at y = 6
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(Target, c);    //set constraints to Target
        this.add(Target);   //add Target

        c.gridx = 1;    //start at x = 1
        c.gridy = 6;    //start at y = 6
        c.gridwidth = 4;    //take up 4 columns
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(direction, c);     //set constraints to direction
        this.add(direction);        //add direction


        c.gridx = 0;        //start at x = 0
        c.gridy = 7;        //start at y = 7
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(filenametitle, c);     //set constraints to filenametitle
        this.add(filenametitle);    //add filenametitle

        c.gridx = 1;    //start at x = 1
        c.gridy = 7;    //start at y = 7
        c.gridwidth = 4;    //take up 4 columns
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(FileName, c);      //set constraints to FileName
        this.add(FileName);     //add FileName

        c.gridx = 5;        //start at x = 5
        c.gridy = 7;        //start at y = 7
        c.gridwidth = 1;    //take up 1 column
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(OK, c);    //set constraints to Ok button
        this.add(OK);       //add OK button

        c.gridx = 0;        //start at x = 0
        c.gridy = 8;        //start at y = 8
        c.gridwidth = 6;    //take up 6 columns
        c.gridheight = 1;   //take up 1 row
        displ.setConstraints(messageLabel, c);      //set constraints to messageLabel
        this.add(messageLabel);     //add messageLabel


        this.pack();    //sets all contents at or above preferred sizes

        this.setVisible(true);              //make the window visible
        this.addWindowListener(this);       //add window listener

        Target.setEnabled(false);       //make target button not active on start
        Target.addActionListener(this); //add ActionListener to the Target Button

        OK.setEnabled(true);            //enable OK buttone
        OK.addActionListener(this);     //add actionlistener to OK button

        list.addActionListener(this);        //add ActionListener to the list

        FileName.addActionListener(this);       //add ActionListener to FileName textfield


        display(null);          //call display function
    }

    public void display(String name) {
        String[] filenames = new String[10];
        boolean itisdirectory = false;
        if (name != null) {
            if (name == "..") //if asking for parent
            {
                curDir = new File(curDir.getParent()); // set curDir to parent, so when display will display parent
            }
            else //if given file or directory name
            {
                if ((name.charAt(name.length() - 1) == '+') && (name.charAt(name.length() - 2) == ' ')) //need check if name has " + on end, if it does delete it"
                {
                    name = name.substring(0, (name.length() - 2));
                }
                File f = new File(curDir, name); // file currently selected when method called
                if (f.isDirectory())
                {
                    itisdirectory = true;
                    curDir = f;//reset curDir to be file selected if file selected was directory for display to use later	/*may want remove later*

                }else if(!Sourcebool || !Targetbool)
                {//if file selected and either or both false
                    Sourcefield.setText(curDir.getAbsolutePath() + "\\" + name);
                    Sourcebool = true;
                    Target.setEnabled(true);
                } else
                {//if both true
                   // if file selected and both source and target been gone through
                    FileName.setText(/*curDir.getAbsolutePath() + "\\" + */name);
                    OutFilebool = true;
                }
            }
        }
        if ((name == null) || (name == "..") || (itisdirectory)) // if dealing with first call, asking for parent, or a directory passed into method
        {
            filenames = curDir.list();
            this.setTitle(curDir.getAbsolutePath());
            if (filenames != null) // if there is anything inside directory selected to display
            {
                int i = 0;
                while (i < (filenames.length)) // loop through filenames array
                {
                    File f = new File(curDir, filenames[i]);
                    {
                        if ((f.isDirectory()) && (f.list() != null)) // for each directory inside filenames that has children
                        {
                            String[] children = f.list();
                            int j = 0;
                            boolean founddir = false;
                            while ((j < (children.length)) && (!founddir)) // loop through children until reach end or found directory
                            {
                                if (new File(f, children[j]).isDirectory())
                                {
                                    founddir = true;    //change filedir to true to break inner while loop
                                }
                                j++;    //increment j
                            }//at end of loop either found directory or no directory is in children
                            if (founddir)   //if founddir is true
                            {
                                filenames[i] = filenames[i] + " +";//if one of children had directory, add plus to parent
                            }
                        }
                    }
                    i++;
                }
            }
            list.removeAll(); // empty list, refresh ready
            if (curDir.getParent() != null) // if the current directory is not null
            {
                list.add(".."); //add .. to the list
            }
            if (filenames != null)
            {
                for (int k = 0; k < (filenames.length); k++) // add each file in curDir to list
                {
                    list.add(filenames[k]); //if filenames is not null, add the name at index k to the list
                }
            }
        }
    }


    public void CopyFile() throws IOException //copyfile function
    {
        int c = 0;  //set c to 0
        String source = Sourcefield.getText();  //get our source file string
        String target = FileName.getText(); //get our target file string
        String finalfile = "";  //empty string
        Boolean outfe = false;  //boolean outfile existence set to false
        File sourcefile = new File(source); //new file with source name intialized
        File targetfile = new File(target); //new file with target file name initialized
        File FinalFile = new File(target);      //Final file set to target file name

        String targetpath = targetfile.getAbsolutePath();// getting path assuming file in current directory
        if (Sourcebool == false || Targetbool == false || OutFilebool == false || targetfile.isDirectory())
        {
            messageLabel.setText("Cannot complete file copy");
        }
        else
        {

            if (targetfile.exists())
            {
                outfe = true;   //change outfe to true to know we have to overwrite
            }

            BufferedReader infile = null;   //set buffered reader infile to null
            try
            {
                infile = new BufferedReader(new FileReader(sourcefile));        //try opening the file
            }
            catch (FileNotFoundException exception)
            {
                messageLabel.setText("Source file not specified");        //error message if file isn't found
            }
            PrintWriter outfile = null;        //sets printwriter named out to null
            if(!(targetpath.equals(target)))    //check if target path is equal to string target
            {
                targetfile = new File(curDir.getAbsolutePath() + "\\" + target); //if not, there is no specified directory, so we extract it from direction label
                if (targetfile.exists())
                {
                    outfe = true;   //change outfe to true to know we have to overwrite
                }
            }
            try
            {
                outfile = new PrintWriter(new FileWriter(targetfile));        //tries opening printwriter with output file name
            }
            catch (FileNotFoundException exception)
            {
                messageLabel.setText("Target file not specified");    //error message if file isn't found
            }

            if (outfe == true)
            {
                if (sourcefile.getAbsolutePath().equals(targetfile.getAbsolutePath()))
                {
                    messageLabel.setText("Cannot overwrite source file");   //error message
                }
                else
                {
                    messageLabel.setText("File " + targetfile.getName() + " will be overwritten");  //overwrite message for when file exists
                    try
                    {
                        Thread.sleep(3000); //gives user time to read message
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    while ((c = infile.read()) != -1) {
                        outfile.write(c);       //copies integers from file
                    }
                    infile.close();     //close files
                    outfile.close();
                    display(null);      //call display with null again
                    Sourcefield.setText("");    //reset textfields
                    direction.setText("");
                    FileName.setText("");
                    messageLabel.setText("File Copied");    //success message
                    OutFilebool = false;    //reset flags
                    Targetbool = false;
                    Sourcebool = false;
                    Target.setEnabled(false);   //reset target button
                }
            }
            else
            {
                    messageLabel.setText("Creating new file");      //new file message
                    try {
                        Thread.sleep(3000);    //gives user time to read message
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    while ((c = infile.read()) != -1) {
                        outfile.write(c);       //copies integers from file
                    }
                    infile.close();     //close files
                    outfile.close();
                    display(null);      //call display with null again
                    Sourcefield.setText("");    //reset textfields
                    direction.setText("");
                    FileName.setText("");
                    messageLabel.setText("File Copied");    //success message
                    OutFilebool = false;    //reset flags
                    Targetbool = false;
                    Sourcebool = false;
                    Target.setEnabled(false);   //reset target button

            }

            //test out file and maybe overwrite
        }
    }





    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource(); //get current event source

        if (source == Target)
        {
            //if event source is target button, get the current directory and set the text on direction to that directory
            messageLabel.setText("");
            direction.setText(curDir.getAbsolutePath());
            Targetbool = true;
        }

        if (source == FileName)
        {
            //if source is the textfield, extract the text and put it in a string
            messageLabel.setText("");
            String Filename = FileName.getText();

            if(Filename.length()!=0)
            {
                //if the length is greater than 0, try calling CopyFile()
                OutFilebool = true;
                try
                {
                    CopyFile();
                }
                catch (IOException e1)
                {
                    messageLabel.setText("Error"+e1);
                }
            }
            else
            {
                //otherwise, no file specified
                messageLabel.setText(messageLabel.getText()+"Target file not specified");
            }

        }

        if (source == list)
        {
            //get the selected item, and call display with said item
            String item = list.getSelectedItem();
            display(item);
            messageLabel.setText("");
            if(Targetbool == true)
            {
                File itemfile = new File(curDir.getParent(), item); //if the target bool is true, create a new file with the current directory and the string in item
             /*
                if((item != "..")&&(!(itemfile.isDirectory()))) // if have regular file
                {
                	FileName.setText(curDir.getAbsolutePath() + "\\" + item);
                }
                else
                {
                	FileName.setText(curDir.getAbsolutePath()); //if have parent, or directory
                }
            */
            }
        }

        if (source == OK)
        {   //if the source is the OK button, extract the filename text and store it into filename
            String Filename = FileName.getText();
            String sourcename = Sourcefield.getText();  //extract sourcefield text and store it into string sourcename
            messageLabel.setText("");
            if(Filename.length()!=0)
            {
                //if there is more than 0 characters in Filename string, change outfile to true and try copyfile
                OutFilebool = true;
                try
                {
                    CopyFile();
                }
                catch (IOException e1)
                {
                    messageLabel.setText("Error"+e1);
                }
            }
            else
            {
                //otherwise, file is not specified
                messageLabel.setText(messageLabel.getText()+"Source or Target file not specified");
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e)
    {

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        //close all elements and dispose
        this.removeWindowListener(this);        //remove elements
        Target.removeActionListener(this);
        OK.removeActionListener(this);
        list.removeActionListener(this);
        FileName.removeActionListener(this);
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        //remove listener and dispose
        this.removeWindowListener(this);    //remove window listener
        this.dispose();                     //dispose this
    }

    @Override
    public void windowIconified(WindowEvent e)
    {

    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {

    }

    @Override
    public void windowActivated(WindowEvent e)
    {
        messageLabel.setText("A window activated");     //window activated message
    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {
        messageLabel.setText("A window deactivated");   //window deactivated message
    }
}