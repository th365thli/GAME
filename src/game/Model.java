package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import util.Location;
import util.Pixmap;
import util.Sound;
//import util.Sound;
import util.ValueText;
import view.Canvas;


/**
 * Represents a simple game of Space Invaders.
 * 
 * It enforces the rules of the game each moment in the game as well as award points and
 * change levels.
 * 
 * @author Jerry Li
 */
public class Model {
    // resources used in the game
	private static final Pixmap BACKGROUND = new Pixmap("space.gif");
	private static final Pixmap SHIP_IMAGE = new Pixmap("spaceship.gif");
    private static final Pixmap LOCUST_IMAGE = new Pixmap("locust.gif");
	private static final Pixmap CRAB_IMAGE = new Pixmap("boss.gif");
    private static final Pixmap LOCUST_MISSILE_IMAGE = new Pixmap("LocustMissile.gif");
    private static final Pixmap CRAB_MISSILE_IMAGE = new Pixmap("crabMissile.gif");
    private static final Pixmap EXPLOSION = new Pixmap("explosion.gif");
	
    //game input
    public static final int ENTER = KeyEvent.VK_ENTER;
    public static final int SKIP_LEVEL = KeyEvent.VK_L;
    public static final int END_GAME = KeyEvent.VK_E;
    public static final int STOP_FIRING = KeyEvent.VK_B;
    
    // game play values
    private static final int STARTING_LIVES = 3;
    private static final int LOCUST_HIT_SCORE = 20;
    private static final int CRAB_HIT_SCORE = 200;
    private static final int LOCUST_COUNT = 10;
    
    // Object position values
    private static final int CRAB_X_POSITION = 300;
    private static final int CRAB_Y_POSITION = 100;
    
    // heads up display values
    private static final int LABEL_X_OFFSET = 52;
    private static final int LABEL_Y_OFFSET = 25;
    private static final Color LABEL_COLOR = Color.WHITE;
    private static final String SCORE_LABEL = "Score";
    private static final String HEALTH_LABEL = "Health: ";
    
    //game sound
    private static final Sound POP = new Sound("pop.wav");
    private static final Sound BACKGROUND_MUSIC = new Sound("gangnam.mid");
        
    //game state
    private boolean gameWon = false;
    private boolean gameLost = false;
    private boolean level1 = true;
    private boolean level2 = false;
    private boolean gameisLoading = true;
    private boolean gameStart = false;
   
    //game objects
    private Spaceship myShip;
    private ArrayList<Locust> enemyLocusts = new ArrayList<Locust>();
    private ArrayList<Crab> enemyCrabs = new ArrayList<Crab>();
    public ArrayList<LocustMissile> fireOnField = new ArrayList<LocustMissile>();
    public ArrayList<CrabMissile> fireOnField2 = new ArrayList<CrabMissile>();
    public ArrayList<Explosion> explosionOnField = new ArrayList<Explosion>();
    private Canvas myView;
    private Background myBackground;
    private Background myBackgroundLoop;
    private ValueText myScore;
    private ValueText finalScore;
    private ValueText myLevel;
    private ValueText myHealth;
    private ValueText bossHealth;

    /**
     * Create a game of the given size with the given display for its shapes.
     */
    public Model (Canvas canvas) {
        myView = canvas;
        initStatus();
        initBackground(canvas.getSize());
        initShip(canvas.getSize());
        initLocust(canvas.getSize());
    	initCrab(myView.getSize());
    	BACKGROUND_MUSIC.play();
    	gameisLoading = false;
    }

    /**
     * Return the Spaceship object created
     * @return myShip;
     */
    public Spaceship getShip() {
    	return myShip;
    }
    
    /**
     *	Return if the game is still loading assets 
     *	@return gameisLoading
     */	
    public boolean getLoadingStatus(){
    	return gameisLoading;
    }
    
    /**
     * Return if the game has started after loading assets
     * @return gameStart
     */
    public boolean hasGameStarted(){
    	return gameStart;
    }
    
    /**
     * Return the Model
     * @return this
     */
    
    public Model getModel(){
    	return this;
    }
    

	/**
     * Draw all elements of the game.
     */
    
