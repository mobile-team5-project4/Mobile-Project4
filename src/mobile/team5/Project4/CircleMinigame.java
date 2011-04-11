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
	boolean selected;
	Bitmap reticle;

	public CircleMinigame(Canvas c, Context con) {
		super(c);
		Random rand = new Random();
		
		reticle = BitmapFactory.decodeResource(con.getResources(), R.drawable.squarereticle);

		int minRad = (int) (c.getWidth() * .25);
		int maxRad = (int) (c.getWidth() * .5);
		int rad = rand.nextInt(maxRad - minRad) + minRad;
		Rect bounds = c.getClipBounds();
		bounds.bottom -= minRad;
		bounds.top += minRad;
		bounds.left += minRad;
		bounds.right -= minRad;

		int x = rand.nextInt(bounds.right) + bounds.left;
		int y = rand.nextInt(bounds.bottom) + bounds.top;

		OvalShape oval = new OvalShape();
		// oval.resize(width, height);

		circle = new ShapeDrawable(oval);
		circle.setBounds(x - rad, y + rad, x + rad, y - rad);
	}

	@Override
	public void gameDraw(Canvas c) {
		circle.draw(c);
		if(selected) {
			c.drawBitmap(reticle, userPoint.x, userPoint.y, null);
		}

	}

	@Override
	public Double getScore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selectPoint(MotionEvent e) {
		userPoint.x = e.getX();
		userPoint.y = e.getY();
		selected = true;
	}

}
