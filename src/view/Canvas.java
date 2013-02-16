package view;

import game.Model;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;


/**
 * Creates an area of the screen in which the game will be drawn that supports:
 * <UL>
 *   <LI>animation via the Timer
 *   <LI>mouse input via the MouseListener
 *   <LI>keyboard input via the KeyListener
 * </UL>
 * 
 * @author Jerry Li
 */
public class Canvas extends JComponent {
	
    // default serialization ID
    private static final long serialVersionUID = 1L;
    // animate 25 times per second if possible
    public static final int FRAMES_PER_SECOND = 30;
    // better way to think about timed events (in milliseconds)
    public static final int ONE_SECOND = 1000;
    public static final int DEFAULT_DELAY = ONE_SECOND / FRAMES_PER_SECOND;
    // input state
    public static final int NO_KEY_PRESSED = -1;
    
    public static final int ENTER = KeyEvent.VK_ENTER;
    
    private Set<Integer> myKeys;
    
    // drives the animation
    private Timer myTimer;
    // game to be animated
    private Model myGame;
    // input state
    private int myLastKeyPressed;
    private Point myLastMousePosition;
    
    
    /**
     * Create a panel so that it knows its size
     */
    public Canvas (Dimension size) {
        // set size (a bit of a pain)
        setPreferredSize(size);
        setSize(size);
        // prepare to receive input
        setFocusable(true);
        requestFocus();
        setInputListeners();
      
    }
    
    public Timer getTimer(){
    	return myTimer;
    }

    /**
     * Paint the contents of the canvas.
     * 
     * Never called by you directly, instead called by Java runtime
     * when area of screen covered by this container needs to be
     * displayed (i.e., creation, uncovering, change in status)
     * 
     * @param pen used to paint shape on the screen
     */
    @Override
    public void paintComponent (Graphics pen) {
 
        //pen.fillRect(0, 0, getSize().width, getSize().height);
        // first time needs to be special cased :(
    		paintSplash((Graphics2D)pen);
    		paintGame(pen);
    }
    
    /**
     * Paint components in game
     * @param pen
     */
    public void paintGame(Graphics pen) {
    	if(myGame != null){
    		if(!myGame.getLoadingStatus()){
    			myGame.paintGame((Graphics2D) pen);
    		}
    	}
    }
    
    /**
     * Paint loading screen
     * @param pen
     */
    public void paintSplash(Graphics2D pen) {
		Image over = new ImageIcon(getClass().getResource("../images/loading.gif")).getImage();
		pen.drawImage(over, 0, 0, this);
	
    }

    /**
     * Returns last key pressed by the user.
     */
    public int getLastKeyPressed () {
        return myLastKeyPressed;
    }
    
 
    /**
     * Returns last position of the mouse in the canvas.
     */
    public Point getLastMousePosition () {
        return myLastMousePosition;
    }
    
    /**
     * Return game model created
     * @return myGame
     */
    public Model getGame() {
    	return myGame;
    }

    /**
     * Start the animation.
     */
    public void start () {
        final int stepTime = DEFAULT_DELAY;
        // create a timer to animate the canvas
        Timer timer1 = new Timer(stepTime, 
            new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    myGame.update((double) stepTime / ONE_SECOND);
                    repaint();
                }
            });
        
        // start animation
        myGame = new Model(this);
        repaint();
        timer1.start();
    }
    /**
     * Stop the animation.
     */
    public void stop () {
        myTimer.stop();
    }
    
    /**
     * Returns all keys currently pressed by the user.
     */
    // MULTIPLE KEY SUPPORT
    public Collection<Integer> getKeysPressed () {
        return Collections.unmodifiableSet(myKeys);
    }
    
    /**
     * Create listeners that will update state based on user input.
     */
    private void setInputListeners () {
        // initialize input state
        myLastKeyPressed = NO_KEY_PRESSED;
        // MULTIPLE KEY SUPPORT
        myKeys = new TreeSet<Integer>();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e) {
                myLastKeyPressed = e.getKeyCode();
                // MULTIPLE KEY SUPPORT
                myKeys.add(e.getKeyCode());
            }
            @Override
            public void keyReleased (KeyEvent e) {
                myLastKeyPressed = NO_KEY_PRESSED;
                // MULTIPLE KEY SUPPORT
                myKeys.remove((Integer)e.getKeyCode());
            }
        });
        myLastMousePosition = new Point();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved (MouseEvent e) {
                myLastMousePosition = e.getPoint();
            }
        });
    }
}