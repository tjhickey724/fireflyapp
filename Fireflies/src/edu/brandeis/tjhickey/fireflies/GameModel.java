package edu.brandeis.tjhickey.fireflies;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.PointF;

/**
 * This is a simple model for a game with objects that
 * move around the screen and the user needs to 
 * catch them by clicking on them. This class
 * represents the state of the game
 * @author tim
 *
 */
public class GameModel {
	float width;
	float height;
	float size;
	
	List<GameActor> actors;
	GameActor avatar;
	GameActor hole;

	boolean gameOver = false;
	boolean paused = true;
	
	int numActors;
	int numActive;

	private Random rand = new Random();
	
	private PointF avatarMovement = new PointF(0f,0f);


	public GameModel(float size, int numActors) {
		this.width =size;
		this.height = size;
		this.size=size;
		
		this.numActors = numActors;
		initActors();
		
		this.avatar = new GameActor(size/2,size/2);
		avatar.species = Species.avatar;
		this.avatar.radius=4;
		
		this.hole = new GameActor(size/2,size/2);
		hole.species = Species.hole;
		this.hole.radius = 6;
		
		this.gameOver = false;
	}
	/**
	 * initActors creates a new ArrayList of actors
	 * which consists of 90% fireflies and 10% wasps
	 * REFACTOR!
	 */
	public void initActors(){
		numActive=0;
		this.actors = new ArrayList<GameActor>();
		for(int i=0; i<numActors;i++){
			float x = rand.nextFloat()*width;
			float y = rand.nextFloat()*height;
			GameActor a = new GameActor(x,y);
			this.actors.add(a);
			a.speed = 2;
			a.radius = 3;
			if (numActive> numActors-3){
				a.species = Species.wasp;
			}else{
				a.species = Species.firefly;
				numActive++;
			}
		}	
	}
	
	
	public void start(){
		paused = false;
	}
	
	public void stop(){
		paused = true;
	}
	
	public void moveAvatar(PointF dp){
		this.avatarMovement.x += dp.x;
		this.avatarMovement.y += dp.y;
	}
	
	/**
	 * update moves all actors one step and if
	 * any fireflies that intersect with the avatar
	 * are remove, while if a wasp intersects the avatar,
	 * the game ends
	 * REFACTOR
	 */
	public void update(){
		if (paused || gameOver) return;
		float storedx = avatar.x;
		float storedy = avatar.y;
		
		avatar.x += avatarMovement.x;
		avatar.y += avatarMovement.y;
		
		float avatarVX = (float) (avatar.x-storedx);
		float avatarVY = (float) (avatar.y-storedy);
		for(GameActor a:this.actors){
			if (a.active) {
				a.update();
				keepOnBoard(a);
				if (intersects(a,hole)) {
					a.active=false;
					numActive--;
					if (a.species==Species.wasp){
						initActors(); // you lose and have to restart!
					}
				}
				
				if (intersects(a,avatar)){
					a.vx = (a.x-avatar.x);
					a.vy = (a.y-avatar.y);
					
				}
		
				
				
			} else {
				
				//a.x += avatarMovement.x;
				//a.y += avatarMovement.y;
			}

			

		}
		
		
		avatarMovement.x=0;
		avatarMovement.y=0;
		
		if (numActive==0)
			gameOver=true;
	}
	
	/**
	 * if an actor moves off the board, in the x (or y) direction, 
	 * it is bounced back into the board and its velocity in the
	 * offending direction is reversed
	 * @param a
	 */
	public void keepOnBoard(GameActor a){
		if (a.x<0) {
			a.x = -a.x;a.vx = -a.vx;
		}else if (a.x> width){
			a.x = width - (a.x-width);
			a.vx = -a.vx;
		}
		if (a.y<0) {
			a.y = -a.y;a.vy = -a.vy;
		}else if (a.y > height){
			a.y = height - (a.y-height);
			a.vy=-a.vy;
		}
	}
	
	/**
	 * this returns true if the two actors intersect
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean intersects(GameActor a, GameActor b){
		float dx=a.x-b.x;
		float dy = a.y-b.y;
		float d = (float) Math.sqrt(dx*dx+dy*dy);
		return (d < a.radius + b.radius);
	}
	
	
               
                
                
}
	



