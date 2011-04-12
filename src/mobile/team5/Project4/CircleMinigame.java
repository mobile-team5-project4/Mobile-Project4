package mobile.team5.Project4;

import java.util.Random;

import mobile.team5.Project4.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.MotionEvent;

public class CircleMinigame extends Minigame {
	private ShapeDrawable circle;
	private Point userPoint;
	private Point centerPoint;
	boolean selected;
	Bitmap reticle;
	int width, height;

	public CircleMinigame(Context con, int w, int h) {
		Random rand = new Random();

		width = w;
		height = h;
		reticle = BitmapFactory.decodeResource(con.getResources(),
				R.drawable.squarereticle);

		int minRad = (int) (width * .25);
		int maxRad = (int) (width * .5);
		int rad = rand.nextInt(maxRad - minRad) + minRad;

		Rect bounds = new Rect(0, 0, width, height);
		bounds.bottom -= rad;
		bounds.top += rad;
		bounds.left += rad;
		bounds.right -= rad;

		int x = rand.nextInt(bounds.right - bounds.left) + bounds.left;
		int y = rand.nextInt(bounds.bottom - bounds.top) + bounds.top;

		centerPoint = new Point(x, y);

		OvalShape oval = new OvalShape();
		oval.resize(width, height);

		circle = new ShapeDrawable(oval);
		circle.setBounds(x - rad, y - rad, x + rad, y + rad);
		Paint p = circle.getPaint();
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2.0f);
	}

	@Override
	public void gameDraw(Canvas c) {
		circle.draw(c);
		if (selected) {
			c.drawBitmap(reticle, userPoint.x - (reticle.getWidth() / 2), userPoint.y - (reticle.getHeight() / 2), null);
		}
	}

	@Override
	public Double getScore() {
		double x = userPoint.x - centerPoint.x;
		double y = userPoint.y - centerPoint.y;
		double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

		return dist;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if(userPoint != null) {
			userPoint.x = e.getX();
			userPoint.y = e.getY();
		} else {
			userPoint = new Point(e.getX(), e.getY());
			selected = true;
		}
		return true;
	}

	public boolean onDoubleTap(MotionEvent e) {
		double score = getScore();
		String s = "Score = " + score;
		Log.d("Game", s);
		return true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
		if(userPoint != null) {
			userPoint.x -= dx;
			userPoint.y -= dy;
			if(userPoint.x < 0)
				userPoint.x = 0;
			else if(userPoint.x > width)
				userPoint.x = width;
			if(userPoint.y < 0)
				userPoint.y = 0;
			else if(userPoint.y > height)
				userPoint.y = height;
		} else {
			userPoint = new Point(e1.getX(), e1.getY());
			selected = true;
		}
		return true;
	}
}
