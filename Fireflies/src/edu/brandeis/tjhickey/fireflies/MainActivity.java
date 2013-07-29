package edu.brandeis.tjhickey.fireflies;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	GameModel gm;
	GameLoop gl;
	Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startGame();
		b = (Button) this.findViewById(R.id.restartbutton);
		b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGame();
			}
			
		});
	}
	
    private void startGame(){
		GameView gameView = (GameView) this.findViewById(R.id.game_view);
		gm = new GameModel(100,10);
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
		super.onStop();
		gm.gameOver = true;
	}
	

}
