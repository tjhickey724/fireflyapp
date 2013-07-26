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
	float radius = 1;
	// position
	float x;
	float y;
	// velocity
	float vx;
	float vy;
	// still on board?
	boolean active;
	// speed
	float speed=1;
	// turning speed
	float turnspeed = 0;
	// species
	Species species = Species.firefly; 
	
	private java.util.Random rand = new java.util.Random();

	public GameActor(float x, float y, boolean active) {
		this.x=x; this.y=y; this.active=active;
		this.vx = speed*(rand.nextFloat()-0.5f);
		this.vy = speed*(rand.nextFloat()-0.5f);
	}
	

	
	public GameActor(float x, float y){
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

		vx += rand.nextFloat()*turnspeed -turnspeed/2;
		vy += rand.nextFloat()*turnspeed -turnspeed/2;
		float tmpSpeed = (float) Math.sqrt(vx*vx+vy*vy);
		x += vx*speed/tmpSpeed;
		y += vy*speed/tmpSpeed;
	}
	
	public String toString(){
		int ix = (int)x;
		int iy = (int)y;
		return "["+ix+","+iy+","+active+"]";
	}
	
	public float getvx(){
		return this.vx;
	}

	public float getvy(){
		return this.vy;
	}

}
