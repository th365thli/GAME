package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;

/**
 *  Represents a missile that the Locust ship fires.
 *  
 * @author Jerry Li
 */
public class LocustMissile extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(32, 32);
    
    private final int FIRE_SPEED = 15;
    private final Vector FIRE_VELOCITY = new Vector(DOWN_DIRECTION, FIRE_SPEED);
    
    private boolean visible = true;
    
    /**
     * Create a Locust Missile with given location and size
     * @param Image
     * @param center
     * @param size
     * @param canvas
     */
    public LocustMissile (Pixmap Image, Location center, Dimension size) {
    	super (Image, center, size);
    }
    
    /**
     * Return if missile is visible
     * @return visible
     */
    public boolean getVisibleStatus() {
    	return visible;
    }
    
    /**
     * Make missile invisible
     */
    public void setInvisible() {
    	visible = false;
    }
    
    /**
     * Update missile state
     * If missile is out of bounds, make it invisible
     */
    public void update(double elapsedTime, Dimension bounds) {

   		translate(FIRE_VELOCITY);
    
     	if (getY() > bounds.height) {
    		setInvisible();
    	}
    }
}