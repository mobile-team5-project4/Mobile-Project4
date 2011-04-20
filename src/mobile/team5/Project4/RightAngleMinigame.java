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

public class RightAngleMinigame extends Minigame {
	private Point[] startLine;
	private Point[] currentLine;
	private Point[] winningLine;
	private boolean pointSet = false;
	private boolean submitted = false;
	private int len;

	public RightAngleMinigame(Context con, int width, int height) {
		super(con, width, height);
		Random rand = new Random();

		startLine = new Point[2];
		currentLine = new Point[2];
		winningLine = new Point[2];

		int minLen = (int) (width * .25);
		int maxLen = (int) (width * .5);
		len = rand.nextInt(maxLen - minLen) + minLen;

		Rect bounds = new Rect(0, 0, width, height);
		bounds.bottom -= len;
		bounds.top += len;
		bounds.left += len;
		bounds.right -= len;

		int x = rand.nextInt(bounds.right - bounds.left) + bounds.left;
		int y = rand.nextInt(bounds.bottom - bounds.top) + bounds.top;
		startLine[0] = new Point(x, y);
		currentLine[0] = new Point(x, y);
		winningLine[0] = new Point(x, y);

		x = rand.nextInt(2 * len) - len + x;
		if (x < 0)
			x = 0;
		else if (x > width)
			x = width;

		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(startLine[0].x - x, 2))) - startLine[0].y);
		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;
		startLine[1] = new Point(x, y);

		x = rand.nextInt(2 * len) - len + currentLine[0].x;
		if (x < 0)
			x = 0;
		else if (x > width)
			x = width;
		y = (int) -(Math.sqrt(Math.abs(Math.pow(len, 2)
				- Math.pow(startLine[0].x - x, 2))) - startLine[0].y);
		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;
		currentLine[1] = new Point(x, y);

	}

	@Override
	public void gameDraw(Canvas c) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2.0f);
		paint.setColor(Color.BLACK);

		c.drawLine(startLine[0].x, startLine[0].y, startLine[1].x,
				startLine[1].y, paint);

		// if (pointSet)
		paint.setColor(Color.BLUE);
		c.drawLine(currentLine[0].x, currentLine[0].y, currentLine[1].x,
				currentLine[1].y, paint);

		if (submitted) {
			paint.setColor(Color.GREEN);
			c.drawLine(winningLine[0].x, winningLine[0].y, winningLine[1].x,
					winningLine[1].y, paint);

		}
	}

	@Override
	public Double getScore() {
		double slope1 = ((double) (startLine[1].y - startLine[0].y))
				/ (startLine[1].x - startLine[0].x);
		double slope2 = ((double) (currentLine[1].y - currentLine[0].y))
				/ (currentLine[1].x - currentLine[0].x);
		double arcTan = Math.abs(slope1 - slope2) / (1 + slope1 * slope2);
		double angle = Math.abs(Math.toDegrees(Math.atan(arcTan)));
		angle = Math.abs(angle - 90) / 90;
		angle = Math.ceil(angle * MAX_SCORE);

		slope1 = -1 / slope1;

		int x;
		if (currentLine[1].x > currentLine[0].x)
			x = startLine[0].x + len;
		else
			x = startLine[0].x - len;
		int y = (int) (slope1 * (x - startLine[0].x) + startLine[0].y);
		winningLine[1] = new Point(x, y);
		submitted = true;
		return angle;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		currentLine[1] = new Point((int) e.getX(), (int) e.getY());
		// pointSet = true;

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
		} /*
		 * else { // currentLine[1] = new Point((int) e1.getX(), (int)
		 * e1.getY()); // pointSet = true; }
		 */
		return true;
	}

	@Override
	public String getInstructions() {
		return "Form a right angle.";
	}

}
