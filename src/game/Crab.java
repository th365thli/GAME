package game;

import java.awt.Dimension;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;
import java.util.*
;

/**
 *  Represents a Crab Boss enemy with automated movement.
 *  
 * @author Jerry Li
 */
public class Crab extends Sprite
{
    // reasonable default values
    public static final Dimension DEFAULT_SIZE = new Dimension(400, 300);
    public static final int DEFAULT_OFFSET = 500;
    private static final int MOVE_SPEED = 10;
    private static final Vector LEFT_VELOCITY = new Vector(LEFT_DIRECTION, MOVE_SPEED);
    private static final Vector RIGHT_VELOCITY = new Vector(RIGHT_DIRECTION, MOVE_SPEED);
    private static final Vector UP_VELOCITY = new Vector(UP_DIRECTION, MOVE_SPEED);
    private static final Vector DOWN_VELOCITY = new Vector(DOWN_DIRECTION, MOVE_SPEED);
    private int health = 99;
    private boolean alive = true;
    private int moveCounter = 0;
    
    //Firing conditions
    private int FIRECODE = 5;
	private boolean isFiring = false;
    
    /**
     * Construct a Crab Boss enemy ship
     * @param image
     * @param center
     * @param size
     * @param canvas
     */
    public Crab (Pixmap image, Location center, Dimension size)
    {
        super(image, center, size);
    }
    
    /**
     * set firecode to parameter
     * used for cheat
     * @param x
     */
    public void setFireCode(int x){
    	FIRECODE = x;
    }
    
    /**
     * Return the health of the Crab Boss
     */
    public int getHealth() {
    	return health;
    }
    
    /**
     * Subtracts 1 from the Crab Boss
     */
    public void takeHit() {
    	health -= 1;
    }
    
    /**
     * Returns if the Crab is alive
     * @return
     */
    public boolean isAlive() {
    	return alive;
    }
    
    /**
     * Kill the Crab (called when health is zero)
     */
    public void makeDead() {
    	alive = false;
    	
    }
    
    /**
     * Creates a random code and checks if it is equal to fireCode
     * If so, change crab firing state
     * Firing is handled differently than Spaceship
     * 	-For ship, missile is created in ship object
     * 	-For Locust and Crab, missile is created in model object
     * 	-The purpose of this is because if a Locust/Crab/Spaceship object
     * 	 is removed, it also removes the missile(s) it created, which is 
     * 	 not desirable for game play 
     * @param elapsedTime
     * @param bounds
     */
    public void fire() {
    	Random rand = new Random();
    	int fireRate = rand.nextInt(10);
    	if (fireRate == FIRECODE){
    		isFiring = true;
    	}
    	else {
    		isFiring = false;
    	}
    		
    }
    
    /**
     * Return if Crab is firing
     * @return isFiring
     */
    public boolean actOfFire(){
    	return isFiring;
    }
    
    /**
     * Update state of Crab
     */
    public void update (double elapsedTime, Dimension bounds)
    {
    	if (this.getHealth() <= 0) {
    		this.makeDead();
    	}
    	fire();
    	
    	moveCounter++;
    	move();
    }
    
    /**
     * Update the movement of Crab
     * @param bounds
     */
    public void move(){
    	if (moveCounter >= 10 && moveCounter < 30) {
    		translate(DOWN_VELOCITY);
    	}
    	else if (moveCounter >= 40 && moveCounter < 70) {
    		translate(RIGHT_VELOCITY);
    	}
    	else if (moveCounter >= 80 && moveCounter < 100){
    		translate(UP_VELOCITY);
    	}
    	else if (moveCounter >= 110 &&moveCounter < 140) {
    		translate(LEFT_VELOCITY);
    	}
    	else if (moveCounter >= 140){
    		moveCounter = 0;
    	}
    }
}