
import java.awt.*;
        import java.awt.event.*;
        import java.io.*;
        import java.util.Vector;


public class Main extends Frame implements MouseListener, MouseMotionListener, WindowListener, ComponentListener,ActionListener,AdjustmentListener,Runnable
{

    private static final long serialVersionUID = 10L;

    private Point m1 = new Point(0,0);// initial mouse position
    private Point m2 = new Point(0,0);// current mouse position

    private int ScreenWidth; // drawing screen width
    private int ScreenHeight; // drawing screen height

    private Rectangle Perimeter = new Rectangle(0,0,ScreenWidth,ScreenHeight);// bouncing perimeter
    private Rectangle ObjBoundary = new Rectangle();// create Rectangle ObjBoundary
    private Rectangle r = new Rectangle();// create Rectangle r
    private Rectangle t = new Rectangle();// create Rectangle t
    private Rectangle db = new Rectangle(); // create Rectangle db

    private final int WIDTH = 640; // initial frame width
    private final int HEIGHT = 400; // initial frame height
    private final int BUTTONH = 20; // button height
    private final int BUTTONHS = 5; // button height spacing
    private int WinWidth = WIDTH; // frame width
    private int WinHeight = HEIGHT; // frame height
    private int WinTop = 10; // top of frame
    private int WinLeft = 10; // left side of frame
    private int BUTTONW = 50; // button width
    private int CENTER = (WIDTH/2); // scrren center

    private final int MAXObj = 100; //max object size
    private final int MINObj =10; // min object size
    private final int SPEED =50; // initial speed
    private final int SBvisible = 10; //visible scroll bar
    private final int SBunit =1; //scroll bar unit step size
    private final int SBblock=10; // scroll bar block step size
    private final int SCROLLBARH = BUTTONH; //scroll bar height
    private final int SOBJ =21; // initial object width
    private int SObj=SOBJ; //initial object width
    private int SpeedSBmin =1; // speed scrollbar minimum value
    private int SpeedSBmax = 100+SBvisible; // speed scrollbar minimum value with visible offset
    private int SpeedSBinit = SPEED; // speed scrollbar value
    private int ScrollBarW; // scrollbar width

    private long delay = SpeedSBmax - SPEED ; //makes the delay

    private Objc Obj; // object to draw
    private Label SPEEDL = new Label("Speed",Label.CENTER); // label for scrollbar
    private Label SIZEL = new Label("Size",Label.CENTER); // label for scrollbar
    Scrollbar SpeedScrollBar, ObjSizeScrollBar; // scroll bars

    private Insets I; // insets of frame
    private Thread theThread; // makes the thread theThread
    boolean run = true; // makes boolean run
    boolean TimeIsPaused = false; // makes boolean TimeIsPaused
    boolean started = false; // makes boolean started
    boolean ok;// makes boolean ok

    private Panel sheet = new Panel(); // makes panel sheet
    private Panel control = new Panel(); // makes panel control

    Button Start, Stop, Quit; // buttons

    public Main() // Bounce Constructor
    {
        setLayout(new BorderLayout()); // BorderLayout layout frame
        setVisible(true); // makes it visible
        MakeSheet(); // determines the size of the sheet
        try
        {
            initComponents(); // initializes the components

        }

        catch(Exception e) {e.printStackTrace();}

        SizeScreen(); // size of components on screen
        start(); // begins the thread
    }//end bounce

    public static void main(String[] args)

    {
        new Main(); //Creates a new Bounce object
    }


    public void initComponents() throws Exception, IOException // initComponents method

