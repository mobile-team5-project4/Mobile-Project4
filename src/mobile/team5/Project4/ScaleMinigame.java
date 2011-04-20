package mobile.team5.Project4;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class ScaleMinigame extends Minigame {
	
	private final int CIRCLE = 0;
	private final int RECTANGLE = 1;
	
	private ShapeDrawable shape, userShape;
	private int shapeType;
	private Random rand;

	ScaleMinigame(Context con, int width, int height) {
		super(con, width, height);
		
		rand = new Random();

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

		switch(rand.nextInt(2)) {
			case CIRCLE:
				shapeType = CIRCLE;
				OvalShape oval = new OvalShape();
				OvalShape userOval = new OvalShape();
				oval.resize(width, height);
				userOval.resize(width, height);
				shape = new ShapeDrawable(oval);
				userShape = new ShapeDrawable(userOval); // needs a separate object
				break;
			case RECTANGLE:
				shapeType = RECTANGLE;
				RectShape rect = new RectShape();
				rect.resize(width, height);
				RectShape userRect = new RectShape(); 
				userRect.resize(width, height);
				shape = new ShapeDrawable(rect);
				userShape = new ShapeDrawable(userRect); // needs a separate object
				break;
		}

		shape.setBounds(x - rad, y - rad, x + rad, y + rad);
		userShape.setBounds(x - rad, y - rad, x + rad, y + rad);
		Paint p = shape.getPaint();
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2.0f);
		p = userShape.getPaint();
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2.0f);
		p.setColor(Color.BLUE);
	}

	@Override
	public void gameDraw(Canvas c) {
		userShape.draw(c);
		shape.draw(c);
	}

	@Override
	public Double getScore() {
		Double area, userArea, score;
		if(shapeType == CIRCLE) {
			area = Math.PI * Math.pow(shape.getShape().getWidth() / 2, 2);
			userArea = Math.PI * Math.pow(userShape.getShape().getWidth() / 2, 2);
		}
		else {
			area = (double) (shape.getShape().getWidth() * shape.getShape().getHeight());
			userArea = (double) (userShape.getShape().getWidth() * userShape.getShape().getHeight());
		}
		score = Math.abs((userArea - area / 2) / (area / 2) ) * MAX_SCORE;
		if(score > MAX_SCORE)
			score = (double) MAX_SCORE;
		return score;
	}

	@Override
	public String getInstructions() {
		return "Halve the area.";
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
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
		if(e2.getAction() == MotionEvent.ACTION_MOVE) {
			int scale = 2;
			Rect r = userShape.getBounds();
			if(e2.getX() > e1.getX() || e2.getY() < e1.getY())
				scale *= -1;
			userShape.setBounds(r.left + scale, r.top + scale, r.right - scale, r.bottom - scale);
		}
		return true;
	}
	
}
