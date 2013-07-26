package edu.brandeis.tjhickey.fireflies;

import android.content.Context;


import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;

/**
 * a GameView is a SurfaceView that hides the FireflyCanvas
 * which is where the real action is. This class responds to
 * stop() calls from the MainActivity and also to onTouch events
 * It plays a dual role as an instance of the SurfaceHolder.Callback
 * which allows us to discover when the surface is created, destroyed, or changed in size.
 * 
 * @author tim
 * 
 */
public class GameController extends SurfaceView implements SurfaceHolder.Callback {


	private GameView gameView;
	private SurfaceHolder surfaceHolder;
	
	public GameController(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);



	}
	
	public void stop(){
		gameView.stop();
	}
	
	
	// here is where we implement the SurfaceHolder.Callback interface
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		gameView.changeDimensions(width,height);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameView = new GameView(surfaceHolder);
		gameView.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameView.stop();
		
	}
	// end of the SurfaceHolder.Callback interface
	
	
	// finally we respond to TouchEvents by calling fc.moveAvatar(...)
	
	
	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	private float mLastTouchX, mLastTouchY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.
		// mScaleDetector.onTouchEvent(ev);

		final int action = ev.getActionMasked();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = ev.getActionIndex();
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Remember where we started (for dragging)
			mLastTouchX = x;
			mLastTouchY = y;
			// Save the ID of this pointer (for dragging)
			mActivePointerId = ev.getPointerId(0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Find the index of the active pointer and fetch its position
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);

			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Calculate the distance moved
			final float dx = x - mLastTouchX;
			final float dy = y - mLastTouchY;

			PointF dp = new PointF(dx, dy);
			
			gameView.moveAvatar(dp);

			invalidate();

			// Remember this touch position for the next move event
			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = MotionEvent.INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = MotionEvent.INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {

			final int pointerIndex = ev.getActionIndex();
			final int pointerId = ev.getPointerId(pointerIndex);

			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
			}
			break;
		}
		}
		return true;
	}



}