    {
        this.setBounds(Perimeter); //sets the bounds of the perimeter
        this.setVisible(true); // makes it visible

        double colWeight[] = {1,1,1,1,1,1,1,1,1,1,1,1,1};              //sets the weight of the gridbag col
        double rowWeight[] = {1,1,1};        //sets the weight of the gridbag row

        int colWidth[]= {1,1,1,1,1,1,1,1,1,1,1,1,1}; //sets the size of the gridbag col
        int rowHeight[]= {1,1,1}; //sets the size of the gridbag row


        GridBagConstraints gbc = new GridBagConstraints(); // makes new GridBagConstraints
        GridBagLayout gbl = new GridBagLayout(); // makes new GridBagLayout
        BorderLayout bl = new BorderLayout(); // makes new BorderLayout

        gbl.rowHeights = rowHeight;
        gbl.columnWidths = colWidth;  // displays rowHeight, columnWidths, columnWeights, rowWeights
        gbl.columnWeights = colWeight;
        gbl.rowWeights = rowWeight;

        Start = new Button("Run"); // makes the buttons and sets them
        Stop = new Button("Pause");
        Quit = new Button("Quit");
        Start.setEnabled(false);
        Stop.setEnabled(true);

        SpeedScrollBar= new Scrollbar(Scrollbar.HORIZONTAL); // creates scrollbar
        SpeedScrollBar.setMaximum(SpeedSBmax); // sets max speed
        SpeedScrollBar.setMinimum(SpeedSBmin); // sets min speed
        SpeedScrollBar.setUnitIncrement(SBunit); // sets the unit increment
        SpeedScrollBar.setBlockIncrement(SBblock); // sets the block increment
        SpeedScrollBar.setValue(SpeedSBinit); // sets initial value
        SpeedScrollBar.setVisibleAmount(SBvisible); // sets the visible size
        SpeedScrollBar.setBackground(Color.gray); // sets the background color

        ObjSizeScrollBar = new Scrollbar(Scrollbar.HORIZONTAL); // creates scrollbar
        ObjSizeScrollBar.setMaximum(MAXObj); // sets max speed
        ObjSizeScrollBar.setMinimum(MINObj); // sets min speed
        ObjSizeScrollBar.setUnitIncrement(SBunit); // sets the unit increment
        ObjSizeScrollBar.setBlockIncrement(SBblock); // sets the block increment
        ObjSizeScrollBar.setValue(SOBJ);  // sets initial value
        ObjSizeScrollBar.setVisibleAmount(SBvisible); // sets the visible size
        ObjSizeScrollBar.setBackground(Color.gray); // sets the background color
        Obj = new Objc(SObj, ScreenWidth, ScreenHeight); // creates the object
        Obj.setBackground(Color.white); // sets the background color

        add(SpeedScrollBar); // adds scrollbar to frame
        add(ObjSizeScrollBar); // adds scrollbar to frame
        add(SPEEDL); //adds label to the frame
        add(SIZEL); //adds label to the frame
        add(Obj); // adds object to the frame
        SpeedScrollBar.addAdjustmentListener(this); //adds the adjustment listener
        ObjSizeScrollBar.addAdjustmentListener(this); //adds the adjustment listener*/

        m1.setLocation(10,10); //sets the m1 point location
        m2.setLocation(0,0); //sets the m2 point location
        Perimeter.setBounds(Obj.getx(), Obj.gety(),ScreenWidth,ScreenHeight); // sets the bounds of the perimeter
        Perimeter.grow(-1, -1); //grows the perimeter by 1
        setLayout(new BorderLayout()); // sets the border layout
        setBounds(WinLeft, WinTop, WIDTH, HEIGHT); // sets the borderlayout's bounds
        setBackground(Color.lightGray); // sets the color
        setVisible(true);// makes it visible


        control.setLayout(gbl); // sets controls layout to the gridbag

        gbc.weightx = 1; //sets the x weight
        gbc.weighty = 1; //sets the y weight
        gbc.gridwidth =1; //sets the size of the button
        gbc.gridheight =1; //sets the size of the button
        gbc.gridx=4; //puts the button at spot 4 on gridx and 1 on gridy
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(Start, gbc); // displays the button
        Start.setVisible(true); // sets it visible
        control.add(Start); //adds the button to control

        gbc.weightx = 1; //sets the x weight
        gbc.weighty = 1; //sets the y weight
        gbc.gridwidth =1; //sets the size of the button
        gbc.gridheight =1; //sets the size of the button
        gbc.gridx=5; //puts the target button at spot 5 on gridx and 1 on gridy
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(Stop, gbc); // displays the button
        Stop.setVisible(true); // sets it visible
        control.add(Stop); //adds the button to control

        gbc.weightx = 1; //sets the x weight
        gbc.weighty = 1; //sets the y weight
        gbc.gridwidth =1; //sets the size of the button
        gbc.gridheight =1; //sets the size of the button
        gbc.gridx=6; //puts the target button at spot 6 on gridx and 1 on gridy
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(Quit, gbc); // displays the button
        Quit.setVisible(true);     // sets it visible
        control.add(Quit); //adds the button to control

        gbc.weightx = 1; //sets the x weight
        gbc.weighty = 1; //sets the y weight
        gbc.gridwidth =1; //sets the size of the scrollbar
        gbc.gridheight =1; //sets the size of the scrollbar
        gbc.gridx=2; //puts the target button at spot 2 on gridx and 1 on gridy
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(SpeedScrollBar, gbc); // displays the scrollbar
        SpeedScrollBar.setVisible(true); // sets it visible
        control.add(SpeedScrollBar); //adds the Scrollbar to control

        gbc.weightx = 1; //sets the x weight
        gbc.weighty = 1; //sets the y weight
        gbc.gridwidth =1; //sets the size of the label
        gbc.gridheight =1; //sets the size of the label
        gbc.gridx=2; //puts the target button at spot 2 on gridx and 0 on gridy
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(SPEEDL, gbc); // displays the label
        SPEEDL.setVisible(true); // sets it visible
        control.add(SPEEDL); //adds the label to control

        gbc.weightx = 1;//sets the x weight
        gbc.weighty = 1;//sets the y weight
        gbc.gridwidth =1; //sets the size of the scrollbar
        gbc.gridheight =1;
        gbc.gridx=8; //puts the target button at spot 8 on gridx and 1 on gridy
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(ObjSizeScrollBar, gbc); // displays the scrollbar
        ObjSizeScrollBar.setVisible(true); // sets it visible
        control.add(ObjSizeScrollBar); //adds the Scrollbar to control

        gbc.weightx = 1;//sets the x weight
        gbc.weighty = 1;//sets the y weight
        gbc.gridwidth =1; //sets the size of the label
        gbc.gridheight =1;
        gbc.gridx=8; //puts the target button at spot 0 on gridx and 5 on gridy
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(SIZEL, gbc); // displays the label
        SIZEL.setVisible(true); // sets it visible
        control.add(SIZEL); // adds label to control

        control.setBackground(Color.lightGray); //sets the background color
        control.setSize(WIDTH, 2*BUTTONH);
        control.setVisible(true);

        sheet.setLayout(new BorderLayout(0,0)); // sheet boarder layout
        Obj = new Objc(SObj, ScreenWidth, ScreenHeight);// makes new Objc
        Obj.setBackground(Color.white); // sets the color
        sheet.add("Center", Obj); // adds the center to the sheet
        sheet.setVisible(true); // sets it visible

        add("Center",sheet); // adds center to sheet
        add("South",control); // adds south to control
        validate();
        Obj.addMouseMotionListener(this); //adds MouseMotionListener
        Obj.addMouseListener(this); // adds MouseListener
        Obj.addComponentListener(this); //adds ComponentListener
        this.addWindowListener(this); //adds WindowListener
        this.addComponentListener(this); // adds ComponentListener

        Start.addActionListener(this); //adds button to the action listener
        Stop.addActionListener(this); //adds button to the action listener
        Quit.addActionListener(this); //adds button to the action listener
        SpeedScrollBar.addAdjustmentListener(this); //adds Scrollbar to the action listener
        ObjSizeScrollBar.addAdjustmentListener(this); //adds Scrollbar to the action listener

    }

