package mobile.team5.Project4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Minigame {
	protected Bitmap reticle, greenReticle;
	protected int width, height;
	protected Context con;

	static final int MAX_SCORE = 100;

	Minigame(Context con, int width, int height) {
		this.con = con;
		this.width = width;
		this.height = height;

		reticle = BitmapFactory.decodeResource(con.getResources(),
				R.drawable.squarereticle);

		greenReticle = BitmapFactory.decodeResource(con.getResources(),
				R.drawable.greensquarereticle);

	}

	abstract public void gameDraw(Canvas c);

	abstract public Double getScore();

	abstract public String getInstructions();

	abstract public boolean onSingleTapConfirmed(MotionEvent e);

	abstract public boolean onDoubleTap(MotionEvent e);

	abstract public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
			float dy);
	
	public static double getDistance(Point a, Point b) {
		double x = a.x - b.x;
		double y = a.y - b.y;
		return Math.abs(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}

	// Will need a series of functions here for each of the gestures
	// we use during all minigames. The Game class can then call within
	// it's respective gesture functions, the minigame version of these,
	// overriden by the subclasses, with minigame.onTouchEvent(), or
	// minigame.onFling(), etc...
}
