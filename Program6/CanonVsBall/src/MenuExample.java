import java.awt.*;
import java.awt.event.*;

public class MenuExample implements ActionListener, WindowListener, ItemListener{
    private int sw = 650, sh = 480; //width and height of screen
    private Frame EditorFrame;  //frame for app
    private TextArea EditArea;  //text area for typing
    private MenuBar MMB;        //menu bar
    private Menu FILE, TEXT;    //main items on the menu bar
    private Menu NEW, SIZE, FONT;   //sub menu items under menu items
    private MenuItem FOLDER, DOCUMENT;  //terminal items in New menu
    private MenuItem QUIT;  //menu item
    private CheckboxMenuItem S10, S14, S18; //checkbox menu items for size
    private CheckboxMenuItem TNR, CO;       //checkbox menu items for font

    private int FontType = Font.PLAIN;
    private String FontStyle = "TimesNewRoman";
    private int FontSize = 14;

    public static void main(String[] args)
    {
        new MenuExample();
    }

    public MenuExample()
    {
        EditArea = new TextArea("", sw-10, sh-10, TextArea.SCROLLBARS_BOTH);

        EditorFrame = new Frame("Editor");  //frame for application
        EditorFrame.setLayout(new BorderLayout(0,0));   //Frame borderlayout
        EditorFrame.setBackground(Color.LIGHT_GRAY);    //background color
        EditorFrame.setForeground(Color.black);     //frame foreground color
        EditorFrame.add("Center", EditArea);    //add textarea

        //specify the structure sequentially, left to right
        MMB = new MenuBar();        //create menu bar
        FILE = new Menu("FILE");        //create FIRST menu entry for menu bar
        NEW = new Menu("New");          //creat first menu entry for file menu

        //add MenuItem Folder with shortcut key to Menu entry new
        FOLDER = NEW.add(new MenuItem("Folder", new MenuShortcut(KeyEvent.VK_F)));
        //add MenuItem Document with shortcut entry to menu entry new
        DOCUMENT = NEW.add(new MenuItem("Document", new MenuShortcut(KeyEvent.VK_D)));

        //add and finish first menuBar entry file
        FILE.add(NEW);      //add menu NEW to FILE menu
        FILE.addSeparator();    //add separator to file
        //add menuitem quit with shortcut key to File Menu
        QUIT = FILE.add(new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q)));

        //Specify second menu entry
        TEXT = new Menu("TEXT");    //create second menu entry for menu bar
        SIZE = new Menu("Size");    //create first menu entry for text menu
        FONT = new Menu("Font");    //create second menu entry for text menu

        //create font size CheckboxMenuItems and add to SIZE menu
        SIZE.add(S10 = new CheckboxMenuItem("10")); //CheckBoxMenuItem size 10
        SIZE.add(S14 = new CheckboxMenuItem("14")); //CheckBoxMenuItem size 14
        SIZE.add(S18 = new CheckboxMenuItem("18")); //CheckBoxMenuItem size 18

        //Set Size 14 active initially
        S14.setState(true);     //initialize Checkbox selection to 14

        //Add size menu to text menu
        TEXT.add(SIZE);     //add Size Menu to text Menu

        //create font style checkboxmenuitems and add to FONT menu
        FONT.add(TNR = new CheckboxMenuItem("TimesNewRoman"));
        FONT.add(CO = new CheckboxMenuItem("Courier"));

        TNR.setState(true); //set TNR to true initially
        TEXT.add(FONT);

        //add the file and TEXT menu's bar to the menu bar
        MMB.add(FILE);
        MMB.add(TEXT);

        //add listeners for Menu item's and shortcuts
        DOCUMENT.addActionListener(this);
        FOLDER.addActionListener(this);
        QUIT.addActionListener(this);

        //turn on listeners for CheckboxMenuItem;s
        S10.addItemListener(this);
        S14.addItemListener(this);
        S18.addItemListener(this);
        TNR.addItemListener(this);
        CO.addItemListener(this);

        //add menu bar to frame
        EditorFrame.setMenuBar(MMB);

        //turn on windowlistener, size, and validate frame
        EditorFrame.addWindowListener(this);
        EditorFrame.setSize(sw, sh);
        EditorFrame.setResizable(true);
        EditorFrame.setVisible(true);
        EditorFrame.validate();

        //set font function on current settings
        setTheFont();
    }

    public void setTheFont()
    {
        FontSize = 10;
        if (S10.getState() == true)
        {
            FontSize = 10;
        }
        if (S14.getState() == true)
        {
            FontSize = 14;
        }
        if (S18.getState() == true)
        {
            FontSize = 18;
        }

        FontStyle = "TimesNewRoman";
        if(TNR.getState() == true)
        {
            FontStyle = "TimesNewRoman";
        }
        if(CO.getState() == true)
        {
            FontStyle = "Courier";
        }

        FontType = Font.PLAIN;
        EditArea.setFont(new Font(FontStyle, FontType, FontSize));
    }


    @Override
    public void actionPerformed(ActionEvent e) //
    {
        Object source = e.getSource();
        if (source == FOLDER)
        {
            EditArea.append("\nFolder\n");
        }
        if (source == DOCUMENT)
        {
            EditArea.append("\nDOCUMENT\n");
        }
        if (source == QUIT)
        {
            stop();
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e)   //changes checkmark to selected item and updates font
    {
        CheckboxMenuItem checkbox = (CheckboxMenuItem) e.getSource();
        if(checkbox == S10 || checkbox == S14 || checkbox == S18)
        {
            S10.setState(false);
            S14.setState(false);
            S18.setState(false);
            checkbox.setState(true);
        }
        if(checkbox == TNR || checkbox == CO)
        {
            TNR.setState(false);
            CO.setState(false);
            checkbox.setState(true);
        }
        setTheFont();

    }

    public void stop()
    {
        DOCUMENT.removeActionListener(this);
        FOLDER.removeActionListener(this);
        QUIT.removeActionListener(this);
        S10.removeItemListener(this);
        S14.removeItemListener(this);
        S18.removeItemListener(this);
        TNR.removeItemListener(this);
        CO.removeItemListener(this);
        EditorFrame.removeWindowListener(this);
        EditorFrame.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        stop();
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