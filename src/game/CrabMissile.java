package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;

/**
 *  Represents a missile that the Crab ship fires.
 *  
 * @author Jerry Li
 */
public class CrabMissile extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(18, 35);
    private final int FIRE_SPEED = 20;
    private final Vector FIRE_VELOCITY = new Vector(DOWN_DIRECTION, FIRE_SPEED);
    private boolean visible = true;
    
    /**
     * Constructs a CrabMissile fired by the crab with given size and location
     * @param Image
     * @param center
     * @param size
     * @param canvas
     */
    public CrabMissile (Pixmap Image, Location center, Dimension size) {
    	super (Image, center, size);
    }
    
    /**
     * Checks if CrabMissile is visible
     * @return
     */
    public boolean getVisibleStatus() {
    	return visible;
    }
    
    /**
     * Make CrabMissile invisible
     */
    public void setInvisible() {
    	visible = false;
    }
    
    /**
     * Update missile state
     * Moves missile down
     * If missile is out of bounds, sets missile invisible
     * @param elapsedTime
     * @param bounds
     */
    public void update(double elapsedTime, Dimension bounds) {

   		translate(FIRE_VELOCITY);
    
     	if (getY() > bounds.height) {
    		visible = false;
    	}
    }
}