package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;


/**
 *  Represents the background of the game.
 *  
 * @author Jerry Li
 */



public class Background extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(1000, 800);
    private static final int TRAVEL_SPEED = 5;
    private static final Vector TRAVEL_VELOCITY = new Vector(DOWN_DIRECTION, TRAVEL_SPEED);
    
    /*
     * Creates a background based on given image, location, and size
     */
    public Background (Pixmap Image, Location center, Dimension size) {
    	super (Image, center, size);
    }
    
    /**
     * updates background. Background travels down to give
     * sense of movement. Loops in model paintBackground method
     */
    public void update(double elapsedTime, Dimension bounds){
    	translate(TRAVEL_VELOCITY);
    	if ((this.getY() - 384) >= bounds.height){
    		setCenter(bounds.width/2, bounds.height/2 - 768);
    	}
    }
    
}