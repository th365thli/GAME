package game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;
import view.Canvas;
import java.util.*;

/**
 *  Represents a spaceship that the player controls.
 *  
 * @author Jerry Li
 */
public class Spaceship extends Sprite
{
	
	//Resources used by Spaceship Object
	private static final Pixmap MISSILE1_IMAGE = new Pixmap("myMissile.gif");
    
	// reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(40, 40);
    public static final int DEFAULT_OFFSET = 50;
    private int coolDownThreshold = 0;
    public static ArrayList<YourMissile> ammo;
    
    // movement
	public ArrayList<Integer> keysPressed = new ArrayList<Integer>();
    private static final int FIRE = KeyEvent.VK_SPACE;
    private static final int MOVE_LEFT = KeyEvent.VK_A;
    private static final int MOVE_RIGHT = KeyEvent.VK_D;
    private static final int MOVE_UP = KeyEvent.VK_W;
    private static final int MOVE_DOWN = KeyEvent.VK_S;
    private static final int MOVE_SPEED = 10;
    private static final Vector LEFT_VELOCITY = new Vector(LEFT_DIRECTION, MOVE_SPEED);
    private static final Vector RIGHT_VELOCITY = new Vector(RIGHT_DIRECTION, MOVE_SPEED);
    private static final Vector UP_VELOCITY = new Vector(UP_DIRECTION, MOVE_SPEED);
    private static final Vector DOWN_VELOCITY = new Vector(DOWN_DIRECTION, MOVE_SPEED);
    
    // get input directly
    private Canvas myCanvas;
    
    /**
     * Create a paddle at the given position, with the given size.
     */
    public Spaceship (Pixmap image, Location center, Dimension size, Canvas canvas)
    {
        super(image, center, size);
        myCanvas = canvas;
        ammo = new ArrayList<YourMissile>();
    }

    /**
     * Return missiles created by ship;
     * @return
     */
    public ArrayList<YourMissile> getAmmo() {
    	return ammo;
    }
    
    /**
     * Creates a missile and adds it to ammo collection
     * Firing is handled differently than Locust and Crab
     * 	-For ship, missile is created in ship object
     * 	-For Locust and Crab, missile is created in model object
     * 	-The purpose of this is because if a Locust/Crab/Spaceship object
     * 	 is removed, it also removes the missile it created from, which is 
     * 	 not desirable for game play 
     * @param elapsedTime
     * @param bounds
     */
    public void fire(double elapsedTime, Dimension bounds) {
    	YourMissile bullet = new YourMissile(MISSILE1_IMAGE, new Location(this.getX(), this.getY()), YourMissile.DEFAULT_SIZE, myCanvas);
    	ammo.add(bullet);
    	
    	
    }
    
    /**
     * Updates the missiles created by Spaceship
     * Iterates through collection
     * @param elapsedTime
     * @param bounds
     */
    public void updateAmmo(double elapsedTime, Dimension bounds) {
    	ArrayList<YourMissile> ammo = getAmmo();
     	for (int i = 0; i < ammo.size(); i++){
     		YourMissile m = (YourMissile) ammo.get(i);
     		if (m.getVisibleStatus()){
     			m.update(elapsedTime, bounds);
     		}
     		else {
     			ammo.remove(m);
     		}
     	}
    }
    
    /**
     * Describes how to "animate" the shape by changing its state.
     * Also updates Spaceship state
     * Currently, moves the shape based on the keys pressed.
     */
    public void update (double elapsedTime, Dimension bounds)
    {
    	int key = myCanvas.getLastKeyPressed();
    	move(elapsedTime, bounds);
        if (key == FIRE) {
        		if (coolDownThreshold <= 0){
        			fire(elapsedTime, bounds);
        			coolDownThreshold += 25;
        		}
        }
        coolDownThreshold -= 3;
        updateAmmo(elapsedTime, bounds);
        
    }
    
    /** 
     * move Ship based on canvas input
     * @param elapsedTime
     * @param bounds
     */
    public void move(double elapsedTime, Dimension bounds){
    	if (myCanvas.getKeysPressed().contains(MOVE_LEFT) && getLeft() > 0){
    		translate(LEFT_VELOCITY);
    	}
    	if (myCanvas.getKeysPressed().contains(MOVE_RIGHT) && getRight() < bounds.width){
    		translate(RIGHT_VELOCITY);
    	}
    	if (myCanvas.getKeysPressed().contains(MOVE_UP) && getTop() > 0){
    		translate(UP_VELOCITY);
    	}
    	if (myCanvas.getKeysPressed().contains(MOVE_DOWN) && getBottom() < bounds.height){
    		translate(DOWN_VELOCITY);
    	}
    }
}