    public void paintGame (Graphics2D pen){
    	if (gameStart == true){
    		paintInit(pen);
    	}
    	if (gameStart == false){
    		paintEnter(pen);
    	}
    	if(gameStart == true && gameLost == false && gameWon == false && level1){
    		paintLocusts(pen);
    		paintExplosion(pen);
    	}
    	if(gameStart == true && gameLost == false && gameWon == false && level2){
    		paintCrab(pen);
    		paintExplosion(pen);
    	}
    	else if (gameLost == true && gameWon == false){
    		gameLost(pen);
    	}
    	else if (gameLost == false && gameWon == true && level2){
    		gameWon(pen);
    	}
    }
    
    public void gameLost(Graphics2D pen){
    	paintGameOver(pen);
	    paintLevelStatus(pen);
	    paintLastStatus(pen);
    }
    
    public void gameWon(Graphics2D pen){
    	paintGameWon(pen);
    	paintLevelStatus(pen);
    	paintLastStatus(pen);
    }
    
    public void paintInit(Graphics2D pen){
		paintBackground(pen);
		paintStatus(pen);
		paintLevelStatus(pen);
		paintShip(pen);
		paintBullets(pen);
    }
    
    public void paintExplosion(Graphics2D pen){
    	for (Explosion e : explosionOnField){
    		e.paint(pen);
    	}
    }
    
    public void paintBackground (Graphics2D pen){
    		myBackground.paint(pen);
    		myBackgroundLoop.paint(pen);
    }
    
    public void paintShip (Graphics2D pen) {
    		myShip.paint(pen);
    }
    
    public void paintBullets (Graphics2D pen) {
   			ArrayList<YourMissile> ammo = myShip.getAmmo();
   			for (int i = 0; i < ammo.size(); i++){
   				YourMissile m = (YourMissile) ammo.get(i);
    			m.paint(pen);
    		}
    	
    		for(LocustMissile e : fireOnField){
    			e.paint(pen);
   			}
   			for(CrabMissile c : fireOnField2){
   				c.paint(pen);
   			}
    }
    
    public void paintLocusts (Graphics2D pen) {
   			for (int i = 0; i < enemyLocusts.size(); i++){
   			enemyLocusts.get(i).paint(pen);
   			}
    }
    
    public void paintCrab (Graphics2D pen) {
			for (int i = 0; i < enemyCrabs.size(); i++){
				enemyCrabs.get(i).paint(pen);
			}
			paintBossStatus(pen);
    }
    
    public void paintGameOver (Graphics2D pen) {
    	Image over = new ImageIcon(getClass().getResource("../images/gameover.gif")).getImage();
    	pen.drawImage(over, 0, 0, myView);
    }
    
    public void paintGameWon (Graphics2D pen) {
    	Image won = new ImageIcon(getClass().getResource("../images/gamewon.gif")).getImage();
    	pen.drawImage(won, 0, 0, myView);    
    }
    
    /**
     * Create a Spaceship object controlled by user
     * @param bounds
     */
    private void initShip (Dimension bounds) {     
        
    	myShip = new Spaceship(SHIP_IMAGE, new Location((bounds.width/2), bounds.height - Spaceship.DEFAULT_OFFSET),
        						Spaceship.DEFAULT_SIZE, myView);
    }  
    
    /**
     * Create Locust enemy ships that are added to a collection 
     * @param bounds
     */
    private void initLocust (Dimension bounds) {
    	//First Row
    	for(int i = 1; i <= LOCUST_COUNT; i++) {	 
    		Locust l = new Locust (LOCUST_IMAGE, new Location((i*80) + 20, 100), 
    								Locust.DEFAULT_SIZE);
    		enemyLocusts.add(l);
    	}
    	//Second Row
    	for (int i = 1; i <= LOCUST_COUNT; i++) {
    		Locust l = new Locust (LOCUST_IMAGE, new Location((i*80) + 20, 250), 
					Locust.DEFAULT_SIZE);
    		enemyLocusts.add(l);
    	}
    }
    
    /**
     * Create a Boss Crab Ship 
     * @param bounds
     */
    
    private void initCrab (Dimension bounds){
    	Crab c = new Crab(CRAB_IMAGE, new Location(CRAB_X_POSITION, CRAB_Y_POSITION), Crab.DEFAULT_SIZE);
    	enemyCrabs.add(c);
    }
    
