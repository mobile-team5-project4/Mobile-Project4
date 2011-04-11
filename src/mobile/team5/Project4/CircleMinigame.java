package mobile.team5.Project4;

import java.util.Random;

import mobile.team5.Project4.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;

public class CircleMinigame extends Minigame {
	private ShapeDrawable circle;
	private Point userPoint;
	private Point centerPoint;
	boolean selected;
	Bitmap reticle;

	public CircleMinigame(Context con, int width, int height) {
		Random rand = new Random();

		reticle = BitmapFactory.decodeResource(con.getResources(),
				R.drawable.squarereticle);

		int minRad = (int) (width * .25);
		int maxRad = (int) (height * .5);
		int rad = rand.nextInt(maxRad - minRad) + minRad;

		Rect bounds = new Rect(0, 0, width, height);
		bounds.bottom -= minRad;
		bounds.top += minRad;
		bounds.left += minRad;
		bounds.right -= minRad;

		int x = rand.nextInt(bounds.right - bounds.left) + bounds.left;
		int y = rand.nextInt(bounds.bottom - bounds.top) + bounds.top;
		centerPoint = new Point(x, y);

		OvalShape oval = new OvalShape();
		// oval.resize(width, height);

		circle = new ShapeDrawable(oval);
		circle.setBounds(x - rad, y + rad, x + rad, y - rad);
	}

	@Override
	public void gameDraw(Canvas c) {
		circle.draw(c);
		if (selected) {
			c.drawBitmap(reticle, userPoint.x, userPoint.y, null);
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
	public boolean selectPoint(MotionEvent e) {
		userPoint.x = e.getX();
		userPoint.y = e.getY();
		selected = true;
		return true;
	}

}
