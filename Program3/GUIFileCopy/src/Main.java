import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

public class Main extends Frame implements WindowListener, ActionListener {
    public static void main(String[] args)
    {
        String name = "";
        switch(args.length)
        {
            case 0:
                new Main(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));
                break;
            case 1:
                name = args[0];
                File test = new File(name);
                if (test.isDirectory())
                {
                    new Main(new File(test.getAbsolutePath())); //passes directory into constructor
                }
                else
                {
                    System.out.print("Invalid Directory entry");
                }
                break;
        }




        //new Main();
    }

    private static final long serialVersionUID = 1L;
    Label Source = new Label();
    Label filename = new Label();

    Label direction = new Label();
    Button Target = new Button("Target:");
    Button OK = new Button("OK");

    List list = new List(13);	//create 13 row list

    TextField FileName = new TextField(80);

    Label messageLabel = new Label();

    Boolean SourceBool;//Created Flags in main
    Boolean TargetBool;
    Boolean OutFile;

    File curDir = new File("");
    File originalDirectory = new File("");

    Main(File directory)
    {	//set up for GridBagLayout
        System.out.println("Got directory:" + directory.getAbsolutePath());
        curDir = new File(String.valueOf(directory.getAbsolutePath()));
        originalDirectory = curDir;
        File[] filenames = directory.listFiles();
        //filenames[0] = "..";
        SourceBool = false;//Changed flags to false in Constructor. source is "operations of constructor" slide bullet point 3
        TargetBool = false;
        OutFile = false;


        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout displ = new GridBagLayout();
        double colWeight[] = {1, 3, 1};
        double rowWeight[] = {1, 1, 1, 1};
        int colWidth[] = {1};
        int rowHeight[] = {1, 2, 2, 3};
        displ.rowHeights = rowHeight;
        displ.columnWidths = colWidth;
        displ.columnWeights = colWeight;
        displ.rowWeights = rowWeight;
        this.setBounds(0, 0, 900, 300);
        this.setLayout(displ);
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.setTitle(curDir.getAbsolutePath());


        list.setSize(300, 800);		//set an initial size of 300 pixel by 800 pixel
        c.gridwidth = 4;		//set constraints for 4 columns
        c.gridheight = 1;		//set constraints for 1 row
        c.fill = GridBagConstraints.BOTH; //stretch to fill both directions
        c.gridx = 0;		//position in the List starting in cell 0,0 (upper left)
        c.gridy = 0;
        displ.setConstraints(list, c);		//apply constraints
        this.add(list);	//add list to the frame


        c.gridwidth = 1;		//set constraints for 1 columns
        c.gridheight = 1;		//set constraints for 1 row
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        displ.setConstraints(Target, c);
        this.add(Target);


        c.gridx = 0;
        c.gridy = 1;
        displ.setConstraints(Source, c);
        this.add(Source);
        Source.setText("Source:");
        c.gridx = 1;
        c.gridy = 3;
        displ.setConstraints(FileName, c);
        this.add(FileName);


        c.gridwidth = 1;		//set constraints for 1 columns
        c.gridheight = 1;		//set constraints for 1 row
        c.gridx = 0;
        c.gridy = 3;
        displ.setConstraints(filename, c);
        this.add(filename);
        filename.setText("File Name:");


        c.gridwidth = 1;		//set constraints for 1 columns
        c.gridheight = 1;		//set constraints for 1 row
        c.gridx = 2;
        c.gridy = 3;
        displ.setConstraints(OK, c);
        this.add(OK);

        c.gridwidth = 1;		//set constraints for 1 columns
        c.gridheight = 1;		//set constraints for 1 row
        c.gridx = 1;
        c.gridy = 2;
        displ.setConstraints(direction, c);
        this.add(direction);
        direction.setText("Select Target Directory: ");


        this.setVisible(true);
        Target.addActionListener(this);
        Target.setEnabled(false);
        OK.addActionListener(this);
        OK.setEnabled(true);
        list.addActionListener(this);		//add ActionListener to the list
        list.add(directory.getAbsolutePath());
        for (int i = 0; i < filenames.length; i++) {
            list.add(String.valueOf(filenames[i]));
        }
        FileName.addActionListener(this);
        this.addWindowListener(this);
        display(null);
    }

    void display(String name)
    {
        String[] filenames = new String[10];
        //curDir = new File(name);
        //int i = 0;
        if(name != null)
        {
            if(name == "..")
            {
                curDir = new File(curDir.getParent());
            }
            else
            {
                File f = new File(curDir, name);
                if(f.isDirectory()) {
                    curDir = new File(curDir, name);
                }else {
                    if(!SourceBool || !TargetBool) {
                        Source.setText(curDir.getAbsolutePath()+"\\"+name);
                        SourceBool = true;
                        Target.setEnabled(true);
                    }else {
                        FileName.setText(name);
                        OutFile = true;
                    }
                }
            }
        }
        else
        {
            filenames = curDir.list();
            this.setTitle(curDir.getAbsolutePath());
            if(filenames != null)
            {
                int i =0;
                while (i < filenames.length)
                {
                    File f = new File(curDir, filenames[i]);
                    if(f.isDirectory())
                    {
                        String[] children = f.list();
                        int j = 0;
                        boolean flag = false;
                        while(j < children.length || flag)
                        {
                            File test = new File(children[j]);
                            if (test.isDirectory())
                            {
                                filenames[i] += " +";
                                flag = true;
                            }
                            j++;
                        }

                    }
                    i++;
                }
            }

            list.removeAll();
            if ((curDir.getParent() != null)) //list.add("..")???
            {
                list.add("..");
            }

            if (filenames != null)
            {
                int i = 0;
                while (i < filenames.length)
                {
                    list.add(filenames[i]);
                    i++;
                }
            }


        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current event source
        if (source == Target) {
            direction.setText(curDir.getAbsolutePath());

            TargetBool = true;
            //  Sourcebool = false;??????????????
        }
        if (source == FileName) {
            String Filename = FileName.getText();
            //check input file does not equal output file??
            //check have both input, output
            //do file copy
            //display error to message if input file not there? or check if valid input when check if have both?
            //reset entire program
            curDir = originalDirectory;
            display(null);
            Source.setText("");
            direction.setText("");
            FileName.setText("");
            //messageLabel.setText("");
            TargetBool = false;
            SourceBool = false;
            Target.setEnabled(false);
        }
        if (source == list) {
            String item = list.getSelectedItem();
            display(item);

            if (TargetBool == true) {
                direction.setText(curDir.getAbsolutePath());

                //setting FileName to absolute path plus apprpriate filename if file
                File itemfile = new File(curDir.getParent(), item);

                if ((item != "...") && (!(itemfile.isDirectory()))) // if have regular file
                {

                    FileName.setText(curDir.getAbsolutePath() + "\\" + item);

                } else {

                    FileName.setText(curDir.getAbsolutePath()); //if have parent, or directory
                }
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.removeWindowListener(this);
        Target.removeActionListener(this);
        OK.removeActionListener(this);
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

