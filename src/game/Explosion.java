package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;


/**
 *  Represents explosion in the game. 
 * @author Jerry Li
 */



public class Explosion extends Sprite
{
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(64, 64);
    
    private int LIFE_TIME = 15;
    private boolean visible = true;
    
    /*
     * Creates a background based on given image, location, and size
     */
    public Explosion(Pixmap Image, Location center, Dimension size) {
    	super (Image, center, size);
    }
    
    /**
     * returns if explosion still in animation
     * @return
     */
    public boolean getVisible(){
    	return visible;
    }
    
    /**
     * make explosion invisible 
     */
    public void makeInvisible(){
    	visible = false;
    }
    
    /**
     * update explosion timer
     */
    public void update(){
    	LIFE_TIME--;
    	if (LIFE_TIME <= 0){
    		makeInvisible();
    	}
    }
    
}