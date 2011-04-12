package mobile.team5.Project4;

import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class RightAngleMinigame extends Minigame {
	private Point[] startLine;
	private Point[] currentLine;
	private boolean pointSet = false;

	public RightAngleMinigame(Context con, int width, int height) {
		super(con, width, height);
		Random rand = new Random();

		startLine = new Point[2];
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
		startLine[0] = new Point(x, y);
		currentLine[0] = new Point(x, y);

		x = rand.nextInt(2 * len) - len + x;
		y = (int) -(Math.sqrt(Math.pow(len, 2)
				- Math.pow(startLine[0].x - x, 2)) - startLine[0].y);
		startLine[1] = new Point(x, y);
	}

	@Override
	public void gameDraw(Canvas c) {
		// TODO Auto-generated method stub
		c.drawLine(startLine[0].x, startLine[0].y, startLine[1].x,
				startLine[1].y, new Paint(Paint.ANTI_ALIAS_FLAG));

		if (pointSet)
			c.drawLine(currentLine[0].x, currentLine[0].y, currentLine[1].x,
					currentLine[1].y, new Paint(Paint.ANTI_ALIAS_FLAG));
	}

	@Override
	public Double getScore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		currentLine[1] = new Point((int) e.getX(), (int) e.getY());
		pointSet = true;

		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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
