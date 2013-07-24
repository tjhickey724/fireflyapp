package edu.brandeis.tjhickey.fireflies;
/**
 * a GameActor has a position and a velocity and a speed
 * they also have a species 
 * and they keep track of whether they are active or not
 * @author tim
 *
 */
public class GameActor {
	// size
	double radius = 10;
	// position
	double x;
	double y;
	// velocity
	double vx;
	double vy;
	// still on board?
	boolean active;
	// speed
	double speed=1;
	// species

	Species species = Species.firefly; 
	
	private java.util.Random rand = new java.util.Random();

	public GameActor(double x, double y, boolean active) {
		this.x=x; this.y=y; this.active=active;
		this.vx = speed*(rand.nextDouble()-0.5);
		this.vy = speed*(rand.nextDouble()-0.5);
	}
	
	public GameActor(double x, double y){
		this(x,y,true);
	}
	
	public GameActor(){
		this(0,0,true);
	}

	/**
	 * actors change their velocity slightly at every step
	 * but their speed remains the same. Update slightly modifies
	 * their velocity and uses that to compute their new position.
	 * Note that velocity is in units per update.
	 */
	public void update(){
		double turnspeed = 0.1;
		vx += rand.nextDouble()*turnspeed -turnspeed/2;
		vy += rand.nextDouble()*turnspeed -turnspeed/2;
		double tmpSpeed = Math.sqrt(vx*vx+vy*vy);
		x += vx*speed/tmpSpeed;
		y += vy*speed/tmpSpeed;
	}
	
	public String toString(){
		int ix = (int)x;
		int iy = (int)y;
		return "["+ix+","+iy+","+active+"]";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
