package edu.brandeis.tjhickey.fireflies;

import android.util.Log;
import android.view.View;


public class GameLoop implements Runnable{

	private GameModel gm; 
	private GameView gv;
	
	public GameLoop(GameModel gm, GameView gv) {
	 this.gm=gm;
	 this.gv=gv;

	}

	public void run(){


		while(true){
			if (gm.isStopped()) return; // end the loop
			
			// update the model
			gm.update();
			
			// repaint the gameView, safely
			gv.redraw();
			
			// sleep for 0.05 seconds
			try{
				Thread.sleep(50l);
			}catch(Exception e){
				System.out.println("In game loop:"+ e);
			}
		}
	}

}
