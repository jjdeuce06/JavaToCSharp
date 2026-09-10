/*
Group 2 - Bouncing Ball Program
John Gerega (ger9822@pennwest.edu
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu)
This is an extension of the Bounce Program, in this program, a border layout and a grid bag layout is used
The grid bag layout contains all buttons and scrollbars for this program
The border layout is where the object (a ball) bounces
This program implemenets the use of Vectors, Rectangles, Points, and mouse events
When the mouse is clicked and dragged, a drag box will appear,
if that drag box does not contain the ball, it is filled in and added to the vector when the mouse is released
When the mouse is clicked and is within a rectangle, that rectangle is deleted
Ball bounces accordingly based on perimeter and rectangles
Ball resizes accordingly based on perimeter and rectangles
A thread is used to run the program
*/




import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;


public class BouncingBall extends Frame implements WindowListener, ComponentListener, MouseListener, MouseMotionListener, ActionListener, AdjustmentListener, Runnable
{

    public static void main(String[] args) {
        BouncingBall b = new BouncingBall();
    }
    //taken from bounce
    private static final long serialVersionUID = 10L;

    private final int WIDTH = 640;  //initial frame width
    private final int HEIGHT = 400;  //initial frame height
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

    private final int MAXObj = 100; //maximum object size
    private final int MINObj = 10;  //minimum object size
    private final int SPEED = 50;   //initial speed
    private final int SBvisible = 10;   //visible scroll bar
    private final int SBunit = 1;    //Scroll bar unit step size
    private final int SBblock = 10;  //Scroll bar block step size
    private final int SCROLLBARH = BUTTONH; //scrollbar height
    private final int SOBJ = 21;    //initial object width, need odd value to have even distribution on top, right, left, and bottom

    private int ball = SOBJ;        //initial object width
    private int SpeedSBmin = 1; //speed scrollbar minimum value
    private int SpeedSBmax = 100+SBvisible;  //speed scrollbar maximum value with visible offset
    private int SpeedSBinit = SPEED;    //initial speed scrollbar value
    private int ScrollBarW; //scrollbar width
    private int delay = 4;      //initial delay speed
    private Thread thethread; //thread for timer delay

    //added for bouncing ball
    private Point m1 = new Point(0, 0); //starting mouse position
    private Point m2 = new Point(0, 0); //updating mouse position
    private Point Screen = new Point(WIDTH-1, HEIGHT-1);    //make Point named screen
    private Rectangle Perimeter = new Rectangle(0, 0, ScreenWidth, ScreenHeight);   //make perimeter rectangle
    private Rectangle db = new Rectangle();     //make db rectangle for dragboxes

    private static final Rectangle ZERO = new Rectangle(0, 0, 0, 0);
    //private Rectangle r = new Rectangle(ZERO);
    //private Rectangle b = new Rectangle(Ball);




    private Ballc Ball;       //object to draw
    private Label SPEEDL = new Label("Speed", Label.CENTER);        //label for speed scroll bar
    private Label SIZEL = new Label("Size", Label.CENTER);  //label for scroll size bar
    Scrollbar SpeedScrollBar, ObjSizeScrollBar; //scrollbars

    private Insets I;       //insets of frame

    Button Start, Pause, Quit;     //Buttons
    private boolean runBall = true, Timepause = false, start = false, good;       //flags for running object, timepause, and start
    private Panel sheet = new Panel();      //make new panel named sheet
    private Panel control = new Panel();    //make new panel named control
    private GridBagConstraints c;       //create GBC object named c
    private GridBagLayout gbl;      //create GridBagLayout object named gbl
    public BouncingBall()
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

        SpeedScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);   //create speed scroll bar
        SpeedScrollBar.setMaximum(SpeedSBmax);      //set max speed
        SpeedScrollBar.setMinimum(SpeedSBmin);      //set minimum speed
        SpeedScrollBar.setUnitIncrement(SBunit);    //set the unit increment
        SpeedScrollBar.setBlockIncrement(SBblock);  //set the block increment
        SpeedScrollBar.setValue(SpeedSBinit);       //set the initial value
        SpeedScrollBar.setVisibleAmount(SBvisible); //set the visible size
        SpeedScrollBar.setBackground(Color.gray);   //set the background color

        ObjSizeScrollBar = new Scrollbar(Scrollbar.HORIZONTAL); //create size scroll bar
        ObjSizeScrollBar.setMaximum(MAXObj);                    //set max speed
        ObjSizeScrollBar.setMinimum(MINObj);                    //set minimum speed
        ObjSizeScrollBar.setUnitIncrement(SBunit);              //set unit increment
        ObjSizeScrollBar.setBlockIncrement(SBblock);            //set block increment
        ObjSizeScrollBar.setValue(SOBJ);                        //set initial value
        ObjSizeScrollBar.setVisibleAmount(SBvisible);           //set visible size
        ObjSizeScrollBar.setBackground(Color.gray);             //set background color
        Ball = new Ballc(SOBJ, ScreenWidth, ScreenHeight);        //create object
        Ball.setBackground(Color.white);

        //add scrollbars, labels, and Ball object
        add(SpeedScrollBar);        //add speed scroll bar to frame
        add(ObjSizeScrollBar);      //add size scroll bar to frame
        add(SPEEDL);                //add speed label to the frame
        add(SIZEL);                 //add size label to the frame
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

        //constraints for speed scrollbar
        c.weightx = 3;
        c.weighty = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(SpeedScrollBar, c);
        SpeedScrollBar.setVisible(true);
        control.add(SpeedScrollBar);

        //constraints for speedlabel
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        gbl.setConstraints(SPEEDL, c);
        SPEEDL.setVisible(true);
        control.add(SPEEDL);

        //constraints for start button
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(Start, c);
        Start.setVisible(true);
        Start.setSize(BUTTONW, BUTTONH);
        control.add(Start);

        //constraints for pause button
        c.weightx = 0;
        c.weighty = 1;
        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(Pause, c);
        Pause.setVisible(true);
        Pause.setSize(BUTTONW, BUTTONH);
        control.add(Pause);

        //constraints for quit button
        c.weightx = 0;
        c.weighty = 1;
        c.gridx = 6;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.CENTER;
        gbl.setConstraints(Quit, c);
        Quit.setVisible(true);
        Quit.setSize(BUTTONW, BUTTONH);
        control.add(Quit);

        //constraints for size scrollbar
        c.weightx = 3;
        c.weighty = 1;
        c.gridx = 8;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(ObjSizeScrollBar, c);
        ObjSizeScrollBar.setVisible(true);
        control.add(ObjSizeScrollBar);

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
        SpeedScrollBar.addAdjustmentListener(this);     //add speedscroll listener
        ObjSizeScrollBar.addAdjustmentListener(this);   //add speedscroll listener
        Ball.addMouseListener(this);
        Ball.addMouseMotionListener(this);
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
                Ball.repaint();      //repaint

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
        SpeedScrollBar.removeAdjustmentListener(this);
        ObjSizeScrollBar.removeAdjustmentListener(this);
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
    public void mouseClicked(MouseEvent e) {
            Point check = e.getPoint(); //get the point
            Rectangle deletecheck = new Rectangle();    //create new rectangle

            int i = 0;
            while (i < Ball.getWallSize())
            {
                deletecheck = Ball.getOne(i);   //set temp rectangle to rectangle at ith vector position
                if(deletecheck.contains(check))
                {
                    //if point clicked is within that rectangle, delete it
                    Ball.removeOne(i);
                }
                i++;    //increment i
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

        if (db.intersects(test))    //if db intersects the ball
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

        if (source == Start)
        {
            Start.setEnabled(false);    //disable Start, enable Pause, resume thread
            Pause.setEnabled(true);
            Timepause = false;
        }

        if (source == Pause)
        {
            Start.setEnabled(true);     //enabled start button, disable start, stop the thread
            Pause.setEnabled(false);
            Timepause = true;
        }
        if (source == Quit)
        {
            stop();                 //stop the program
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int TS;
        Scrollbar sb = (Scrollbar)e.getSource();    //get scrollbar that trigger event
        if(sb == SpeedScrollBar) {
            TS = e.getValue();
            delay = (ScrollBarW+SBvisible) / TS;    //set delay accordingly
            if(delay < 2) {
                delay = 2;
            }
            if(delay > 9) {
                delay = 9;
            }
        }

        if(sb == ObjSizeScrollBar) {
            if(sb == ObjSizeScrollBar)
            {
                //get scrollbar value, make it odd, and half the size
                TS = e.getValue();
                TS = (TS/2)*2 + 1;
                int half = (TS -1)/2;

                //make a new rectangle, keep ball's old size, and set bounds of rectangle
                Rectangle b = new Rectangle();
                int old = Ball.getOSize();
                int SBall;
                b.setBounds(Ball.getx()-half-1,Ball.gety()-half-1,TS+2, TS+2);
                //Intersection of perimeter with the ball will produce a rectangle?
                if(b.equals(Perimeter.intersection(b)))
                {
                    //if ball is within perimeter, check to make sure it is within rectangles too
                    int i = 0;
                    boolean ok = true;

                    while((i < Ball.getWallSize()) && ok)
                    {
                        //get rectangle from vector
                        Rectangle t = new Rectangle();
                        t = Ball.getOne(i);
                        if(t.intersects(b))
                        {//checks for intersection
                            //if intersection, set ok to false, and reset scrollbar
                            ok = false;
                            sb.setValue(old);
                        }
                        i++;    //increment i
                    }

                    if(Ball.getx() + (TS+1)/2 < Ball.findmaxX() && (Ball.getx() + (TS+1)/2 > Ball.findminx())) //check if the x and y remain in bounds with new value
                    {
                        if (Ball.gety() + (TS+1)/2 < Ball.findMaxY() && Ball.gety() + (TS+1)/2 > Ball.findMinY())
                        {
                            SBall = TS;
                            Ball.size(SBall);   //if so, resize the ball
                            Ball.repaint();
                        }
                        else
                        {
                            sb.setValue(old);   //if not, reset the object size and scrollbar position
                        }
                    }
                    else
                    {
                        sb.setValue(old);   //reset scrollbar value
                    }
                }

                //check if requested size will intersect with any of the stored rectangles
            }

            //check if requested size will intersect with any of the stored rectangles
            Ball.repaint();
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
}

class Ballc extends Canvas{
    private static final long serialversionUID = 11L;
    private int ScreenWidth;
    private int ScreenHeight;
    private int SObj;
    private int x, y, minx, miny, xmax, ymax, oldx, oldy;
    private boolean ok = true;
    private boolean down, right, good;
    Image buffer;   //image initialization
    Graphics g; //graphics initialization

    private Vector <Rectangle> Walls = new Vector<Rectangle>();     //vector for our rectangles
    private Rectangle potential = new Rectangle();      //potential made for dragbox

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

    @Override
    public void paint(Graphics cg)
    {
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



        cg.drawImage(buffer, 0, 0, null);   //draw image
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}