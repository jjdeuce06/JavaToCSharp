/*
Group 2 - Bounce Program
John Gerega (ger9822@pennwest.edu
Lance Ramsey(ram28736@pennwest.edu)
Clayton Sanner(san5024@pennwest.edu)
Purpose of this program is to use a null frame layout manager for the Frame, which contains an object where a circle/square will be animated
a speed scrollbar, a run/pause button, a tail/notail button, a clear button, a quit button, and a size scrollbar
Window listeners and component listeners will be used so window is resized, minimized, maximized and closed
Buttons will toggle the actions and labels of the buttons will change to identify the action taken if the button is pressed
Run/Pause - will run object or pause
Tail/No tail - will create tail or no tail
Clear - clears everything but the object
Quit - terminates program
change in size of animated object is restricted by position on screen and the borders of the animated screen
Object can not be made larger than the space available
scrollbar remains in sync with object size
Application implements Runnable and uses a thread with start and run methods
application moves animated object on a diagonal, whenever contact is made with boundary
*/


import java.awt.*;  //import statements
import java.awt.event.*;
import java.io.*;

public class Bounce extends Frame implements WindowListener, ComponentListener, ActionListener, AdjustmentListener, Runnable        //class declaration
{
    public static void main (String args[])
    {
        Bounce b = new Bounce();
    }       //main method calling Bounce class
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

    private int SObj = SOBJ;        //initial object width
    private int SpeedSBmin = 1; //speed scrollbar minimum value
    private int SpeedSBmax = 100+SBvisible;  //speed scrollbar maximum value with visible offset
    private int SpeedSBinit = SPEED;    //initial speed scrollbar value
    private int ScrollBarW; //scrollbar width
    private int delay = 4;      //initial delay speed

    private Thread thethread; //thread for timer delay



    private Objc Obj;       //object to draw
    private Label SPEEDL = new Label("Speed", Label.CENTER);        //label for speed scroll bar
    private Label SIZEL = new Label("Size", Label.CENTER);  //label for scroll size bar
    Scrollbar SpeedScrollBar, ObjSizeScrollBar; //scrollbars

    private Insets I;       //insets of frame

    Button Start, Shape, Clear, Tail, Quit;     //Buttons

    private boolean runobj, Timepause, start;       //flags for running object, timepause, and start

    public Bounce()
    {
        setLayout(null);        //use null layout of frame
        setVisible(true);        //make it visible

        MakeSheet();        //determine sizes for sheet
        try
        {
                initComponents();   //try to initialize components
        }
        catch (Exception e){e.printStackTrace();}
        start = false;      //set start flag to false
        SizeScreen();       //size items on the screen
        start();            //calls start method

    }

    private void MakeSheet()
    {
        I = getInsets();    //get insets
        ScreenWidth = WinWidth - I.left - I.right;  //make screen size the width of the frame less the left and right insets
        ScreenHeight = WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;   //make screen height the height of the frame less the top and bottom insets and space for two rows of buttons and two button spaces
        setSize(WinWidth, WinHeight);   //set frame size
        CENTER = (ScreenWidth/2);       //determine center of screen
        BUTTONW = ScreenWidth/11;       //determine width of buttons (11 units)
        BUTTONS = BUTTONW/4;            //determine button spacing
        setBackground(Color.LIGHT_GRAY);        //set background color
        ScrollBarW = 2*BUTTONW; //determine scrollbar width
    }
    public void initComponents() throws Exception, IOException
    {
        Timepause = true;       //set timepause to true, start program paused
        runobj = true;          //set runobj to true

        Start = new Button("Run");  //create start button
        Shape = new Button("Circle");    //create shape button
        Clear = new Button("Clear");     //create clear button
        Tail = new Button("No Tail");       //create tail button
        Quit = new Button("Quit");      //create quit button

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
        Obj = new Objc(SOBJ, ScreenWidth, ScreenHeight);        //create object
        Obj.setBackground(Color.white);                         //set background color

        add("Center", Start);       //add Start button to frame
        add("Center", Shape);       //add shape button to frame
        add("Center", Clear);       //add tail button to the frame
        add("Center", Tail);        //add clear button to the frame
        add("Center", Quit);        //add quit button to the frame

        add(SpeedScrollBar);        //add speed scroll bar to frame
        add(ObjSizeScrollBar);      //add size scroll bar to frame
        add(SPEEDL);                //add speed label to the frame
        add(SIZEL);                 //add size label to the frame
        add(Obj);                   //add object to the frame


        SpeedScrollBar.addAdjustmentListener(this);     //add speedscroll listener
        ObjSizeScrollBar.addAdjustmentListener(this);   //add speedscroll listener
        Start.addActionListener(this);      //add start listener
        Shape.addActionListener(this);      //add shape listener
        Clear.addActionListener(this);      //add clear listener
        Tail.addActionListener(this);       //add tail listener
        Quit.addActionListener(this);       //add quit listener
        this.addComponentListener(this);    //add component listener
        this.addWindowListener(this);       //add window listener
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(getPreferredSize());
        setBounds(WinLeft, WinTop, WIDTH, HEIGHT);  //size and position the frame
        validate(); //validate layout


    }