    /**
     * Create background for game
     * @param bounds
     */
    private void initBackground (Dimension bounds) {
    	myBackground = new Background(BACKGROUND, new Location((bounds.width/2), (bounds.height/2-768)), 
    								Background.DEFAULT_SIZE);
    	myBackgroundLoop = new Background(BACKGROUND, new Location((bounds.width/2), bounds.height/2), 
				Background.DEFAULT_SIZE);
    }
      
    /**
     * Create initial "heads up display", i.e., status labels that will appear in the game.
     */
    private void initStatus () {
    	myLevel = new ValueText("LEVEL: ", 1);
        myScore = new ValueText(SCORE_LABEL, 0);
        myHealth = new ValueText(HEALTH_LABEL, STARTING_LIVES);
        bossHealth = new ValueText("BOSS HEALTH: ", 100);
    }
    
    /**
     * Create a final "heads up display" that displays user final score
     */
    private void finalStatus(){
    	finalScore = new ValueText("FINAL SCORE: ", myScore.getValue());
    }
    
    /**
     * Ends game (win)
     */
    public void cheatEndGame(int key){
    	if (key == END_GAME){
    		level1 = false;
    		level2 = true;
    		initGameWon();
    	}
    }
    /**
     * Cheat the skips level
     * @param key
     */
    public void cheatSkipLevel(int key){
    	if (key == SKIP_LEVEL){
    		initLevel2();
    	}
    }
    /**
     * Cheat that stops enemies from firing
     * @param key
     */
    public void cheatStopFiring(int key){
    	if (key == STOP_FIRING){
    		for (Crab c : enemyCrabs){
    			c.setFireCode(-1);
    		}
    		for (Locust l : enemyLocusts){
    			l.setFireCode(-1);
    		}
    	}
    }
    
    /**
     * Start the game
     * @param key
     */
    public void startGame(int key){
    	if (key == ENTER){
    		gameStart = true;
    	}
    }
    
    /**
	 *  game for this moment, given the time since the last moment.
	 */
	public void update (double elapsedTime) {
		if (!gameisLoading){
			int key = myView.getLastKeyPressed();
			startGame(key);
			cheatSkipLevel(key);
			cheatStopFiring(key);
			cheatEndGame(key);
			if(gameStart){
				Dimension bounds = myView.getSize();
				updateSprites(elapsedTime, bounds);
				intersectSprites(bounds);
				checkRules(bounds);
			}
		}
	}

	/**
     * Update sprites based on time since the last update.
     * Based on level status
     */
    private void updateSprites (double elapsedTime, Dimension bounds) {
    	myBackground.update(elapsedTime, bounds);
    	myBackgroundLoop.update(elapsedTime, bounds);
    	if (level1){
    		myShip.update(elapsedTime, bounds);
    		updateLocusts(elapsedTime);
    		updateLocustFire(elapsedTime, bounds);
    		updateFireOnField(elapsedTime, bounds);
    	}
    	else if (level2){
    		myShip.update(elapsedTime, bounds);
    		updateCrab(elapsedTime);
    		updateCrabFire(elapsedTime, bounds);
    		updateFireOnField2(elapsedTime, bounds);
       	}
    	updateExplosions();
    }
    
    /**
     * updates explosions on field
     */
    public void updateExplosions(){
    	Iterator<Explosion> expIter = explosionOnField.iterator();
		while (expIter.hasNext()){
			Explosion e = expIter.next();
			if (e.getVisible()){
				e.update();
			}
			else {
				expIter.remove();
			}
		}
    }
    
    /**
     * Update the Crab object for this moment
     * @param elapsedTime
     */
    public void updateCrab (double elapsedTime) {
        Dimension bounds = myView.getSize();
    	for (Crab c : enemyCrabs) {
    		c.update(elapsedTime, bounds);
    	}
    }
    
    /**
     * Update the Locust collection for this moment
     * @param elapsedTime
     */
    
    public void updateLocusts (double elapsedTime) {
        Dimension bounds = myView.getSize();
    	for (Locust l : enemyLocusts) {
    		l.update(elapsedTime, bounds);
    	}
    	
    }
    
