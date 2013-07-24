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
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		clearBackground(canvas);
		drawActors(canvas);
		Log.d("main","drawing the view");
		canvas.drawText(""+System.nanoTime(),50,50,wPaint);
	}
	
	private void drawActors(Canvas canvas){
		for (GameActor a:gm.actors){
			this.tempPoint.x = (float)a.x;
			this.tempPoint.y = (float)a.y;
			Point r = this.toViewCoords(tempPoint);
			if (a.species == Species.firefly){
				this.tempPaint = fPaint;
			}else {
				this.tempPaint = wPaint;
			}
			
			canvas.drawCircle(r.x,r.y,10,this.tempPaint);
		}
	}
	
	private void clearBackground(Canvas canvas){
		// Calculate geometry
		int w = getWidth();
		int h = getHeight();
		canvas.drawRect(0, 0,w,h,mPaint);
	}
	/*
	) {
		super();
		this.gm = gm;
		MouseInputListener ml =
				new CanvasMouseInputListener();
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
	}
	*/
	
	/**
	 * toViewCoords(x) converts from model coordinates to pixels
	 * on the screen so that objects can be drawn to scale, i.e.
	 * as the screen is resized the objects change
	 * size proportionately.  
	 * @param x the unit in model coordinates 
	 * @return the corresponding value in pixel based on window-size
	 */
	public int toViewCoords(double x){
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		return (int) Math.round(x/gm.size*viewSize);
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
	public double toModelCoords(int x){
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		return x*gm.size/viewSize;
	}
	
	public PointF toModelCoords(Point p){
		PointF q = new PointF(0f,0f);
		int width = this.getWidth();
		int height = this.getHeight();
		float viewSize = (width<height)?width:height;
		q.x = (float) (p.x*gm.size/viewSize);
		q.y = (float) (p.y*gm.size/viewSize);
		return q;
	}

	/**
	 * paintComponent(g) draws the current state of the model
	 * onto the component. It first repaints it in blue, 
	 * then draws the avatar,
	 * then draws each of the other actors, i.e. fireflies and wasps...
	 */
	/*
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (gm==null) return;
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(Color.BLUE);
		g.fillRect(0,0,width,height);
		drawActor(g,gm.avatar,Color.GREEN);
		for(GameActor a:gm.actors){
			drawActor(g,a,Color.RED);
		}
		g.setFont(new Font("Helvetica",Font.BOLD,64));
		if (gm.gameOver){
			g.drawString("You Won!!!", width/10, height/2);
		}

	}
	*/
	
	/**
	 * drawActor(g,a,c) - draws a single actor a 
	 * using the Graphics object g. The color c is the
	 * default color used for new species, but is ignored
	 * for avatars, wasps, and fireflies
	 * 
	 * @param g - the Graphics object used for drawing
	 * @param a - the Actor to be drawn
	 * @param c - the default color for actors of unknown species
	 */
	/*
	private void drawActor(Graphics g, GameActor a,Color c){
		if (!a.active) return;
		int theRadius = toViewCoords(a.radius);
		int x = toViewCoords(a.x);
		int y = toViewCoords(a.y);
		
		switch (a.species){
		case firefly: c=Color.GREEN; break;
		case wasp: c=Color.RED; break;
		case avatar: c=Color.BLACK; break;
		}
		g.setColor(c);
		if (a.species==Species.avatar){
			g.drawOval(x-theRadius, y-theRadius, 2*theRadius, 2*theRadius);
		} else
			g.fillOval(x-theRadius, y-theRadius, 2*theRadius, 2*theRadius);

	}
	*/
	
	/**
	 * this listens for mouse clicks (which unpauses the game)
	 * and mouse movements which move the avatar
	 * @author tim
	 *
	 */
	/*
	private class CanvasMouseInputListener extends MouseInputAdapter{
		public void mouseClicked(MouseEvent e){
			gm.paused= !(gm.paused); //false;
		}
		public void mouseMoved(MouseEvent e){
			Point p = e.getPoint();
			double x = toModelCoords(p.x); 
			double y = toModelCoords(p.y);
			gm.avatar.x =x;
			gm.avatar.y =y;
			//System.out.println("x="+p.x+" y="+p.y);
			e.getComponent().repaint();		
		}
	}
	*/

}
