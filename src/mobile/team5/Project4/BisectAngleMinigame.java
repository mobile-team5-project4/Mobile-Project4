package mobile.team5.Project4;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class BisectAngleMinigame extends Minigame {
	private Point[] line1;
	private Point[] line2;
	private Point[] currentLine;
	private boolean pointSet = false;

	BisectAngleMinigame(Context con, int width, int height) {
		super(con, width, height);
		Random rand = new Random();

		line1 = new Point[2];
		line2 = new Point[2];
		currentLine = new Point[2];

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
		Point joint = new Point(x, y);

		line1[0] = joint;
		line2[0] = joint;
		currentLine[0] = joint;

		x = rand.nextInt(2 * len) - len + x;
		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(line1[0].x - x, 2))) - line1[0].y);
		line1[1] = new Point(x, y);

		if (line1[1].x < width / 2) {
			x += rand.nextInt(maxLen - minLen) + minLen;
		} else {
			x -= rand.nextInt(maxLen - minLen) + minLen;
		}

		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(line2[0].x - x, 2))) - line2[0].y);
		line2[1] = new Point(x, y);
	}

	@Override
	public void gameDraw(Canvas c) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2.0f);

		c.drawLine(line1[0].x, line1[0].y, line1[1].x, line1[1].y, paint);
		c.drawLine(line2[0].x, line2[0].y, line2[1].x, line2[1].y, paint);

		if (pointSet)
			c.drawLine(currentLine[0].x, currentLine[0].y, currentLine[1].x,
					currentLine[1].y, paint);
	}

	@Override
	public Double getScore() {
		
		return 0.0;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		currentLine[1] = new Point((int) e.getX(), (int) e.getY());
		pointSet = true;

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
		if (currentLine[1] != null) {
			currentLine[1].x -= (int) dx;
			currentLine[1].y -= (int) dy;
			if (currentLine[1].x < 0)
				currentLine[1].x = 0;
			else if (currentLine[1].x > width)
				currentLine[1].x = width;
			if (currentLine[1].y < 0)
				currentLine[1].y = 0;
			else if (currentLine[1].y > height)
				currentLine[1].y = height;
		} else {
			currentLine[1] = new Point((int) e1.getX(), (int) e1.getY());
			pointSet = true;
		}
		return true;
	}

}