    private void SizeScreen()
    {
        //position buttons
        Start.setLocation(CENTER-2*(BUTTONW+BUTTONS)-BUTTONW/2, ScreenHeight+BUTTONHS+I.top);
        Shape.setLocation(CENTER - BUTTONW - BUTTONS - BUTTONW/2, ScreenHeight+BUTTONHS+I.top);
        Tail.setLocation(CENTER-BUTTONW/2, ScreenHeight+BUTTONHS+I.top);
        Clear.setLocation(CENTER+BUTTONS+BUTTONW/2, ScreenHeight+BUTTONHS+I.top);
        Quit.setLocation(CENTER+BUTTONW+2*BUTTONS+BUTTONW/2, ScreenHeight+BUTTONHS+I.top);

        //position scrollbars
        SpeedScrollBar.setLocation(I.left+BUTTONS, ScreenHeight+BUTTONHS+I.top);
        ObjSizeScrollBar.setLocation(WinWidth-ScrollBarW-I.right-BUTTONS, ScreenHeight+BUTTONHS+I.top);
        SPEEDL.setLocation(I.left+BUTTONS, ScreenHeight+BUTTONHS+BUTTONH+I.top);
        SIZEL.setLocation(WinWidth-ScrollBarW-I.right, ScreenHeight+BUTTONHS+BUTTONH+I.top);

        //size scrollbars
        SpeedScrollBar.setSize(ScrollBarW, SCROLLBARH);
        ObjSizeScrollBar.setSize(ScrollBarW, SCROLLBARH);
        SPEEDL.setSize(ScrollBarW, BUTTONH);
        SIZEL.setSize(ScrollBarW, SCROLLBARH);
        Obj.setBounds(I.left, I.top, ScreenWidth, ScreenHeight);



        //size buttons
        Start.setSize(BUTTONW, BUTTONH);
        Shape.setSize(BUTTONW, BUTTONH);
        Tail.setSize(BUTTONW, BUTTONH);
        Clear.setSize(BUTTONW, BUTTONH);
        Quit.setSize(BUTTONW, BUTTONH);
    }

    public void start()     //start method
    {
        if (thethread == null)      //if thread is not created
        {
            thethread = new Thread(this);   //create new thread
            thethread.start();  //start thread
        }
        Obj.setx((Obj.getOSize()-1)/2);
        Obj.sety((Obj.getOSize()-1)/2);
        Obj.repaint();      //repaint object
    }