    private void MakeSheet() // gets the insets and adjusts the sizes
    {
        I=getInsets();

        ScreenWidth = WinWidth -I.left - I.right; //sets screenwidth
        ScreenHeight = WinHeight-I.top-2*(BUTTONH + BUTTONHS)-I.bottom; //sets screenheight
        CENTER = (ScreenWidth/2); // determines the center of the screen
        setSize(WinWidth, WinHeight); // sets the frame size



    }

    private void SizeScreen()
    {
        Obj.setBounds(0,0,ScreenWidth,ScreenHeight); //sets the screen size
    }

    public void run() //run function
    {
        theThread.setPriority(Thread.MAX_PRIORITY); // sets thread to max priority

        while(run == true) // starts a loop for to see if it is running
        {
            if(TimeIsPaused == false)//checks to see if it is not paused
            {
                started = true;


                try
                {
                    theThread.sleep(delay); //sleeps the thread for the delay time
                }

                catch (InterruptedException e) {}


                Obj.repaint(); // repaints the frame
                Obj.move();//calls move function



            }

            try
            {
                Thread.sleep(1); // sleeps the thread
            }

            catch(InterruptedException e) {}

        }

    }

    public void start() // start method
    {

        if(theThread == null) // checks if the thread is empty
        {
            theThread = new Thread(this); // creates new thread
            theThread.start(); // starts the thread
            Obj.repaint(); // repaints the frame
        }
    }


    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource(); // checks for which button is pressed

