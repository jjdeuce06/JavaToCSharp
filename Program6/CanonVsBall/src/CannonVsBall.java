import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;
import java.util.Timer;
import java.util.*;

/*
Group 2 - Canon vs Ball program
John Gerega (ger9822@pennwest.edu
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu)
This is an extension of the Bounce Program, in this program, a border layout, a menu, a polygon class, and a grid bag layout is used
The grid bag layout contains scrollbars for this program
The menu contains the buttons for, stop, run, pause, quit, the gravity checkboxes, and the size and speed checksboxes
The border layout is where the object (a ball) bounces, projectile, and canon
This program implemenets the use of Vectors, Rectangles, Points, polygons, and mouse events
When the mouse is clicked and dragged, a drag box will appear,
if that drag box does not contain the ball, it is filled in and added to the vector when the mouse is released
When the mouse is clicked and is within a rectangle, that rectangle is deleted
When the canon is clicked it will shoot a canon ball
When the canon ball hits rectangles, the ball, and the canon it will destroy them and give the computer a point if it hits the canon or the player a point if the canon hits the ball
Gravity affects the ball and how it moves
Ball bounces accordingly based on perimeter and rectangles
Ball resizes accordingly based on perimeter and rectangles
A thread is used to run the program
*/


public class CannonVsBall extends Frame implements WindowListener, ComponentListener, MouseListener, MouseMotionListener, ActionListener, AdjustmentListener, Runnable, ItemListener
{

    public static void main(String[] args) {
        CannonVsBall b = new CannonVsBall();
    }
    //taken from bounce
    private static final long serialVersionUID = 10L;

    private final int WIDTH = 640;  //initial frame width
    private final int HEIGHT = 480;  //initial frame height
    private final int BUTTONH = 20;     //button height
    private final int BUTTONHS = 5;      //button height spacing

    private int WinWidth = WIDTH;   //initial frame width
    private int WinHeight = HEIGHT;    //initial frame height
    private int ScreenWidth;        //drawing screen width
    private int ScreenHeight;       //drawing screen height
    private int WinTop = 10;        //top of frame
    private int WinLeft = 10;       //left side of frame
    private int BUTTONW = 50;       //initial button width
    private int CENTER = (WIDTH/2); //initial screen center
    private int BUTTONS = BUTTONW/4;        //initial button spacing
    private int ScrollBarW; //scrollbar width

    private final int MAXObj = 100; //maximum object size
    private final int MINObj = 10;  //minimum object size
    private final int SPEED = 50;   //initial speed
    private final int SBvisible = 10;   //visible scroll bar
    private final int SBunit = 1;    //Scroll bar unit step size
    private final int SBblock = 10;  //Scroll bar block step size
    private final int SCROLLBARH = BUTTONH; //scrollbar height
    private final int SOBJ = 21;    //initial object width, need odd value to have even distribution on top, right, left, and bottom
    private int SpeedSBmin = 1; //speed scrollbar minimum value
    private int SpeedSBmax = 100+SBvisible;  //speed scrollbar maximum value with visible offset
    private int SpeedSBinit = SPEED;    //initial speed scrollbar value
    private int delay = 4;      //initial delay speed
    private Thread thethread; //thread for timer delay

    //added for bouncing ball
    private Point m1 = new Point(0, 0); //starting mouse position
    private Point m2 = new Point(0, 0); //updating mouse position
    private Point Screen = new Point(WIDTH-1, HEIGHT-1);    //make Point named screen
    private Rectangle Perimeter = new Rectangle(0, 0, ScreenWidth, ScreenHeight);   //make perimeter rectangle
    private Rectangle db = new Rectangle();     //make db rectangle for dragboxes

    private static final Rectangle ZERO = new Rectangle(0, 0, 0, 0);




    private Ballc Ball;       //object to draw
    private Label SPEEDL = new Label("Velocity", Label.CENTER);        //label for speed scroll bar
    private Label SIZEL = new Label("Angle", Label.CENTER);  //label for scroll size bar
    private Label Player = new Label("Player 1: ", Label.LEFT);
    private Label Comp = new Label("Computer: ", Label.LEFT);
    private Label PScore = new Label("", Label.CENTER);
    private Label CScore = new Label("", Label.CENTER);
    private Label CBallM = new Label("CannonBall Status: ", Label.LEFT);
    private Label CBallS = new Label("Gone", Label.CENTER);
    private int score;
    private int pscore;
    private String player;
    private String comp;

    Scrollbar VelocityScrollBar, AngleScrollBar; //scrollbars

    private Insets I;       //insets of frame

    Button Start, Pause, Quit;     //Buttons
    private boolean runBall = true, Timepause = false, start = false, good;       //flags for running object, timepause, and start
    private Panel sheet = new Panel();      //make new panel named sheet
    private Panel control = new Panel();    //make new panel named control
    private GridBagConstraints c;       //create GBC object named c
    private GridBagLayout gbl;      //create GridBagLayout object named gbl

    private CheckboxMenuItem S10, S12, S14, S18, S20; //checkbox menu items for size
    private CheckboxMenuItem SP2, SP4, SP6, SP8, SP10;  //Speed
    private CheckboxMenuItem Stop, Restart;
    private CheckboxMenuItem Mercury, Venus, Earth, Moon, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto;
    private MenuBar MMB;        //menu bar
    private Menu SIZE, BSPEED, CONTROL, GRAV, BALLCONTROL;    //main items on the menu bar
    private MenuItem Run, BQuit;

