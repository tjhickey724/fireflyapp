package edu.brandeis.tjhickey.fireflies;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	GameModel gm;
	GameLoop gl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GameView gameView = (GameView) this.findViewById(R.id.game_view);
		gm = new GameModel(100,100);
		gameView.setGameModel(gm);
		gl = new GameLoop(gm,this);
		Thread t = new Thread(gl);
		gm.paused = false;
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onStop(){
		gm.gameOver = true;
	}
	public void repaint(){
		GameView gameView = (GameView) this.findViewById(R.id.game_view);
		gameView.postInvalidate();
		Log.d("main","postInvalidate");
	}

}