        if(source==Start) // start button
        {
            try {
                Thread.sleep(1); // sleeps the thread
            } catch (InterruptedException e1) {e1.printStackTrace();}

            Start.setEnabled(false);
            Stop.setEnabled(true);
            TimeIsPaused = false;

            theThread.interrupt();
        }


        if(source==Stop) // stop button
        {
            try {
                Thread.sleep(1); // sleeps the thread
            } catch (InterruptedException e1) {e1.printStackTrace();}

            Start.setEnabled(true);
            Stop.setEnabled(false);
            TimeIsPaused = true;

            theThread.interrupt();
        }

        if (source == Quit) // quit button
        {
            stop(); // stops the program
        }

    }// action performed


    public void adjustmentValueChanged(AdjustmentEvent e)
    {

        int TS;
        int half;
        int i=0;


        Scrollbar sb = (Scrollbar)e.getSource(); // gets the scroll bar that triggered the event

        if(sb==SpeedScrollBar) // speed scollbar

        {
            TS=e.getValue();  //gets the scrollbar value
            delay = (SpeedSBmax- TS)/2; //determines the delay
            theThread.interrupt(); // interupts the thread
        }

        if(sb==ObjSizeScrollBar) // size scroll bar
        {

            Obj.MinMax();//calls MinMax method
            TS=e.getValue(); // gets the value
            TS=(TS/2)*2+1; // makes odd to account for center position
            half = (TS-1)/2; // sets half to half the size of the ball
            ObjBoundary.setBounds(Obj.getx()-half-1,Obj.gety()-half-1,TS+2,TS+2); //sets the size of the boundary arounf the ball
            sb.setValue(Obj.getsize()); // sets the scrollbar value


            if(ObjBoundary.equals(Perimeter.intersection(ObjBoundary)))// if ball intersects with outside of the frame
            {
                i=0;//
                ok=true;//

                while((i < Obj.getWallSize()) && ok)// loop to look through the rectangle vector

                {
                    t = Obj.getOne(i);// get the ith rectankgle

                    if(t.intersects(ObjBoundary)) //checks for intersection with ObjBoundary

                    {
                        ok = false; // sets okay to false
                        sb.setValue(Obj.getsize()); // sets the scrollbar value
                    }
                    i++;//increments i

                }// end while

                Obj.CheckSizeX(TS);//calls CheckSizeX method
                Obj.CheckSizeY(TS);//calls CheckSizeY method

                if(Obj.CheckSizeX(TS) == true && Obj.CheckSizeY(TS) == true && ok == true) // prevents SObj from being increased if it will be outside of xmin, xmax, ymin, or ymax

                {
                    Obj.update(TS); // change the size in the drawing object
                    Obj.MinMax(); //calls MinMax method
                }

                else
                {
                    sb.setValue(Obj.getsize()); // sets the scrollbar value
                }

            }//end if
        }

        Obj.repaint(); // force a repaint
    }


    public void componentResized(ComponentEvent e) //12:09
    {

        WinWidth = getWidth(); // makes WinWidth = getWidth
        WinHeight = getHeight();//  makes WinHeight = getHeight
        MakeSheet(); // calls makesheet
        SizeScreen(); // calls sizescreen
        CheckSize(); // calls checksize
        Obj.reSize(ScreenWidth,ScreenHeight); // resizes the screen
        Perimeter.setBounds(Obj.getx(), Obj.gety(), ScreenWidth, ScreenHeight); // resizes the perimeter
        Perimeter.grow(-1,-1); // grow the perimeter by 1
        Obj.reSize(ScreenWidth,ScreenHeight); // resizes the screen
        Obj.repaint(); // repaints the frame

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
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

    public void CheckSize() // checksize method

    {
        int x = Obj.getx(); // creates x int
        int y = Obj.gety(); // creates y int
        int size = Obj.getsize(); // creates size int

        int bottom = y + (size -1)/2;	 //creates bottom int
        int right = x + (size -1)/2; // creates right int

        if(right > ScreenWidth); // checks if right is greater than ScreenWidth
        {
            Obj.setx(ScreenWidth - size - 1);	 // sets the x
        }

        if(bottom > ScreenHeight) // checks if bottom is greater than ScreenHeight

        {
            Obj.sety(ScreenHeight - size -1); // sets the y
        }

    }


    public void stop() // stop method

    {
        run = false; // stops the running of the program
        theThread.interrupt(); // interupts the thread

        SpeedScrollBar.removeAdjustmentListener(this); // removes all the lsteners
        ObjSizeScrollBar.removeAdjustmentListener(this);

        Start.removeActionListener(this); // removes the ActionListener
        Stop.removeActionListener(this);// removes the ActionListener
        Quit.removeActionListener(this);// removes the ActionListener
        this.removeComponentListener(this);// removes the ComponentListener
        this.removeWindowListener(this); // // removes the WindowListener
        dispose();
        System.exit(0);
    }

    private Rectangle getDragBox(MouseEvent e)

    {
        m2.setLocation(e.getPoint()); //sets the point m2 for the dragbox
        Rectangle MathRec = new Rectangle(); // makes a rectangle

        MathRec.setBounds(Math.min(m2.x,m1.x), Math.min(m1.y,m2.y), Math.max(m2.x - m1.x, m1.x - m2.x), Math.max(m1.y - m2.y, m2.y - m1.y)); // calculates which way to draw the dragbox

        return MathRec;
    }

    public void mousePressed(MouseEvent e)
    {
        m1.setLocation(e.getPoint()); 	//sets the point m1 for the dragbox
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        db.setBounds(getDragBox(e));   //makes db = dragbox

        if(Perimeter.contains(db)) // if db is in the perimeter

        {

            Obj.setDragBox(db); // sets the dragbox
            Obj.repaint(); // paints the dragbox

        }

    }



    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        ok = true;
        int i =0;

        Point p = new Point(e.getPoint()); //makes the point p

        while((i < Obj.getWallSize()) && ok) //loops throught the rectangle vector
        {

            r = Obj.getOne(i); // sets r = to getOne

            if(r.contains(p)) // if the rectangle is clicked remove that rectangle
            {
                Obj.removeOne(i); //removes a rectangle from the vector

            }

            else

            {
                i++; // increments i
            }


        }// end while



    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        ok = true;
        int i=0;
        int half = (Obj.getsize()-1)/2; //sets half
        Rectangle ObjBoundary = new Rectangle(Obj.getx()-half,Obj.gety()-half,Obj.getsize(),Obj.getsize()); // sets the boundary around the ball

        Rectangle getOne = new Rectangle(); //makes new rectangle getOne

        if(db.intersects(ObjBoundary))// checks if ball is in the drawbox

        {
            ok = false; // sets ok to false
            db.setBounds(0,0,0,0); // set the db bounds to 0
            Obj.setDragBox(db);
            Obj.repaint(); // repaint the db
        }

        while((i < Obj.getWallSize()) && ok) // loops through therectangle vector
        {

            getOne = Obj.getOne(i); // sets getOne to Obj.getOne

            if(db.intersection(getOne).equals(db))//if a smaller rectangle is put on a bigger rectangle delete the smaller
            {
                ok = false;
                db.setBounds(0,0,0,0); // set the db bounds to 0
                Obj.setDragBox(db);
                Obj.repaint(); // repaint the db
            }

            if(getOne.intersection(db).equals(getOne))//if a rectangle is covered by another delete the botttom rectangle
            {
                Obj.removeOne(i);	// remove rectangle
            }

            else
            {
                i++; // increment i
            }

        }// end while

        if(ok) // if ok is true

        {
            Obj.addOne(db); //add a rectangle to the vector

            db.setBounds(0,0,0,0); // set the db bounds to 0
            Obj.setDragBox(db);
            Obj.repaint(); // repaint the db


        }

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        Obj.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {


    }



}// end class