    public void run()       //run method
    {
        while (runobj)      //while runobj is true
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
                move();     //move the object
                Obj.repaint();      //repaint

            }
        }
    }


    public void stop()      //stop method
    {
        runobj = false;     //set runobj to false
        //interrupt thread, remove everything from the screen, dispose, and then exit
        thethread.interrupt();
        Start.removeActionListener(this);
        Shape.removeActionListener(this);
        Tail.removeActionListener(this);
        Clear.removeActionListener(this);
        Quit.removeActionListener(this);
        SpeedScrollBar.removeComponentListener(this);
        ObjSizeScrollBar.removeComponentListener(this);
        this.removeComponentListener(this);
        this.removeWindowListener(this);
        dispose();
        System.exit(0);
    }

    public void move()      //move method
    {
        //find minimum and maximum widths and heights of current screen
        int side1 = Obj.findmaxX();
        int side2 = Obj.findminx();
        int top = Obj.findMinY();
        int bottom = Obj.findMaxY();

        //if the object's y next position is greater than the bottom screen, flip the down flag
        if (Obj.gety()+1 >= bottom-2)
        {
            Obj.setDown(false);
        }
        //if the y position is greater than or equal to the top, set down to true
        else if (Obj.gety() <= top+1)
        {
            Obj.setDown(true);
        }

        //if object's x's next position is greater than the right side, set right to false
        if (Obj.getx()+1 >= side1-1)
        {
            Obj.setRight(false);
        }
        //if the x position is less than the left side, set right to true
        else if (Obj.getx() <= side2+1)
        {
            Obj.setRight(true);
        }

            //add 1 if down is true
        if (Obj.getDown() == true)
        {
            Obj.sety(Obj.gety()+1);
        }
        //otherwise, subtract 1
        else
        {
            Obj.sety(Obj.gety()-1);
        }

        //add one to x if right is true
        if (Obj.getRight() == true)
        {
            Obj.setx(Obj.getx()+1);
        }

        //subtract one otherwise
        else
        {
            Obj.setx(Obj.getx()-1);
        }

        //repaint
        Obj.repaint();
    }

    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        //create new integer TS
        int TS;
        Scrollbar sb = (Scrollbar)e.getSource();        //get scrollbar that trigger event
        if (sb == SpeedScrollBar)
        {
            //get value from speed scrollbar
            TS = e.getValue();
            delay = (ScrollBarW + SBvisible) / TS;      //set delay accordingly
            if(delay < 2)
                delay = 2;
            if(delay > 9)
                delay = 9;

        }
        if (sb == ObjSizeScrollBar)
        {
            //Obj.setx(Obj.getx());           //get current x and y values
            //Obj.sety(Obj.gety());
            int oldsize = Obj.getOSize();           //set oldsize to the current size

            if ((Obj.Xcheck() == false || Obj.Ycheck() == false) && (e.getValue() > oldsize))     //check if the x and y remain in bounds with new value
            {
                boolean b = Obj.Xcheck();
                boolean d = Obj.Ycheck();
                //Obj.size(oldsize);          //if not, reset the object size and scrollbar position
                sb.setValue(oldsize);
            }
            else
            {
                boolean b = Obj.Xcheck();
                boolean d = Obj.Ycheck();
                TS = e.getValue();      //get value
                TS = (TS/2)*2+1;        //make odd to account for center position
                Obj.size(TS);           //update object
            }

            //clear if no tail
            if (Obj.getTail() == false)
            {
                Obj.Clear();
            }
        }
        Obj.repaint();      //force repaint
    }

    public void componentResized(ComponentEvent e)
    {
        WinWidth = getWidth();      //get the width
        WinHeight = getHeight();    //get the height
        ScreenWidth = WinWidth - I.left - I.right;  //set the screenwidth to the window width minus the right and left insets
        ScreenHeight = WinHeight - I.top - 2 * (BUTTONH + BUTTONHS) - I.bottom; //set the screenheight to top inset minus 2 * the sum of BUTTONH and BUTTONHS subtracted by insets bottom


        // Resize the object canvas
        Obj.reSize(ScreenWidth, ScreenHeight);      //resizes canvas
        Obj.setMaxy(Obj.findMaxY());                //set maximum values
        Obj.setMiny(Obj.findMinY());
        Obj.setMaxx((Obj.findmaxX()));
        Obj.setMinx(Obj.findminx());

        int x = Obj.getx();     //store x and y in variables
        int y = Obj.gety();
        if(x > Obj.findmaxX() || y > Obj.findMaxY())        //check if object is still on the screen
        {
            //redraw to be on the screen
            Obj.setx((Obj.getOSize()-1)/2);
            Obj.sety((Obj.getOSize()-1)/2);
            Obj.repaint();
        }
        // Recalculate and resize components
        MakeSheet();
        SizeScreen();
    }
    @Override
    public void componentHidden(ComponentEvent e){}

    public void componentShown(ComponentEvent e){}

    public void componentMoved(ComponentEvent e){}


    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();      //get the source
        if (source == Start)                //if it is the start button
        {
            if (Start.getLabel() == "Pause")        //if the label is pause
            {
                Start.setLabel("Run");      //change it to run
                Timepause = true;           //flip timepause flag
            }
            else
            {
                Start.setLabel("Pause");    //otherwise change it to pause
                Timepause = false;          //flip timepause tail
                runobj = true;              //set runobj to true
            }
        }
        if (source == Shape)    //if it is the shape button
        {
            if (Shape.getLabel() == "Circle")       //if the label is circle
            {
                Shape.setLabel("Square");       //set label to square
                Obj.rectangle(false);          //set rectangle to false
            }
            else
            {
                Shape.setLabel("Circle");       //set label to circle
                Obj.rectangle(true);        //set rectangle to true
            }
            Obj.repaint();
        }
        if (source == Tail)     //if source is tail
        {
            if (Tail.getLabel() == "Tail")  //if label is tail
            {
                Tail.setLabel("No Tail");       //change label to no tail
                Obj.flipTail(true);         //set tail to true
            }
            else
            {
                Tail.setLabel("Tail");          //change label to tail
                Obj.flipTail(false);        //set tail to false
            }
        }

        if (source == Clear)        //if label is clear
        {
            Obj.Clear();            //clear the object
            Obj.repaint();          //repaint the object
        }
        if (source == Quit)
        {
            stop();                 //stop the program
        }
    }

    public void windowClosed(WindowEvent e){}
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e)
    {
            stop();
    }

    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}

}

class Objc extends Canvas{
    private static final long serialversionUID = 11L;
    private int ScreenWidth;
    private int ScreenHeight;
    private int SObj;
    private int x, y, minx, miny, xmax, ymax, oldx, oldy;
    private boolean rect;
    private boolean clear;
    private boolean tail = true;
    private boolean down, right;