    private double VInit; // initial velocity of projectile
    private double dx; //change in x direction
    private double dy; //change in y direction
    private double dt; //change in time
    private double dvy; //change in y velocity
    private double gravOfPlanet; //gravity of desired planet
    private double yVel; //y velocity
    private double totalTime; //total time of projectile
    private double totalX; //total x distance

    public CannonVsBall()
    {
        setLayout(new BorderLayout());
        setVisible(true);
        MakeSheet();        //determine sizes for sheet
        try
        {
            initComponents();   //try to initialize components
        }
        catch (Exception e){e.printStackTrace();}
        SizeScreen();       //size items on the screen
        start();            //calls start method
    }

    private void MakeSheet()
    {
        I = getInsets();    //get insets
        ScreenWidth = WinWidth - I.left - I.right;  //make screen size the width of the frame less the left and right insets
        ScreenHeight = WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;   //make screen height the height of the frame less the top and bottom insets and space for two rows of buttons and two button spaces
        CENTER = (ScreenWidth/2);       //determine center of screen
        setSize(WinWidth, WinHeight);   //set frame size



    }

    public void initComponents() throws Exception, IOException
    {
        this.setBounds(Perimeter);
        this.setVisible(true);

        //initialize c, gbl, and create borderlayout named b
        c = new GridBagConstraints();
        gbl = new GridBagLayout();
        BorderLayout b = new BorderLayout();

        //initialize gbl weights and heights of columns and rows
        double colWeight[] = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2};
        double rowWeight[] = {1, 1, 1};
        int colWidth[] = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2};
        int rowHeight[] = {1, 1, 1};
        gbl.rowHeights = rowHeight;
        gbl.columnWidths = colWidth;
        gbl.columnWeights = colWeight;
        gbl.rowWeights = rowWeight;

        //initialize buttons
        Start = new Button("Run");
        Pause = new Button("Pause");
        Quit = new Button("Quit");
        Start.setEnabled(false);
        Pause.setEnabled(true);

        VelocityScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);   //create speed scroll bar
        VelocityScrollBar.setMaximum(SpeedSBmax);      //set max speed
        VelocityScrollBar.setMinimum(SpeedSBmin);      //set minimum speed
        VelocityScrollBar.setUnitIncrement(SBunit);    //set the unit increment
        VelocityScrollBar.setBlockIncrement(SBblock);  //set the block increment
        VelocityScrollBar.setValue(SpeedSBinit);       //set the initial value
        VelocityScrollBar.setVisibleAmount(SBvisible); //set the visible size
        VelocityScrollBar.setBackground(Color.gray);   //set the background color

        AngleScrollBar = new Scrollbar(Scrollbar.HORIZONTAL); //create size scroll bar
        AngleScrollBar.setMaximum(MAXObj);                    //set max speed
        AngleScrollBar.setMinimum(MINObj);                    //set minimum speed
        AngleScrollBar.setUnitIncrement(SBunit);              //set unit increment
        AngleScrollBar.setBlockIncrement(SBblock);            //set block increment
        AngleScrollBar.setValue(SOBJ);                        //set initial value
        AngleScrollBar.setVisibleAmount(SBvisible);           //set visible size
        AngleScrollBar.setBackground(Color.gray);             //set background color
        Ball = new Ballc(SOBJ, ScreenWidth, ScreenHeight);        //create object
        Ball.setBackground(Color.white);

        //add scrollbars, labels, and Ball object
        add(VelocityScrollBar);        //add speed scroll bar to frame
        add(AngleScrollBar);      //add size scroll bar to frame
        add(SPEEDL);                //add speed label to the frame
        add(SIZEL);                 //add size label to the frame
        add(Player);
        add(Comp);
        add(PScore);
        add(PScore);
        add(Ball);                   //add object to the frame

        //initialize points and Rectangles
        m1.setLocation(0,0);
        m2.setLocation(0,0);
        Perimeter.setBounds(Ball.getx(), Ball.gety(), ScreenWidth, ScreenHeight);
        Perimeter.grow(-1, -1);
        setLayout(new BorderLayout());
        setBounds(WinLeft, WinTop, WIDTH, HEIGHT);
        setBackground(Color.LIGHT_GRAY);
        setVisible(true);

        sheet.setLayout(b);
        sheet.setBackground(Color.lightGray);
        sheet.add("Center", Ball);
        setVisible(true);


        control.setLayout(gbl); //set layout of control to a GridBagLayout

        //constraints for Velocity label
        c.weightx = 2;
        c.weighty = 1;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        gbl.setConstraints(SPEEDL, c);
        SPEEDL.setVisible(true);
        control.add(SPEEDL);

        //constraints for speed scrollbar
        c.weightx = 3;
        c.weighty = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(VelocityScrollBar, c);
        VelocityScrollBar.setVisible(true);
        control.add(VelocityScrollBar);

        //constraints for cannonball status message
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(CBallM, c);
        CBallM.setVisible(true);
        control.add(CBallM);

        //constraints for cannon ball status
        c.weightx = 2;
        c.weighty = 1;
        c.gridx = 5;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(CBallS, c);
        CBallS.setVisible(true);
        control.add(CBallS);

        //constraints for start button
        //c.weightx = 1;
        c.weighty = 1;
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(Player, c);
        Player.setVisible(true);
        control.add(Player);

        //c.weightx = 1;
        c.weighty = 1;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(PScore, c);
        PScore.setVisible(true);
        PScore.setText("0");
        control.add(PScore);


        //constraints for pause button
        c.weightx = 0;
        c.weighty = 1;
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(Comp, c);
        Comp.setVisible(true);
        control.add(Comp);

        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 6;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(CScore, c);
        CScore.setVisible(true);
        CScore.setText("0");
        control.add(CScore);


        //constraints for size scrollbar
        c.weightx = 3;
        c.weighty = 1;
        c.gridx = 8;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(AngleScrollBar, c);
        AngleScrollBar.setVisible(true);
        control.add(AngleScrollBar);

        //constraints for size label
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 8;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(SIZEL, c);
        SIZEL.setVisible(true);
        control.add(SIZEL);


        //add listeners and validate frame
        add("Center", sheet);
        add("South", control);
        this.addComponentListener(this);
        this.addWindowListener(this);
        Start.addActionListener(this);
        Pause.addActionListener(this);
        Quit.addActionListener(this);
        VelocityScrollBar.addAdjustmentListener(this);     //add speedscroll listener
        AngleScrollBar.addAdjustmentListener(this);   //add speedscroll listener
        Ball.addMouseListener(this);
        Ball.addMouseMotionListener(this);

        //specify the structure sequentially, left to right
        MMB = new MenuBar();        //create menu bar
        CONTROL = new Menu("Control");



        //CONTROL.add(Run = new CheckboxMenuItem("Run"));
        CONTROL.add(Stop = new CheckboxMenuItem("Stop"));
        CONTROL.add(Restart = new CheckboxMenuItem("Restart"));
        //CONTROL.add(BQuit = new CheckboxMenuItem("Quit"));


        Run = CONTROL.add(new MenuItem("Run", new MenuShortcut(KeyEvent.VK_R)));
        BQuit = CONTROL.add(new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q)));


        MMB.add(CONTROL);


        BALLCONTROL = new Menu("Ball Control");
        SIZE = new Menu("Size");        //create FIRST menu entry for menu bar
        SIZE.add(S10 = new CheckboxMenuItem("10")); //CheckBoxMenuItem size 10
        SIZE.add(S12 = new CheckboxMenuItem("12")); //CheckBoxMenuItem size 12
        SIZE.add(S14 = new CheckboxMenuItem("14")); //CheckBoxMenuItem size 14
        SIZE.add(S18 = new CheckboxMenuItem("18")); //CheckBoxMenuItem size 18
        SIZE.add(S20 = new CheckboxMenuItem("20")); //CheckBoxMenuItem size 20
        S14.setState(true);

        BALLCONTROL.add(SIZE);

        //creates the speed checkboxes
        BSPEED = new Menu("Speed");
        BSPEED.add(SP2 = new CheckboxMenuItem("2"));
        BSPEED.add(SP4 = new CheckboxMenuItem("4"));
        BSPEED.add(SP6 = new CheckboxMenuItem("6"));
        BSPEED.add(SP8 = new CheckboxMenuItem("8"));
        BSPEED.add(SP10 = new CheckboxMenuItem("10"));
        SP6.setState(true);

        BALLCONTROL.add(BSPEED);

        MMB.add(BALLCONTROL);

        //creates the gravity checkboxes
        GRAV = new Menu("Gravity");
        GRAV.add(Mercury = new CheckboxMenuItem("Mercury"));
        GRAV.add(Venus = new CheckboxMenuItem("Venus"));
        GRAV.add(Earth = new CheckboxMenuItem("Earth"));
        GRAV.add(Moon = new CheckboxMenuItem("Moon"));
        GRAV.add(Mars = new CheckboxMenuItem("Mars"));
        GRAV.add(Jupiter = new CheckboxMenuItem("Jupiter"));
        GRAV.add(Saturn = new CheckboxMenuItem("Saturn"));
        GRAV.add(Uranus = new CheckboxMenuItem("Uranus"));
        GRAV.add(Neptune = new CheckboxMenuItem("Neptune"));
        GRAV.add(Pluto = new CheckboxMenuItem("Pluto"));
        Earth.setState(true);

        MMB.add(GRAV);


        S10.addItemListener(this);
        S12.addItemListener(this);
        S14.addItemListener(this);
        S18.addItemListener(this);
        S20.addItemListener(this);
        SP2.addItemListener(this);
        SP4.addItemListener(this);
        SP6.addItemListener(this);
        SP8.addItemListener(this);
        SP10.addItemListener(this);
        Run.addActionListener(this);
        BQuit.addActionListener(this);
        Restart.addItemListener(this);
        Stop.addItemListener(this);
        Mercury.addItemListener(this);
        Venus.addItemListener(this);
        Earth.addItemListener(this);
        Moon.addItemListener(this);
        Mars.addItemListener(this);
        Jupiter.addItemListener(this);
        Saturn.addItemListener(this);
        Uranus.addItemListener(this);
        Neptune.addItemListener(this);
        Pluto.addItemListener(this);
        this.setMenuBar(MMB);

        setTheSize();
        changeGrav();
        validate();





    }

    public void start()     //start method
    {
        if (thethread == null)      //if thread is not created
        {
            thethread = new Thread(this);   //create new thread
            thethread.start();  //start thread
        }
        runBall = true;
        Ball.setx((Ball.getOSize()-1)/2);
        Ball.sety((Ball.getOSize()-1)/2);
        Ball.repaint();      //repaint object
    }

    public void run()       //run method
    {

        while (runBall)      //while runobj is true
        {
            //updates the player and computer score each interaction of the program
            score = Ballc.getScore();
            comp = String.valueOf(score);
            pscore = Ballc.getPScore();
            player = String.valueOf(pscore);
            PScore.setText(player);
            CScore.setText(comp);

            try
            {
                thethread.sleep(1);     //delay for 1 millisecond
            }
            catch(InterruptedException e){};
            if (Timepause == false)     //check timepause flag
            {
                start = true;       //if false, set start to true
                try
                {
                    thethread.sleep(delay);     //delay object movement by delay speed grabbed from scrollbar
                }
                catch(InterruptedException e){};
                //Obj.update
                Ball.checkRec();
                Ball.move();     //move the object
                if(Ball.getdraw())
                {
                    Ball.moveP();
                    CBallS.setText("Going");
                }
                else {
                    CBallS.setText("Gone");
                }

                /*if(Ball.getdraw() == true)
                {
                    Ball.setUpC();
                }*/
                //Ball.repaint();      //repaint

            }
        }
    }




    public void stop()      //stop method
    {
        //interrupt thread, remove everything from the screen, dispose, and then exit
        runBall = false;
        thethread.interrupt();
        Start.removeActionListener(this);
        Pause.removeActionListener(this);
        Quit.removeActionListener(this);
        VelocityScrollBar.removeAdjustmentListener(this);
        AngleScrollBar.removeAdjustmentListener(this);
        this.removeComponentListener(this);
        dispose(); // Dispose of the window
        this.removeWindowListener(this); // Remove the window listener after disposing of the window
        this.removeComponentListener(this);
        this.removeMouseMotionListener(this);
        this.removeMouseListener(this);
        System.exit(0); // Exit the program
    }

    private void SizeScreen()
    {
        Ball.setBounds(0, 0, ScreenWidth, ScreenHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Point check = e.getPoint(); //get the point
        Rectangle cbase = new Rectangle(ScreenWidth - 100, ScreenHeight - 100, 100, 100);
        Rectangle deletecheck = new Rectangle();    //create new rectangle
        int clicks = e.getClickCount();
        if (clicks == 1)
        {
            if (cbase.contains(check)) {
                Ball.setdraw(true);
                Ball.startTime();
            }
        }
        else if (clicks > 1) {
            int i = 0;
            while (i < Ball.getWallSize()) {
                deletecheck = Ball.getOne(i);   //set temp rectangle to rectangle at ith vector position
                if (deletecheck.contains(check)) {
                    //if point clicked is within that rectangle, delete it
                    Ball.removeOne(i);
                }
                i++;    //increment i
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        m1.setLocation(e.getPoint());   //get point
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        good = true;    //set good to true
        //set index to 0, get a half size, create new rectangles
        int i = 0;
        int half = (Ball.getOSize()-1)/2;
        Rectangle test = new Rectangle(Ball.getx() - half, Ball.gety()-half, Ball.getOSize(), Ball.getOSize()); //ball
        Rectangle grab = new Rectangle();   //used for vector
        Rectangle cbase = new Rectangle(ScreenWidth - 100, ScreenHeight - 100, 100, 100);

        if (db.intersects(test) || db.intersects(cbase))    //if db intersects the ball
        {
            good = false;   //not a good rectangle
            db.setBounds(0, 0, 0, 0);   //reset
            Ball.setDragBox(db);    //set dragbox to all 0
            Ball.repaint(); //repaint
        }

        do {
            if (i == 0)
            {
                Ball.addOne(grab);  //add the rectangle if no rectangles
            }
            grab = Ball.getOne(i);  //set grab to first index
            if (db.intersection(grab).equals(db))
            {
                //if grab db covers grab, do not add
                good = false;
                db.setBounds(0, 0, 0, 0);
                Ball.setDragBox(db);
                Ball.repaint();
            }
            if(grab.intersection(db).equals(grab))
            {
                //if new rectangle covers grab, remove grab
                Ball.removeOne(i);
            }
            else
            {
                i++;    //increment i
            }


        }while ((i < Ball.getWallSize()) && good);      //do all this while boolean is true and i is less than Vector size

        if(good)    //if good is still true
        {
            Ball.addOne(db);    //add the rectangle, reset db
            db.setBounds(0, 0, 0, 0);
            Ball.setDragBox(db);
            Ball.repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Ball.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        db.setBounds(getDragBox(e));    //set bounds for db and get the dragbox of e

        if(Perimeter.contains(db))
        {
            Ball.setDragBox(db);    //if db is within perimeter, set the dragbox db and repaint
            Ball.repaint();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();      //get source

        if (source == Run)
        {
            Timepause = false;
        }

        if (source == BQuit)
        {
            stop();                 //stop the program
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int TS;
        Scrollbar sb = (Scrollbar)e.getSource();    //get scrollbar that trigger event
        if(sb == AngleScrollBar)//changes angle of the canon
        {
            TS = e.getValue();
            int old = Ball.getAngle();
            if (TS >= 0 && TS <= 90)
            {
                Ball.setAngle(TS);
                Ball.repaint();
            }
            else
            {
                sb.setValue(old);
            }

        }

        if(sb == VelocityScrollBar)//changes initial velocity of the canon ball
        {

            TS = e.getValue();
            Ball.setVelocity(TS*20);

        }

    }

    @Override
    public void componentResized(ComponentEvent e) {
        WinWidth = getWidth();      //get the width
        WinHeight = getHeight();    //get the height
        ScreenWidth = WinWidth - I.left - I.right;  //set the screenwidth to the window width minus the right and left insets
        ScreenHeight = WinHeight - I.top - 2 * (BUTTONH + BUTTONHS) - I.bottom; //set the screenheight to top inset minus 2 * the sum of BUTTONH and BUTTONHS subtracted by insets bottom


        for(int i = 0; i < Ball.getWallSize(); i++)
        {
            Rectangle temp = Ball.getOne(i);
            //check if rectangle is within perimeter
            if(!temp.equals(temp.intersection(Perimeter)))
            {
                //if not, set perimeter accordingly
                int tright = temp.x + temp.width + 1;
                int tbottom = temp.y + temp.height + 1;
                ScreenWidth = tright;
                ScreenHeight = tbottom;
            }
            else
            {
                WinWidth = getWidth();      //get the width
                WinHeight = getHeight();    //get the height
            }
        }


        // Resize the object canvas
        Ball.reSize(ScreenWidth, ScreenHeight);      //resizes canvas
        Perimeter.setBounds(Ball.getx(), Ball.gety(), ScreenWidth, ScreenHeight);
        Ball.setMaxy(Ball.findMaxY());                //set maximum values
        Ball.setMiny(Ball.findMinY());
        Ball.setMaxx((Ball.findmaxX()));
        Ball.setMinx(Ball.findminx());
        Ball.reSize(ScreenWidth, ScreenHeight);      //resizes canvas

        int x = Ball.getx();     //store x and y in variables
        int y = Ball.gety();
        if(x > Ball.findmaxX() || y > Ball.findMaxY())        //check if object is still on the screen
        {
            //redraw to be on the screen
            Ball.setx((Ball.getOSize()-1)/2);
            Ball.sety((Ball.getOSize()-1)/2);
            //Ball.repaint();
        }
        // Recalculate and resize components
        MakeSheet();
        SizeScreen();
        Ball.setpx(ScreenWidth-55);
        Ball.setpy(ScreenHeight-55);
        Ball.repaint();





    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    private Rectangle getDragBox(MouseEvent e)
    {   //get the mouse point
        m2.setLocation(e.getPoint());
        Rectangle potential = new Rectangle();

        //find out what quadrant the mouse is dragged using minimum and absolute values of m1 and m2
        potential.setBounds(Math.min(m2.x, m1.x), Math.min(m1.y, m2.y), Math.abs(m2.x-m1.x), Math.abs(m2.y - m1.y));

        return potential;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        CheckboxMenuItem checkbox = (CheckboxMenuItem) e.getSource();
        //checks teh state of the size checkbox
        if(checkbox == S10 || checkbox == S12 || checkbox == S14 || checkbox == S18 || checkbox == S20)
        {
            S10.setState(false);
            S12.setState(false);
            S14.setState(false);
            S18.setState(false);
            S20.setState(false);
            checkbox.setState(true);
        }

        //checks the state of the speed checkbox
        if(checkbox == SP2 || checkbox == SP4 || checkbox == SP6 || checkbox == SP8 || checkbox == SP10)
        {
            SP2.setState(false);
            SP4.setState(false);
            SP6.setState(false);
            SP8.setState(false);
            SP10.setState(false);
            checkbox.setState(true);
        }

        //checks the state of the stop and restart checksboxes
        if(checkbox == Stop || checkbox == Restart)
        {
            Stop.setState(false);
            Restart.setState(false);
            checkbox.setState(true);
        }

        //checks the state of the gravity checkbox
        if(checkbox == Mercury || checkbox == Venus || checkbox == Earth || checkbox == Moon || checkbox == Mars
                || checkbox == Jupiter || checkbox == Saturn || checkbox == Uranus || checkbox == Neptune || checkbox == Pluto)
        {
            Mercury.setState(false);
            Venus.setState(false);
            Earth.setState(false);
            Moon.setState(false);
            Mars.setState(false);
            Jupiter.setState(false);
            Saturn.setState(false);
            Uranus.setState(false);
            Neptune.setState(false);
            Pluto.setState(false);
            checkbox.setState(true);
        }

        changeGrav();
        setTheSize();
    }

    public void changeGrav()
    {
        //sets the gravity of the game
        if(Mercury.getState() == true)
        {
            Ball.setGravOfPlanet(3.7/100);
        }
        if(Venus.getState() == true)
        {
            Ball.setGravOfPlanet(8.87/100);
        }
        if(Earth.getState() == true)
        {
            Ball.setGravOfPlanet(9.8/100);
        }
        if(Moon.getState() == true)
        {
            Ball.setGravOfPlanet(1.62/100);
        }
        if(Mars.getState() == true)
        {
            Ball.setGravOfPlanet(3.71/100);
        }
        if(Jupiter.getState() == true)
        {
            Ball.setGravOfPlanet(24.79/100);
        }
        if(Saturn.getState() == true)
        {
            Ball.setGravOfPlanet(10.44/100);
        }
        if(Uranus.getState() == true)
        {
            Ball.setGravOfPlanet(8.87/100);
        }
        if(Neptune.getState() == true)
        {
            Ball.setGravOfPlanet(11.15/100);
        }
        if(Pluto.getState() == true)
        {
            Ball.setGravOfPlanet(0.62/100);
        }

        //pauses program
        if(Stop.getState() == true)
        {
            Timepause = true;
        }

        //restarts the code setting everything to the begining
        if(Restart.getState() == true)
        {
            start();
            Ball.setScore();
            Ball.setPscore();
            Ball.setGravOfPlanet(gravOfPlanet);
            S10.setState(false);
            S12.setState(false);
            S18.setState(false);
            S20.setState(false);
            SP2.setState(false);
            SP4.setState(false);
            SP8.setState(false);
            SP10.setState(false);
            Stop.setState(false);
            Restart.setState(false);
            Mercury.setState(false);
            Venus.setState(false);
            Moon.setState(false);
            Mars.setState(false);
            Jupiter.setState(false);
            Saturn.setState(false);
            Uranus.setState(false);
            Neptune.setState(false);
            Pluto.setState(false);
            SP6.setState(true);
            S14.setState(true);
            Earth.setState(true);
            Ball.Clear();
            VelocityScrollBar.setValue(50);
            AngleScrollBar.setValue(50);
        }

    }

    //changes the size of the ball based on the checkbox
    public void setTheSize()
    {
        if (S10.getState() == true)
        {
            Ball.size(10);
        }
        if (S12.getState() == true)
        {
            Ball.size(12);
        }
        if (S14.getState() == true)
        {
            Ball.size(14);
        }
        if (S18.getState() == true)
        {
            Ball.size(18);
        }
        if (S20.getState() == true)
        {
            Ball.size(20);
        }

        if(SP2.getState() == true)
        {
            delay = 2;
        }
        if(SP4.getState() == true)
        {
            delay = 4;
        }
        if(SP6.getState() == true)
        {
            delay = 6;
        }
        if(SP8.getState() == true)
        {
            delay = 8;
        }
        if(SP10.getState() == true)
        {
            delay = 10;
        }
    }
}

class Ballc extends Canvas{
    private static final long serialversionUID = 11L;
    private int ScreenWidth;
    private int ScreenHeight;
    private int SObj;
    private int x, y, minx, miny, xmax, ymax, oldx, oldy;
    private int px, py;
    private boolean ok = true;
    private boolean down, right, good, drawP = false;
    int refX, refY, bx, by; //angle variables
    static int score;   //score variable
    static int pscore;  //pscore variable (player score)
    Point  a1 = new Point(), a2 = new Point(), c1 = new Point(), c2 = new Point();  //points a1, a2, c1, c2 initializaiton
    double angle = Math.toRadians(40), velocity = 80;
    private double VInit = 10.0; // initial velocity of projectile
    private double dx; //change in x direction
    private double dy; //change in y direction
    private double dt = 0.09; //change in time
    private double dvy; //change in y velocity
    private double gravOfPlanet; //gravity of desired planet
    private double xVel, yVel; //y velocity
    private double totalTime; //total time of projectile
    private double totalX; //total x distance

    Image buffer;   //image initialization
    Graphics g; //graphics initialization

    private Vector<Rectangle> Walls = new Vector<Rectangle>();     //vector for our rectangles
    private Rectangle potential = new Rectangle();      //potential made for dragbox
    Rectangle Cball = new Rectangle();

    public Ballc(int SB, int w, int h)
    {
        //set values, set flags to true, find minimum/maximum values
        ScreenWidth = w;
        ScreenHeight = h;
        SObj = SB;
        down = true;
        right = true;
        miny = findMinY();
        minx = findminx();
        xmax = findmaxX();
        ymax = findMaxY();
        y = (SObj-1)/2 - 1;
        x = (SObj-1)/2 + 1;
        px = (c1.x-c2.x) + c2.x;
        py = (c1.x-c2.y) + c2.y;
    }

    //gravOfPlanet setter
    public void setGravOfPlanet(double grav)
    {
        gravOfPlanet = grav;
    }

    public void setDragBox(Rectangle d)
    {
        potential.setBounds(d.x, d.y, d.width, d.height);
    }   //set a drag box
    public void addOne(Rectangle r)
    {
        Walls.addElement(new Rectangle(r));
    }       //add a rectangle

    public void removeOne(int i)
    {
        Walls.removeElementAt(i);
    }           //remove a rectangle

    public Rectangle getOne(int i)
    {
        return Walls.elementAt(i);
    }       //get the rectangle at ith position

    public int getWallSize()
    {
        return Walls.size();
    }       //return size of vector
    public int getx(){return this.x;}       //return x value
    public int gety(){return this.y;}       //return y value

    public void setx(int newx){this.x = newx;}  //set x value
    public void sety(int newy){this.y = newy;}  //set y value
    public int findminx()   //find minimum x
    {
        minx = (SObj-1)/2; // Adjusting for the center of the object
        if (minx < 0)
        {
            return 0;          //returns 0 minx is less than the left screen
        } else
        {
            return minx;        //returns minx otherwise
        }
    }

    public int findmaxX()   //find maximum x
    {
        xmax = ScreenWidth-minx;

        return xmax; //returns xmax
    }

    public int findMinY() { //find minimum y
        miny = (SObj - 1)/2; // Adjusting for the center of the object
        if (miny < 0)
        {
            return 0; //returns 0 miny is less than the left screen
        } else
        {
            return miny;    //return miny if not greater than 0
        }
    }

    public int findMaxY()//find maximum y
    {
        ymax = ScreenHeight - miny;
        return ymax;        //returns ymax otherwise
    }

    public boolean Xcheck()     //check if x fits within border
    {
        if (x - (SObj + 1) / 2 >= minx  && x - (SObj + 1) / 2 < xmax)
        {
            return true;        //return true if x is within border
        }

        return  false;      //return false otherwise

    }

    public boolean Ycheck()     //check if y fits within border
    {
        if (y - (SObj - 1) / 2 >= miny && y + (SObj - 1) / 2 <= ymax)
        {
            return true;            //return true if y is within border
        }

        return false;       //return false otherwise
    }

    public int getOSize(){return this.SObj;}    //return object size
    public void setMinx(int val){this.minx = val;}  //set minimum x
    public void setMaxx(int val){this.xmax = val;}  //set maximum x
    public void setMiny(int val){this.miny = val;}  //set minimum y
    public void setMaxy(int val) {this.ymax = val;} //set maximum y

    public boolean getDown(){return down;}  //get the value of down boolean
    public boolean getRight(){return right;}    //get value of right boolean
    public void setDown(boolean val){down = val;}   //set down flag
    public void setRight(boolean val){right = val;} //set right flag
    private static final Rectangle ZERO = new Rectangle(0,0,0,0);

    {
        Rectangle r = new Rectangle(ZERO);
        Rectangle Ballb = new Rectangle(x, y, SObj, SObj);
        Ballb.grow(1, 1);
        int i = 0;
        while (i < Walls.size() && ok) {
            r = Walls.elementAt(i);
            if (r.intersects(Ballb)) {
                ok = false;
            }
            else
            {
                i++;
            }
        }
    }
    public void checkRec()  //check if ball hits rectangle method
    {
        int i = 0;  //index variable
        int half = (SObj-1)/2;  //half the size


        for(i = 0; i < Walls.size(); i++)   //for loop for vector
        {
            Rectangle BallB = new Rectangle(x, y, SObj, SObj);  //create ball rectangle
            Rectangle Cball = new Rectangle(px, py, SObj, SObj);
            Rectangle cur = Walls.elementAt(i);                 //get current rectangle

            int left = getx() + (SObj + 1) / 2; //initialize ball sides
            int right = getx() - (SObj + 1)/2;
            int top = BallB.y;
            int bottom = gety() - half - 1;

            int cleft = cur.x;                  //initialize rectangle sides
            int cright = cleft + cur.width;
            int ctop = cur.y;
            int cbottom = ctop + cur.height;


            if(BallB.intersects(cur))   //check when the ball hits the rectanlge
            {
                if (left >= cright)     //bounce right if it hits left side
                {
                    setRight(true);

                }

                if(right <= cleft)      //bounce left if it hits right side
                {
                    setRight(false);

                }

                if(bottom <= ctop)      //bounce up if it hits the top
                {
                    setDown(false);

                }

                if(top+1 >= cbottom)    //bounce down if it hits the bottom
                {
                    setDown(true);

                }



                //repaint
                repaint();
            }
            //check if cannonabll hits rectangle
            if (Cball.intersects(cur))
            {
                //remove one if so and set drawp to false
                removeOne(i);
                drawP = false;
            }
        }
    }
    public void move()      //move method
    {

        //find minimum and maximum widths and heights of current screen
        int side1 = findmaxX();
        int side2 = findminx();
        int top = findMinY();
        int bottom = findMaxY();

        //if the object's y next position is greater than the bottom screen, flip the down flag
        if (gety() + (SObj - 1) / 2 >= bottom)
        {
            setDown(false);
        }
        //if the y position is greater than or equal to the top, set down to true
        else if (gety() + (SObj - 1) / 2 <= top)
        {
            setDown(true);
        }

        //if object's x's next position is greater than the right side, set right to false
        if (getx() + (SObj - 1) / 2 >= side1)
        {
            setRight(false);
        }
        //if the x position is less than the left side, set right to true
        else if (getx() + (SObj - 1) / 2 <= side2)
        {
            setRight(true);
        }

        //add 1 if down is true
        if (getDown() == true)
        {
            sety(gety()+1);
        }
        //otherwise, subtract 1
        else
        {
            sety(gety()-1);
        }

        //add one to x if right is true
        if (getRight() == true)
        {
            setx(getx()+1);
        }

        //subtract one otherwise
        else
        {
            setx(getx()-1);
        }



        //repaint
        repaint();
    }
    public int getoldx(){return this.oldx;}     //get old x
    public int getoldy(){return this.oldy;}     //get old y
    public void size(int NS)
    {
        SObj = NS;
    }   //set object size
    public void reSize(int sx, int sy)    //resize function
    {
        //set screenwidth and height
        ScreenWidth = sx;
        ScreenHeight = sy;
        //get old x and y values
        y = getoldy();
        x = getoldx();
        //set new minimum and maximum values
        setMinx(findminx());
        setMaxx(findmaxX());
        setMiny(findMinY());
        setMaxy(findMaxY());
    }

    //px and py setters
    public int getpx(){return this.px;}       //return x value
    public int getpy(){return this.py;}       //return y value

    //px and py setters
    public void setpx(int newx){this.px = newx;}  //set x value
    public void setpy(int newy){this.py = newy;}  //set y value

    //angle getter and setter
    public void setAngle(int n){this.angle = Math.toRadians(n);}
    public int getAngle(){return (int) angle;}

    //drawP getter and setter
    public void setdraw(boolean t){this.drawP = t;}
    public boolean getdraw(){return drawP;};

    public void setUpC()
    {
        //resets the cannonball x and y position
        px = c2.x - 12;
        py = c1.y - 12;

    }

    long startTime;
    long elapsedTime = 0L;

    public void startTime()
    {
        //sets starttime to the current system tume
        startTime = System.currentTimeMillis();
    }
    public long getTime()
    {
        //returns elapsed time the cannon ball runs
        return elapsedTime = ((new Date()).getTime() - startTime) / 1000L;
    }

    public void moveP()
    {
        Cball.setBounds(px, py, 21, 21);
        // Update velocity components
        //yVel = (VInit/10) * Math.cos(angle);

        xVel = (VInit/10) * Math.sin(angle);
        yVel = VInit * Math.sin(getAngle()) - gravOfPlanet * dt;

        //delta x, dt = delta time, determines how much projectile moved since last time
        dx = .5 * VInit * Math.cos(getAngle()) * dt;

        //delta y
        dy = .5 * yVel * Math.sin(getAngle()) * dt - gravOfPlanet * dt * dt;

        //delta y Velocity
        dvy = dvy-gravOfPlanet * dt;

        //total time of projectile
        totalTime = 2 * VInit / gravOfPlanet;

        //how far projectile will travel in x direction
        totalX = getpx() + .5 * VInit * Math.cos(getAngle()) * 2 * VInit * Math.cos(getAngle()) / gravOfPlanet;

        px = (int) (px - dx * dt);

        if(getTime() <= totalTime/225)
            py = (int) (py + dvy * dt - 0.5 * gravOfPlanet * (dt * dt));
        else
        {
            dt = 0.5;
            py = (int) (py - dvy * dt - 0.5 * gravOfPlanet * (dt * dt));
            dt = 0.09;
        }
        //sets draw to false when the ball goes outside the left or bottom of the screen
        if (px < minx || py > ymax)
        {
            setdraw(false);
        }

    }

    @Override
    public void paint(Graphics cg)
    {
        Rectangle Cball = new Rectangle(px, py, SObj, SObj);
        Rectangle BallB = new Rectangle(x, y, SObj, SObj);
        //create buffer
        buffer = createImage(ScreenWidth, ScreenHeight);
        if (g != null)
        {
            g.dispose();
        }
        g = buffer.getGraphics();


        //go through vector and draw rectangles
        for (int i = 0; i < Walls.size(); i++)
        {
            Rectangle potential = Walls.elementAt(i);
            g.fillRect(potential.x, potential.y, potential.width, potential.height);
        }

        //set border color and draw it
        g.setColor(Color.blue);
        g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1);

        //set ball color and draw it
        g.setColor(Color.red);
        g.fillOval(getx(), gety(), SObj, SObj);
        g.setColor(Color.black);
        g.drawOval(getx(), gety(), SObj, SObj);

        //draw any potential rectangles (dragbox) if mouse is dragged
        g.drawRect(potential.x, potential.y, potential.width, potential.height);

        //making the points for our barrel


        //creating the barrel
        Polygon poly = new Polygon();
        poly.reset();

        //set X and Y positions to cosine and sine times the angle, then multiply by 80
        refX = (int)(80*Math.cos(angle));
        refY = (int)(80*Math.sin(angle));

        //set bx and by positions by multiplying 10 times the cosine and sine of the angle
        bx = (int)(10*Math.cos(angle));
        by = (int)(10*Math.sin(angle));

        //set points a1 and a2
        a1.x = (ScreenWidth-25) - by;
        a1.y = (ScreenHeight-25) + bx;
        a2.x = (ScreenWidth-25) + by;
        a2.y = (ScreenHeight-25) - bx;

        //set points c1 and c2
        c1.x = (ScreenWidth-25 - refX) - by;
        c1.y = (ScreenHeight-25 - refY) + bx;
        c2.x = (ScreenWidth-25 - refX) + by;
        c2.y = (ScreenHeight-25 - refY) - bx;

        //add the points to the polygon
        poly.addPoint(a1.x, a1.y);
        poly.addPoint(a2.x, a2.y);
        poly.addPoint(c2.x, c2.y);
        poly.addPoint(c1.x, c1.y);


        //set the color of the polygon and draw and fill it
        g.setColor(Color.cyan);
        g.drawPolygon(poly);
        g.fillPolygon(poly);

        //set the color of the base and fill
        g.setColor(Color.green);
        g.fillOval(ScreenWidth - 45, ScreenHeight - 45, 60, 60);

        if (drawP == false)
        {
            //if drawP is false, call SetupC function
            setUpC();
        }
        //checks the intersections of the canon with the ball
        if(poly.intersects(BallB)) {
            score++;
            setx(0);
            sety(0);
        }
        //checks the intersections of the canon ball with the ball
        if(BallB.intersects(Cball)) {
            pscore++;
            setx(0);
            sety(0);
        }

        if (drawP)
        {
            //draws the cannonball if drawP is true
            g.setColor(Color.BLUE);
            g.fillOval(px, py, 21, 21);
        }

        cg.drawImage(buffer, 0, 0, null);   //draw image
    }
    //gets the computer score
    public static int getScore() {
        return score;
    }
    //sets the computer score back to zero
    public void setScore() {
        score = 0;
    }
    //gets the player score
    public static int getPScore() {
        return pscore;
    }
    //sets the player score back to zero
    public void setPscore() {
        pscore = 0;
    }

    //clears the walls vector
    public void Clear() {
        Walls.clear();
    }

    //update function
    public void update(Graphics g)
    {
        paint(g);
    }

    //function to set the velocity of the cannonball
    public void setVelocity(int ts) {
        VInit = ts;
    }
}