class Objc extends Canvas // Objc class
{
    private static final long serialVersionUID = 11L;

    private int ScreenWidth; // makes the the following ints and booleans
    private int ScreenHeight;
    private int SObj;
    private int x,y;
    private int dx;
    private int dy;
    private int xmin, xmax, ymin,ymax;
    private boolean rect = true;
    private boolean clear = false;
    private boolean down = false;
    private boolean right = false;
    private boolean ok;

    Rectangle MathRec = new Rectangle(); //makes rectangle MathRec

    Image buffer; // makes an Image
    Graphics g; // makes a graphic

    private Vector <Rectangle> Walls = new Vector <Rectangle>(); // makes a vector

    public void addOne(Rectangle r) // adds a rectangle to the vector

    {
        Walls.addElement(new Rectangle(r));

    }

    public void removeOne(int i) // removes a rectangle from the vector

    {
        Walls.removeElementAt(i);

    }

    public Rectangle getOne(int i) // gets a rectangle from the vector

    {
        return Walls.elementAt(i);
    }

    public int getWallSize() // returns the size of the vector Walls which store the rectangles

    {
        return Walls.size();
    }

	/*public Rectangle getBall()

	{
		return new Rectangle();
	}*/


    private static final Rectangle ZERO = new Rectangle(0,0,0,0); //makes a zero rectangle
    {

        Rectangle r = new Rectangle(ZERO); //makes new rectangle zero
        Rectangle ObjBoundary = new Rectangle(x+1,y+1,SObj,SObj); // sets the boundary around the ball
        ObjBoundary.grow(1,1); // grows the boundary
        int i =0; // sets i

        while((i<Walls.size()) && ok)// loops throught the rectangle vector

        {
            r=Walls.elementAt(i); // sets r = to the element in the vector

            if(r.intersects(ObjBoundary)) // if the rectangle intersects the ball
            {
                ok = false;// sets ok to false
            }

            else
            {
                i++; // increments i
            }
        }

    }


