package edu.brandeis.tjhickey.fireflies;

import android.util.Log;
import android.view.View;


public class GameLoop implements Runnable{

	private GameModel gm; 
	private MainActivity mainActivity;
	
	public GameLoop(GameModel gm, MainActivity m) {
	 this.gm=gm;
	 this.mainActivity=m;
	}

	public void run(){
		final View gameView = 
				mainActivity.findViewById(R.id.game_view);

		while(true){
			if (gm.isStopped()) return; // end the loop
			
			// update the model
			gm.update();
			Log.d("loop", "in GameLoop");
			
			// repaint the gameView, safely
			gameView.postInvalidate();
			
			// sleep for 0.05 seconds
			try{
				Thread.sleep(50l);
			}catch(Exception e){
				System.out.println("In game loop:"+ e);
			}
		}
	}

}
