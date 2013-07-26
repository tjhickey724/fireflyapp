package edu.brandeis.tjhickey.fireflies;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;



/**
 * a GameView is a 2D view of a GameModel
 * the aspect of the view and model may be different
 * Since the GameModel is a square space, the GameView takes
 * the minimum of the width and height of the JPanel and uses
 * that to scale the GameModel to the Viewing window.
 * Calling repaint() on the GameView will cause it to render
 * the current state of the Model to the JPanel canvas...
 * @author tim
 *
 */
public class GameView extends View {

	private GameModel gm = null;
	private Paint mPaint;
	private Paint fPaint;  // fireflies
	private Paint wPaint;  // wasps
	private Paint aPaint;  // avatars
	private Paint tempPaint;
	private PointF tempPoint = new PointF(0f,0f);
	
	public void setGameModel(GameModel gm){
		this.gm = gm;
	}
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Set up default Paint values
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.BLUE);
		
		fPaint = new Paint();
		fPaint.setAntiAlias(true);
		fPaint.setStyle(Style.FILL);
		fPaint.setColor(Color.GREEN);
		
		wPaint = new Paint();
		wPaint.setAntiAlias(true);
		wPaint.setStyle(Style.FILL);
		wPaint.setColor(Color.RED);
		wPaint.setTextSize(50f);
		
		aPaint = new Paint();
		aPaint.setAntiAlias(true);
		aPaint.setStyle(Style.STROKE);
		aPaint.setColor(Color.WHITE);
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		clearBackground(canvas);
		drawActors(canvas);
		drawActor(canvas,gm.avatar);
		drawActor(canvas,gm.hole);
		//Log.d("main","drawing the view");
		canvas.drawText(""+System.nanoTime(),50,50,wPaint);
	}
	
	private void drawActors(Canvas canvas){
		for (GameActor a:gm.actors){
			drawActor(canvas,a);
		}
	}
	
	private void drawActor(Canvas canvas, GameActor a){
		this.tempPoint.x = (float)a.x;
		this.tempPoint.y = (float)a.y;
		Point r = this.toViewCoords(tempPoint);
		if (a.species == Species.firefly){
			this.tempPaint = fPaint;
		}else if (a.species == Species.wasp){
			this.tempPaint = wPaint;
		} else {
			this.tempPaint = aPaint;
		}
				
		canvas.drawCircle(r.x,r.y,this.toViewCoords(a.radius),this.tempPaint);	
	}
	
	private void clearBackground(Canvas canvas){
		// Calculate geometry
		int w = getWidth();
		int h = getHeight();
		canvas.drawRect(0, 0,w,h,mPaint);
	}
	
	
	/**
	 * toViewCoords(x) converts from model coordinates to pixels
	 * on the screen so that objects can be drawn to scale, i.e.
	 * as the screen is resized the objects change
	 * size proportionately.  
	 * @param x the unit in model coordinates 
	 * @return the corresponding value in pixel based on window-size
	 */
	public float toViewCoords(float x){
		float width = this.getWidth();
		float height = this.getHeight();
		float viewSize = (width<height)?width:height;
		return x/gm.size*viewSize;
	}
	
	public Point toViewCoords(PointF p){
		Point q = new Point(0,0);
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		q.x = (int) Math.round(p.x/gm.size*viewSize);
		q.y = (int) Math.round(p.y/gm.size*viewSize);
		return q;
	}
	/**
	 * toModelCoords(x) is used to convert mouse locations
	 * to positions in the model so that the avatar position
	 * in the model can be changed correctly
	 * @param x position in pixels in view
	 * @return position in model coordinates
	 */
	public float toModelCoords(float x){
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		return x*gm.size/viewSize;
	}
	
	public PointF toModelCoords(PointF p){
		PointF q = new PointF(0f,0f);
		int width = this.getWidth();
		int height = this.getHeight();
		float viewSize = (width<height)?width:height;
		q.x = (float) (p.x*gm.size/viewSize);
		q.y = (float) (p.y*gm.size/viewSize);
		return q;
	}
	
	
	
	// The Ôactive pointerÕ is the one currently moving our object.
	private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	private float mLastTouchX,mLastTouchY;
	private float mPosX,mPosY;
	
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
	        final int pointerIndex = 
	                ev.findPointerIndex(mActivePointerId);  
	            
	        final float x = ev.getX(pointerIndex);
	        final float y = ev.getY(pointerIndex);
	            
	        // Calculate the distance moved
	        final float dx = x - mLastTouchX;
	        final float dy = y - mLastTouchY;

	        mPosX += dx;
	        mPosY += dy;
	        PointF dp = new PointF(dx,dy);
	        dp = toModelCoords(dp);
	        gm.moveAvatar(dp);

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