    /**
     *  check if the Crab is firing at this moment
     *  If it is firing a crab missile is created and added to a missile collection
     *  @param elapsedTime
     *  @param bounds
     */
    public void updateCrabFire(double elapsedTime, Dimension bounds){
    	Random rand = new Random();
    	int missilePosition = rand.nextInt(400);
    	for (Crab c : enemyCrabs) {
    		if (c.actOfFire()){
    			CrabMissile e = new CrabMissile(CRAB_MISSILE_IMAGE, new Location(missilePosition+(c.getX()-200), c.getY()), 
    												CrabMissile.DEFAULT_SIZE);
    			fireOnField2.add(e);
    		}
    	}
    }
    
    /**
     * Update if each Locust in the collection is firing at the moment
     * If it is firing it creates a locust missile and adds it to a collection
     * @param elapsedTime
     * @param bounds
     */
    public void updateLocustFire (double elapsedTime, Dimension bounds) {
    	for (Locust l : enemyLocusts) {
    		if (l.actOfFire()){
    			LocustMissile e = new LocustMissile(LOCUST_MISSILE_IMAGE, new Location(l.getX(), l.getY()), 
    												LocustMissile.DEFAULT_SIZE);
    			fireOnField.add(e);
    		}
    	}
    }
    
    /**
     * Iterates through the collection of Locust missiles and updates its position
     * Once the missile goes out of bounds it removes missile from collection
     * @param elapsedTime
     * @param bounds
     */
    public void updateFireOnField (double elapsedTime, Dimension bounds){
    	for (int i = 0; i < fireOnField.size(); i++){
     		LocustMissile m = (LocustMissile) fireOnField.get(i);
     		if (m.getVisibleStatus()){
     			m.update(elapsedTime, bounds);
     		}
     		else {
     			fireOnField.remove(m);
     		}
     	}
    }
    
    /**
     * Iterates through collection of Crab missiles and updates its position
     * Once missile goes out of bounds it removes missile from colleciton
     * @param elapsedTime
     * @param bounds
     */
    public void updateFireOnField2 (double elapsedTime, Dimension bounds){
    	for (int i = 0; i < fireOnField2.size(); i++){
     		CrabMissile m = (CrabMissile) fireOnField2.get(i);
     		if (m.getVisibleStatus()){
     			m.update(elapsedTime, bounds);
     		}
     		else {
     			fireOnField2.remove(m);
     		}
     	}
    }
    
    /**
     * Check if game objects intersect
     * @param bounds
     */
    private void intersectSprites (Dimension bounds) {
    	if (level1){
    		LocustMissileShip();
    		MissileLocust();
    		LocustShip();
    	}
    	else if (level2) {
    		CrabMissileShip();
    		MissileCrab();
    		CrabShip();
    	}
    }
    
    /** check if Locust ship collides with spaceship
     * 
     */
    private void LocustShip(){
    	Iterator<Locust> locIter = enemyLocusts.iterator();
    	while(locIter.hasNext()) {
    		Locust l = locIter.next();
    		if (l.intersects(myShip)){
    			myHealth.updateValue(-3);
    		}
    	}
    }
    
    /**
     * check if Crab boss collides with spaceship
     */
    private void CrabShip(){
    	Iterator<Crab> crabIter = enemyCrabs.iterator();
    	while(crabIter.hasNext()) {
    		Crab c = crabIter.next();
    		if (c.intersects(myShip)){
    			myHealth.updateValue(-3);
    		}
    	}
    }
    
    
    /**
     * check if LocustMissile intersects user Spaceship
     */
    private void LocustMissileShip(){
		Iterator<LocustMissile> eneIter = fireOnField.iterator();
		while (eneIter.hasNext()){
			LocustMissile l = eneIter.next();
			if (l.intersects(myShip)){
				Explosion e = new Explosion(EXPLOSION, new Location(l.getX(), l.getY()), Explosion.DEFAULT_SIZE);
				explosionOnField.add(e);
				myHealth.updateValue(-1);
				eneIter.remove();
			}
		}
	}
    
    /**
     * check if CrabMissile intersects user Spaceship;
     */
	private void CrabMissileShip(){
		Iterator<CrabMissile> ene1Iter = fireOnField2.iterator();
		while (ene1Iter.hasNext()){
			CrabMissile c = ene1Iter.next();
			if (c.intersects(myShip)){
				Explosion e = new Explosion(EXPLOSION, new Location(c.getX(), c.getY()), Explosion.DEFAULT_SIZE);
				explosionOnField.add(e);
				myHealth.updateValue(-1);
				ene1Iter.remove();
			}
		}
		
	}
	
