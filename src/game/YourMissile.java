package game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;
import view.Canvas;
import java.util.ArrayList;


/**
 *  Represents a missile that your ship fires.
 *  
 * @author Jerry Li
 */
public class YourMissile extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(15, 15);
    public static final int DEFAULT_OFFSET = 50;
    // movement
    private static final int FIRE_SPEED = 50;
    private static final int FIRE_UP = KeyEvent.VK_SPACE; 
    private static final Vector FIRE_VELOCITY = new Vector(UP_DIRECTION, FIRE_SPEED);
    
    
    private boolean visible = true;
    
    private Canvas myCanvas;
    private ArrayList<Integer> fire = new ArrayList<Integer>();
    
    public YourMissile (Pixmap Image, Location center, Dimension size, Canvas canvas) {
    	super (Image, center, size);
    	myCanvas = canvas;
    }
    
    public boolean getVisibleStatus() {
    	return visible;
    }
    
    public void setInvisible() {
    	visible = false;
    }
    public void update(double elapsedTime, Dimension bounds) {
    	int key = myCanvas.getLastKeyPressed();
    	if (key == FIRE_UP) {
    		fire.add(3);
    	}
    	if (fire.contains(3)){
    		translate(FIRE_VELOCITY);
    	}
    	
    	if (getY() < 0) {
    		visible = false;
    	}
      }
}