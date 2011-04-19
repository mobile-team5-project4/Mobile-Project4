package mobile.team5.Project4;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class TriangleMinigame extends Minigame {
	private Point ptA;
	private Point ptB;
	private Point ptC;
	private boolean pointSet = false;
	private Point userPoint;

	public TriangleMinigame(Context con, int width, int height) {
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
		ptA = new Point(x, y);

		x = rand.nextInt(2 * len) - len + x;
		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(ptA.x - x, 2))) - ptA.y);
		ptB = new Point(x, y);

		if (ptB.x < width / 2) {
			do {
				x += rand.nextInt(maxLen - minLen) + minLen;
				if (x < 0)
					x = 0;
				else if (x > height)
					x = height;
			} while (Math.abs(x - ptA.x) < minLen);
		} else {
			do {
				x -= rand.nextInt(maxLen - minLen) + minLen;
				if (x < 0)
					x = 0;
				else if (x > height)
					x = height;
			} while (Math.abs(x - ptA.x) < minLen);
		}
		if (ptA.y < height / 2) {
			y = ptA.y + rand.nextInt(maxLen - minLen) + minLen;
		} else {
			y = ptA.y - rand.nextInt(maxLen - minLen) + minLen;
		}

		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;
		
		ptC = new Point(x, y);
	}

	@Override
	public void gameDraw(Canvas c) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2.0f);
		c.drawLine(ptA.x, ptA.y, ptB.x, ptB.y, paint);
		c.drawLine(ptC.x, ptC.y, ptB.x, ptB.y, paint);
		c.drawLine(ptA.x, ptA.y, ptC.x, ptC.y, paint);

		if (pointSet) {
			c.drawBitmap(reticle, userPoint.x - (reticle.getWidth() / 2),
					userPoint.y - (reticle.getHeight() / 2), null);
		}
	}

	@Override
	public Double getScore() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (userPoint != null) {
			userPoint.x = (int) e.getX();
			userPoint.y = (int) e.getY();
		} else {
			userPoint = new Point((int) e.getX(), (int) e.getY());
			pointSet = true;
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		double score = getScore();
		String s = "Score = " + score;
		Log.d("Game", s);
		Toast.makeText(con, s, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
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
			pointSet = true;
		}
		return true;
	}
}
