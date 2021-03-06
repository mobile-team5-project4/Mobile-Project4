package mobile.team5.Project4;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * This minigame shows the user a color and has them match the color on a color
 * wheel.
 * 
 * @author Michael Longchamps
 * 
 */
public class ColorMinigame extends Minigame {

	private Paint mPaint;
	private Paint mCenterPaint;
	private Paint mWinColor;
	private final int[] mColors;

	private static int CENTER_X;
	private static int CENTER_Y;
	private static final int CENTER_RADIUS = 32;
	private static final float PI = 3.1415926f;

	private boolean mTrackingCenter;
	private boolean mHighlightCenter;

	private Random rand;

	ColorMinigame(final Context con, int width, int height) {
		super(con, width, height);

		CENTER_X = width / 2;
		CENTER_Y = width / 2;

		rand = new Random();

		mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF,
				0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
		Shader s = new SweepGradient(0, 0, mColors, null);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setShader(s);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(32);

		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setColor(randColor());
		mCenterPaint.setStrokeWidth(5);

		mWinColor = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWinColor.setColor(randColor());
		mWinColor.setTextAlign(Align.CENTER);
		mWinColor.setTextSize(24);
	}

	@Override
	public void gameDraw(Canvas canvas) {
		float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;

		canvas.translate(CENTER_X, CENTER_X);

		canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
		canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

		if (mTrackingCenter) {
			int c = mCenterPaint.getColor();
			mCenterPaint.setStyle(Paint.Style.STROKE);

			if (mHighlightCenter) {
				mCenterPaint.setAlpha(0xFF);
			} else {
				mCenterPaint.setAlpha(0x80);
			}
			canvas.drawCircle(0, 0,
					CENTER_RADIUS + mCenterPaint.getStrokeWidth(), mCenterPaint);

			mCenterPaint.setStyle(Paint.Style.FILL);
			mCenterPaint.setColor(c);
		}
		canvas.drawText("MATCH THIS COLOR", 0, 2 * r, mWinColor);
	}

	@Override
	public Double getScore() {
		return Double.valueOf(compare(mCenterPaint.getColor(),
				mWinColor.getColor()));
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		float x = event.getX() - CENTER_X;
		float y = event.getY() - CENTER_Y;
		boolean inCenter = java.lang.Math.sqrt(x * x + y * y) <= CENTER_RADIUS;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTrackingCenter = inCenter;
			if (inCenter) {
				mHighlightCenter = true;
				break;
			}
		case MotionEvent.ACTION_MOVE:
			if (mTrackingCenter) {
				if (mHighlightCenter != inCenter) {
					mHighlightCenter = inCenter;
				}
			} else {
				float angle = (float) java.lang.Math.atan2(y, x);
				// need to turn angle [-PI ... PI] into unit [0....1]
				float unit = angle / (2 * PI);
				if (unit < 0) {
					unit += 1;
				}
				mCenterPaint.setColor(interpColor(mColors, unit));
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTrackingCenter) {
				if (inCenter) {
				}
				mTrackingCenter = false; // so we draw w/o halo
			}
			break;
		}
		return true;
	}

	private double compare(int chosen, int win) {
		int cRed = (0x00FF0000 & chosen) >> 16;
		int wRed = (0x00FF0000 & win) >> 16;
		int cGreen = (0x0000FF00 & chosen) >> 8;
		int wGreen = (0x0000FF00 & win) >> 8;
		int cBlue = (0x000000FF & chosen);
		int wBlue = (0x000000FF & win);

		double score = ((double) Math.abs(cRed - wRed) / 255
				+ (double) Math.abs(cGreen - wGreen) / 255 + (double) Math
				.abs(cBlue - wBlue) / 255) / 3;
		return score * MAX_SCORE;
	}

	private int ave(int s, int d, float p) {
		return s + java.lang.Math.round(p * (d - s));
	}

	private int interpColor(int colors[], float unit) {
		if (unit <= 0) {
			return colors[0];
		}
		if (unit >= 1) {
			return colors[colors.length - 1];
		}

		float p = unit * (colors.length - 1);
		int i = (int) p;
		p -= i;

		// now p is just the fractional part [0...1) and i is the index
		int c0 = colors[i];
		int c1 = colors[i + 1];
		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0), Color.red(c1), p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0), Color.blue(c1), p);

		return Color.argb(a, r, g, b);
	}

	private int randColor() {
		Float f = rand.nextFloat();
		return interpColor(mColors, f);
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
		float x = e2.getX() - CENTER_X;
		float y = e2.getY() - CENTER_Y;
		boolean inCenter = java.lang.Math.sqrt(x * x + y * y) <= CENTER_RADIUS;

		switch (e2.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTrackingCenter = inCenter;
			if (inCenter) {
				mHighlightCenter = true;
				break;
			}
		case MotionEvent.ACTION_MOVE:
			if (mTrackingCenter) {
				if (mHighlightCenter != inCenter) {
					mHighlightCenter = inCenter;
				}
			} else {
				float angle = (float) java.lang.Math.atan2(y, x);
				// need to turn angle [-PI ... PI] into unit [0....1]
				float unit = angle / (2 * PI);
				if (unit < 0) {
					unit += 1;
				}
				mCenterPaint.setColor(interpColor(mColors, unit));
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTrackingCenter) {
				if (inCenter) {
				}
				mTrackingCenter = false; // so we draw w/o halo
			}
			break;
		}
		return true;
	}

	@Override
	public String getInstructions() {
		return "Match the color shown.";
	}

}