    public Objc(int SB, int w, int h) // constructor
    {
        ScreenWidth=w; // assigns values the the following
        ScreenHeight=h;
        SObj = SB;
        rect = true;
        clear = false;
        y= ymin+1;
        x= xmin+1;

        dx = 1;
        dy = 1;


        down = true;
        right = true;

        MinMax(); // calls MinMax

    }

    public void setDragBox(Rectangle rec) // sets the dragbox size

    {
        MathRec.setBounds(rec.x,rec.y,rec.width,rec.height);

    }


    public int getx() // returns the x
    {


        return this.x;

    }

    public int gety() // returns the y
    {


        return this.y;

    }

    public int getsize() //gets the size
    {
        return this.SObj;

    }

    public void setx(int x) // sets the x

    {
        this.x = x;
    }

    public void sety(int y) // sets the y

    {
        this.y = y;
    }




    public void MinMax() // MinMax method

    {
        xmin = (SObj + 1)/2 +2; // assigns values for xmin, xmax, ymin, and ymax
        xmax = (ScreenWidth - xmin);
        ymin = (SObj + 1)/2 +2;
        ymax = (ScreenHeight - ymin);
    }

    public boolean CheckSizeX(int size) // checkSizeX method
    {
        if(x +(size + 1)/2  < xmax && x - (size + 1)/2  > xmin) // checks if x + SObj is inside xmax and xmin
        {
            return true;
        }

        return false;
    }

    public boolean CheckSizeY(int size) // checkSizey method
    {
        if(y + (size + 1)/2 < ymax && y - (size + 1)/2 > ymin) // checks if y + SObj is inside xmax and xmin
        {
            return true;
        }

        return false;
    }



    public boolean CheckX() // checkX method
    {
        if(x < xmax && x > xmin) // checks if x is inside xmax and xmin
        {
            return true;
        }

        return false;
    }


    public boolean CheckY() //checky method

    {
        if(y < ymax && y > ymin) // checks if y is inside the ymax and ymin
        {
            return true;
        }

        return false;
    }