	/**
	 * check if user missile intersects crab
	 */
	private void MissileCrab(){
		Iterator<YourMissile> missileIter = myShip.getAmmo().iterator();
		Iterator<Crab> crabIter = enemyCrabs.iterator();
		while (missileIter.hasNext()){
			YourMissile m = missileIter.next();
			while (crabIter.hasNext()){
				Crab c = crabIter.next();
				if (m.intersects(c)){
					Explosion e = new Explosion(EXPLOSION, new Location(m.getX(), m.getY()-150), Explosion.DEFAULT_SIZE);
					explosionOnField.add(e);
					c.takeHit();
					POP.play();
					bossHealth.setValue(c.getHealth());
					missileIter.remove();
					if(!c.isAlive()){
						crabIter.remove();
						myScore.updateValue(CRAB_HIT_SCORE);
					}
				}
			}
		}
	}

	/**
	 * Check if user missile intersect Locust object
	 */
	
	private void MissileLocust(){
		Iterator<YourMissile> missileIter = myShip.getAmmo().iterator();
		Iterator<Locust> locustIter = enemyLocusts.iterator();
		while (missileIter.hasNext()){
			YourMissile m = missileIter.next();
			while (locustIter.hasNext()){
				Locust l = locustIter.next();
				if (m.intersects(l)){
					Explosion e = new Explosion(EXPLOSION, new Location(m.getX(), m.getY()), Explosion.DEFAULT_SIZE);
					explosionOnField.add(e);
					l.takeHit();
					POP.play();
					missileIter.remove();
					if(!l.isAlive()){
						locustIter.remove();
						myScore.updateValue(LOCUST_HIT_SCORE);
					}
				}
			}
		}
		
	}

	/**
     * Check the game level and if the game is won or lost
     * @param bounds
     */
    private void checkRules (Dimension bounds) {
    	if (level1){
    		initLevel1();
    	}
    	if (myHealth.getValue() <= 0) {
    		initGameLost();
    		BACKGROUND_MUSIC.stop();
    	}
    	if (enemyLocusts.size() <= 0) {
    		initLevel2();
    	}
    	if (enemyCrabs.size() <= 0){
    		initGameWon();
    	}
        
    }
  
    /**
     * Calls the final status and changes game status to "lost"
     */
    private void initGameLost(){
    	finalStatus();
    	gameLost = true;
    	BACKGROUND_MUSIC.stop();
    }
    
    /**
     * Calls final status and changes game status to "won"
     */
    private void initGameWon(){
		finalStatus();
		gameWon = true;
		BACKGROUND_MUSIC.stop();

    }
    
    /**
     * Initiates level 1
     * @param bounds
     */
    private void initLevel1(){
	     level1 = true;
	     level2 = false;
	}
    
    /**
     * Initiates level 2
     * @param bounds
     */
	private void initLevel2(){
    	fireOnField.clear();
    	myLevel.setValue(2);
    	level2 = true;
    	level1 = false;
    }

    /**
     * Display labels on screen
     */
    private void paintStatus (Graphics2D pen) {
        myScore.paint(pen, new Point(LABEL_X_OFFSET, LABEL_Y_OFFSET), LABEL_COLOR);
        myHealth.paint(pen, new Point(myView.getSize().width - LABEL_X_OFFSET, LABEL_Y_OFFSET), LABEL_COLOR);        
    }
    
    private void paintLevelStatus (Graphics2D pen) {
    	myLevel.paint(pen, new Point(LABEL_X_OFFSET, LABEL_Y_OFFSET + 30), LABEL_COLOR);
    }
    
    private void paintLastStatus (Graphics2D pen) {
    	finalScore.paint(pen, new Point(LABEL_X_OFFSET + 25, LABEL_Y_OFFSET), LABEL_COLOR);
    }
    private void paintBossStatus (Graphics2D pen) {
    	bossHealth.paint(pen, new Point(myView.getSize().width-70, LABEL_Y_OFFSET + 20), LABEL_COLOR);
    }
    public void paintEnter(Graphics2D pen) {
		Image enter = new ImageIcon(getClass().getResource("../images/pressEnter.gif")).getImage();
		pen.drawImage(enter, 0, 0, myView);
	
    }
   
}
