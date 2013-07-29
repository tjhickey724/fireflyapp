package edu.brandeis.tjhickey.fireflies;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	GameModel gm;
	GameLoop gl;
	SeekBar bar;
	private int padxpos;
	public TextView textView1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startGame();
		bar = (SeekBar)findViewById(R.id.seekBar1);
		textView1 = (TextView) findViewById(R.id.textView1);
		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            public void onStopTrackingTouch(SeekBar bar)
            {
                setpadx(bar.getProgress()); // the value of the seekBar progress
            }

            public void onStartTrackingTouch(SeekBar bar)
            {

            }

            public void onProgressChanged(SeekBar bar,
                    int paramInt, boolean paramBoolean)
            {
                setpadx(paramInt);
                textView1.setText("" + paramInt + "%");
            }
        });
	}
	
	
    public int getpadx() {
		return padxpos;
	}


	public void setpadx(int x) {
		this.padxpos = x;
	}


	private void startGame(){
		GameView gameView = (GameView) this.findViewById(R.id.game_view);
		gm = new GameModel(100,20);
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
