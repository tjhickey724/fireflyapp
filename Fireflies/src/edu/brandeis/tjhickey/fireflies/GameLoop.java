package edu.brandeis.tjhickey.fireflies;

import android.util.Log;
import android.view.View;


public class GameLoop implements Runnable{

	private GameModel gm; 
	private FireflyCanvas fc;
	
	public GameLoop(GameModel gm, FireflyCanvas fc) {
	 this.gm=gm;
	 this.fc=fc;

	}

	public void run(){


		while(true){
			if (gm.isStopped()) return; // end the loop
			
			// update the model
			gm.update();
			Log.d("loop", "in GameLoop");
			
			// repaint the gameView, safely
			fc.redraw();
			
			// sleep for 0.05 seconds
			try{
				Thread.sleep(50l);
			}catch(Exception e){
				System.out.println("In game loop:"+ e);
			}
		}
	}

}