    public void BounceRec() // bounces the ball off the rectangles
    {

        int i =0; //sets i
        int half = (SObj-1)/2;// sets half
        Rectangle ObjBoundary = new Rectangle(x-half,y-half,SObj,SObj); // sets the boundary for the ball



        for(i=0;i <  Walls.size(); i++) //loops throgh the rectangle vector

        {

            Rectangle Wall = Walls.elementAt(i); // sets wall rectanglec = to the rectangle in the vector

            int ball_left = ObjBoundary.x; // makes ball left
            int ball_right = ball_left + SObj; // makes ball right
            int ball_top = ObjBoundary.y; // makes ball top
            int ball_bottom = ObjBoundary.y+ SObj; // makes ball bottom

            int wall_left = Wall.x; // makes wall left
            int wall_right = wall_left+Wall.width; // makes wall right
            int wall_top = Wall.y; // makes wall top
            int wall_bottom = wall_top+Wall.height; // makes wall bottom

            boolean BallOnLeft    = (ball_right   <=  wall_left+1); // checks if the ball is on the left side of the rectangle
            boolean BallOnRight   = (ball_left    >=  wall_right-1); // checks if the ball is on the right side of the rectangle
            boolean BallIsAbove   = (ball_bottom  <=  wall_top+1); // checks if the ball is on top of the rectangle
            boolean BallIsBelow   = (ball_top     >=  wall_bottom-1); // checks if the ball is below the rectangle

            if(ObjBoundary.intersects(Wall))  // checks if the objboundary intersects the rectangle
            {

                if (BallOnLeft == true) // bounces the ball left
                {
                    MinMax(); //calls minmax method

                    dx = dx*-1; // moves the object

                    x = dx+x; // moves the object
                }

                if(BallOnRight == true) // bounces the ball right

                {
                    MinMax(); //calls minmax method

                    dx = dx*-1; // moves the object

                    x = dx+x; // moves the object
                }

                if (BallIsAbove == true) // bounces the ball up
                {
                    MinMax(); //calls minmax method

                    dy =dy *-1; // moves the object

                    y = dy + y; // moves the object
                }

                if (BallIsBelow == true) // bounces the ball down
                {
                    MinMax(); //calls minmax method

                    dy =dy *-1; // moves the object

                    y = dy + y; // moves the object


                }

            }//end if

        }//end for

    }



    public void move() // move method

    {

        BounceRec();


        if(CheckX() == true) //checks if checkx is true
        {
            right =  true;  // assigns true to right
        }

        else
        {
            right = false; // assigns false to right
        }


        if(CheckY() == true) // checks if Checky is true
        {
            down = true; //assigns true to down
        }

        else
        {
            down = false;// assigns false to down
        }


        if(right == true) // checks if right is true moving to the right
        {
            MinMax(); //calls minmax method
            x+=dx; // moves the object

        }

        else
        {
            MinMax(); //calls minmax method

            dx = dx*-1; // moves the object

            x = dx+x; // moves the object

        }

        if(down == true)
        {
            MinMax(); //calls minmax method

            y+=dy; // moves the object
        }

        else

        {
            MinMax(); //calls minmax method

            dy =dy *-1; // moves the object

            y = dy + y; // moves the object

        }


    }


    public void rectangle(boolean r)
    {
        rect =r; //sets rect to r
    }

    public void update(int NS)
    {
        SObj=NS; //sets SObj to NS
    }

    public void reSize(int w, int h)
    {
        ScreenWidth = w; //sets ScreenWidth to w
        ScreenHeight =h; // sets ScreenHeight to h
        y= ymin+1; // sets y = to a new value
        x= xmin+1; // sets x = to a new value
        MinMax(); // calls minmax method

    }

    public void Clear() //clear method
    {
        clear=true; // sets clear to true
    }

    public void paint(Graphics cg)
    {
        if(g != null) // if graphics isnt empty
        {
            g.dispose();
        }

        buffer= createImage (ScreenWidth, ScreenHeight); // buffers the image

        g= buffer.getGraphics();


        for(int i=0; i<Walls.size(); i++)    // loops throught the rectangle vector

        {
            Rectangle MathRec = Walls.elementAt(i); // makes MathRec equal to rectangle in vector

            g.fillRect(MathRec.x,MathRec.y,MathRec.width,MathRec.height); // fills the rectangles with black


        }

        g.setColor(Color.red); // crates red outline on frame
        g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1); // crates red outline on frame

        g.setColor(Color.red); // draws the object and its outline
        g.fillOval(x-(SObj-1)/2,y-(SObj-1)/2,SObj,SObj);
        g.setColor(Color.black); // draws the object and its outline
        g.drawOval(x-(SObj-1)/2,y-(SObj-1)/2,SObj,SObj);

        g.drawRect(MathRec.x,MathRec.y, MathRec.width, MathRec.height);

        cg.drawImage(buffer,0,0,null);

    }

    public void update(Graphics g)
    {

        paint(g);

    }

}//end Objc class

