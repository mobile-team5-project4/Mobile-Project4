package mobile.team5.Project4;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class EquilateralTriangleMinigame extends Minigame {

	private static final double PI = 3.14159265;

	private Point fixedPoint1;
	private Point fixedPoint2;
	private Point user;
	private Point finalPoint;
	private Point mirrorFinalPoint;
	private boolean submitted = false;

	EquilateralTriangleMinigame(Context con, int width, int height) {
		super(con, width, height);
		Random rand = new Random();

		int minLen = (int) (width * .25);
		int maxLen = (int) (width * .5);
		int len = rand.nextInt(maxLen - minLen) + minLen;

		Rect bounds = new Rect(0, 0, width, height);
		bounds.bottom -= len;
		bounds.top += len;
		bounds.left += len;
		bounds.right -= len;

		int x = rand.nextInt(bounds.right - bounds.left) + bounds.left;
		int y = rand.nextInt(bounds.bottom - bounds.top) + bounds.top;
		fixedPoint1 = new Point(x, y);

		x = rand.nextInt(2 * len) - len + x;
		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(fixedPoint1.x - x, 2))) - fixedPoint1.y);
		fixedPoint2 = new Point(x, y);

		double s60 = Math.sin(60 * PI / 180.0);
		double c60 = Math.cos(60 * PI / 180.0);

		x = (int) (c60 * (fixedPoint1.x - fixedPoint2.x) - s60
				* (fixedPoint1.y - fixedPoint2.y) + fixedPoint2.x);
		y = (int) (s60 * (fixedPoint1.x - fixedPoint2.x) + c60
				* (fixedPoint1.y - fixedPoint2.y) + fixedPoint2.y);

		finalPoint = new Point(x, y);

		double sm60 = Math.sin(-60 * PI / 180.0);
		double cm60 = Math.cos(-60 * PI / 180.0);

		x = (int) (cm60 * (fixedPoint1.x - fixedPoint2.x) - sm60
				* (fixedPoint1.y - fixedPoint2.y) + fixedPoint2.x);
		y = (int) (sm60 * (fixedPoint1.x - fixedPoint2.x) + cm60
				* (fixedPoint1.y - fixedPoint2.y) + fixedPoint2.y);

		mirrorFinalPoint = new Point(x, y);

		x = (bounds.bottom - bounds.top) / 2;
		y = (bounds.right - bounds.left) / 2;

		user = new Point(x, y);
	}

	@Override
	public void gameDraw(Canvas c) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2.0f);
		paint.setColor(Color.BLACK);

		c.drawLine(fixedPoint1.x, fixedPoint1.y, fixedPoint2.x, fixedPoint2.y,
				paint);

		// if (pointSet)
		paint.setColor(Color.BLUE);
		c.drawLine(fixedPoint1.x, fixedPoint1.y, user.x, user.y, paint);
		c.drawLine(fixedPoint2.x, fixedPoint2.y, user.x, user.y, paint);

		if (submitted) {
			paint.setColor(Color.GREEN);
			if (getDistance(finalPoint, user) < getDistance(mirrorFinalPoint,
					user)) {
				c.drawLine(fixedPoint1.x, fixedPoint1.y, finalPoint.x,
						finalPoint.y, paint);
				c.drawLine(fixedPoint2.x, fixedPoint2.y, finalPoint.x,
						finalPoint.y, paint);
			} else {
				c.drawLine(fixedPoint1.x, fixedPoint1.y, mirrorFinalPoint.x,
						mirrorFinalPoint.y, paint);
				c.drawLine(fixedPoint2.x, fixedPoint2.y, mirrorFinalPoint.x,
						mirrorFinalPoint.y, paint);
			}

		}
	}

	@Override
	public Double getScore() {
		double masterLeg = getDistance(fixedPoint1, finalPoint);
		double leg1 = getDistance(fixedPoint1, user);
		double leg2 = getDistance(fixedPoint2, user);

		double difference1 = 0.25 * Math.abs(masterLeg - leg1);// /masterLeg;
		double difference2 = 0.25 * Math.abs(masterLeg - leg2);// /masterLeg;

		// double score = Math.ceil(10.0 * (difference1 + difference2));
		double score = difference1 + difference2;

		if (score > MAX_SCORE)
			score = MAX_SCORE;

		return score;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (user != null) {
			user.x = (int) e.getX();
			user.y = (int) e.getY();
		} else {
			user = new Point((int) e.getX(), (int) e.getY());
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		submitted = true;

		double score = getScore();
		String s = "Score = " + score;
		Log.d("Game", s);
		Toast.makeText(con, s, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
		if (user != null) {
			user.x -= (int) dx;
			user.y -= (int) dy;
			if (user.x < 0)
				user.x = 0;
			else if (user.x > width)
				user.x = width;
			if (user.y < 0)
				user.y = 0;
			else if (user.y > height)
				user.y = height;
		} else {
			user = new Point((int) e1.getX(), (int) e1.getY());
			// pointSet = true;
		}
		return true;
	}

	@Override
	public String getInstructions() {
		return "Make an equilateral triangle.";
	}

}
