package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;
import java.util.*
;

/**
 *  Represents a spaceship that the player controls.
 *  
 * @author Jerry Li
 */
public class Locust extends Sprite
{
    // reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(40, 40);
    public static final int DEFAULT_OFFSET = 500;
    private static final int MOVE_SPEED = 7;
    private static final Vector LEFT_VELOCITY = new Vector(LEFT_DIRECTION, MOVE_SPEED);
    private static final Vector RIGHT_VELOCITY = new Vector(RIGHT_DIRECTION, MOVE_SPEED);
    private static final Vector UP_VELOCITY = new Vector(UP_DIRECTION, MOVE_SPEED);
    private static final Vector DOWN_VELOCITY = new Vector(DOWN_DIRECTION, MOVE_SPEED);
    
    private boolean isFiring = false;
	private boolean alive = true;
	private int FIRECODE = 50;
	private int moveCounter = 0;
	private int health = 3;
   
	/**
	 * Crates locust enemy ship given location and size
	 * @param image
	 * @param center
	 * @param size
	 * @param canvas
	 */
    public Locust (Pixmap image, Location center, Dimension size)
    {
        super(image, center, size);
    }
    
    /**
     * return health of locust
     * @return health
     */
    public int getHealth() {
    	return health;
    }
    
    /**
     * subtracts one from health of locust
     */
    public void takeHit() {
    	health -= 1;
    }
    
    /**
     * returns if locust is alive
     * @return alive
     */
    public boolean isAlive() {
    	return alive;
    }
    
    /**
     * kills locust
     * (called when health is 0)
     */
    public void makeDead() {
    	alive = false;
    }
    
    /**
     * Creates a random code and checks if it is equal to fireCode
     * If so, change Locust firing state
     * Firing is handled differently than Spaceship
     * 	-For ship, missile is created in ship object
     * 	-For Locust and Crab, missile is created in model object
     * 	-The purpose of this is because if a Locust/Crab/Spaceship object
     * 	 is removed, it also removes the missile(s) it created, which is 
     * 	 not desirable for game play 
     * @param elapsedTime
     * @param bounds
     */
    public void fire(int fireRate) {
    	if (fireRate == FIRECODE){
    		isFiring = true;
    	}
    	else {
    		isFiring = false;
    	}
    		
    }
    /**
     * Sets fire code to parameter. Used for cheat 
     * @param x
     */
    public void setFireCode(int x){
    	FIRECODE = x;
    }
    
    /**
     * returns if Locust is firing at moment
     * @return isFiring
     */
    public boolean actOfFire(){
    	return isFiring;
    }
    
    /**
     * Update Locust state
     * @param elapsedTime
     * @param bounds
     */
    public void update (double elapsedTime, Dimension bounds)
    {
    	Random rand = new Random();
    	int fireRate = rand.nextInt(100);
    	    	
    	if (this.getHealth() <= 0) {
    			this.makeDead();
    	}
    	fire(fireRate);
    	moveCounter++;
    	move();
    }
    
    /**
     * Update locust movement
     * @param bounds
     */
    public void move(){
    	if (moveCounter >= 5 && moveCounter < 10) {
    		translate(DOWN_VELOCITY);
    	}
    	else if (moveCounter >= 15 && moveCounter < 20) {
    		translate(LEFT_VELOCITY);
    	}
    	else if (moveCounter >= 25 && moveCounter < 30){
    		translate(UP_VELOCITY);
    	}
    	else if (moveCounter >= 35 &&moveCounter < 40) {
    		translate(RIGHT_VELOCITY);
    	}
    	else if (moveCounter >= 45){
    		moveCounter = 0;
    	}
    }
   
}