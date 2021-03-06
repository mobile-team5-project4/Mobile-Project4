package mobile.team5.Project4;

import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class CircleMinigame extends Minigame {
	private ShapeDrawable circle;
	private Point userPoint;
	private Point centerPoint;
	private int radius;
	private boolean selected, scoreSubmitted = false;

	public CircleMinigame(Context con, int width, int height) {
		super(con, width, height);
		Random rand = new Random();

		int minRad = (int) (width * .25);
		int maxRad = (int) (width * .5);
		int rad = rand.nextInt(maxRad - minRad) + minRad;
		radius = rad;

		Rect bounds = new Rect(0, 0, width, height);
		bounds.bottom -= rad;
		bounds.top += rad;
		bounds.left += rad;
		bounds.right -= rad;

		int x = rand.nextInt(bounds.right - bounds.left) + bounds.left;
		int y = rand.nextInt(bounds.bottom - bounds.top) + bounds.top;

		int user_x = x + rand.nextInt(rad);
		int user_y = y + rand.nextInt(rad);

		centerPoint = new Point(x, y);
		userPoint = new Point(user_x, user_y);
		selected = true;

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
			c.drawBitmap(reticle, userPoint.x - (reticle.getWidth() / 2),
					userPoint.y - (reticle.getHeight() / 2), null);
		}
		if (scoreSubmitted) {
			c.drawBitmap(greenReticle, centerPoint.x
					- (greenReticle.getWidth() / 2), centerPoint.y
					- (greenReticle.getHeight() / 2), null);
		}
	}

	@Override
	public Double getScore() {
		double x = userPoint.x - centerPoint.x;
		double y = userPoint.y - centerPoint.y;
		double dist = Math.abs(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
		dist = dist / radius;
		dist = dist * MAX_SCORE;
		if (dist > MAX_SCORE)
			dist = MAX_SCORE;
		scoreSubmitted = true;
		return dist;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (userPoint != null) {
			userPoint.x = (int) e.getX();
			userPoint.y = (int) e.getY();
		} else {
			userPoint = new Point((int) e.getX(), (int) e.getY());
			selected = true;
		}
		return true;
	}// asdf

	public boolean onDoubleTap(MotionEvent e) {
		double score = getScore();
		String s = "Score = " + score;
		Log.d("Game", s);
		Toast.makeText(con, s, Toast.LENGTH_SHORT).show();
		return true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
		if (userPoint != null) {
			userPoint.x -= (int) dx;
			userPoint.y -= (int) dy;
			if (userPoint.x < 0)
				userPoint.x = 0;
			else if (userPoint.x > width)
				userPoint.x = width;
			if (userPoint.y < 0)
				userPoint.y = 0;
			else if (userPoint.y > height)
				userPoint.y = height;
		} else {
			userPoint = new Point((int) e1.getX(), (int) e1.getY());
			selected = true;
		}
		return true;
	}

	@Override
	public String getInstructions() {
		return "Find the center of the circle.";
	}
}