    public Objc(int SB, int w, int h)
    {
        //set values, set flags to true, find minimum/maximum values
        ScreenWidth = w;
        ScreenHeight = h;
        SObj = SB;
        rect = true;
        clear = true;
        down = true;
        right = true;
        miny = findMinY();
        minx = findminx();
        xmax = findmaxX();
        ymax = findMaxY();
        y = (SObj-1)/2 - 1;
        x = (SObj-1)/2 + 1;
    }

    public int getx(){return this.x;}       //return x value
    public int gety(){return this.y;}       //return y value
    public void setx(int newx){this.x = newx;}  //set x value
    public void sety(int newy){this.y = newy;}  //set y value
    public int findminx()   //find minimum x
    {
        minx = (SObj - 1) / 2; // Adjusting for the center of the object
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
        xmax = x + (SObj - 1) / 2;
        int maxXScreen = ScreenWidth - (SObj - 1) / 2;      //set max screen to screenwidth - (Sobj - 1) / 2
        if (xmax >= maxXScreen)
        {
            return maxXScreen; //returns maxscreen if xmax is larger than maxscreen
        } else
        {
            return xmax; //returns xmax otherwise
        }
    }

    public int findMinY() { //find minimum y
        int minY = (SObj - 1) / 2; // Adjusting for the center of the object
        if (minY < 0)
        {
            return 0; //returns 0 miny is less than the left screen
        } else
        {
            return minY;    //return miny if not greater than 0
        }
    }

    public int findMaxY()//find maximum y
    {
        ymax = y + (SObj - 1) / 2; // Adjusting for the center of the object
        int maxYScreen = ScreenHeight - (SObj - 1) / 2; // Adjusting for the height of the object
        if (ymax >= maxYScreen)
        {
            return maxYScreen; //returns maxscreen if ymax is larger than maxscreen
        } else
        {
            return ymax;        //returns ymax otherwise
        }
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
    public boolean getTail()
    {
        return tail;
    }   //get tail flag

    public int getoldx(){return this.oldx;}     //get old x
    public int getoldy(){return this.oldy;}     //get old y
    public void flipTail(boolean val)
    {
        tail = val;
    }   //set tail value

    public void rectangle(boolean r)
    {
        rect = r;
    }   //set rectangle boolean
    public void size(int NS)
    {
        SObj = NS;
    }   //set object size
    public void reSize(int w, int h)    //resize function
    {
        //set screenwidth and height
        ScreenWidth = w;
        ScreenHeight = h;
        //get old x and y values
        y = getoldy();
        x = getoldx();
        //set new minimum and maximum values
        setMinx(findminx());
        setMaxx(findmaxX());
        setMiny(findMinY());
        setMaxy(findMaxY());
    }
    public void Clear()
    {
        clear = true;
    }       //clear function

    @Override
    public void paint(Graphics g)   //paint function
    {
        g.setColor(Color.red);      //sets color to red
        g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1);    //draw a rectangle
        update(g);      //call update
    }

    public void update(Graphics g)  //udpate function
    {
        if (clear)      //check clear flag
        {
            super.paint(g);     //paint again
            clear = false;      //clear is false
            g.setColor(Color.red);  //set red color
            g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1); //draw rectangle again
        }

        boolean check = getTail();      //get tail flag
        if (!check) //if false
        {
            g.setColor(getBackground());        //get background color and set g to that color
            if (rect)
            {
                g.fillRect(oldx-(SObj-1)/2, oldy-(SObj-1)/2,SObj, SObj);        //if the rectangle flag is true, fill the rectangle
            }
            else
            {
                g.fillRect(x - (SObj - 1) / 2, y - (SObj - 1) / 2, SObj, SObj);
                g.fillOval(oldx-(SObj-1)/2-1, oldy-(SObj-1)/2-1, SObj+2,SObj+2);        //otherwise fill the oval
            }
        }


            if (rect)       //check the rect flag
            {
                //draw a light gray object with a black border
                g.setColor((Color.lightGray));
                g.fillRect(x - (SObj - 1) / 2, y - (SObj - 1) / 2, SObj, SObj);
                g.setColor(Color.black);
                g.drawRect(x - (SObj - 1) / 2, y - (SObj - 1) / 2, SObj - 1, SObj - 1);
            } else
            {
                //otherwise draw a light gray oval with a black border
                g.setColor((Color.lightGray));
                g.fillOval(x - (SObj - 1) / 2, y - (SObj - 1) / 2, SObj, SObj);
                g.setColor(Color.black);
                g.drawOval(x - (SObj - 1) / 2, y - (SObj - 1) / 2, SObj-1, SObj-1);
            }

        oldx = getx();      //get oldx and y valyus
        oldy = gety();


    }
}

