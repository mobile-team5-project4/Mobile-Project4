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
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * This minigame shows the user a color and has them match the color on a color wheel. 
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
    
	ColorMinigame(final Context con, int width, int height) {
		super(con, width, height);
		
		CENTER_X = width/2;
		CENTER_Y = width/2;
		
        mColors = new int[] {
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
            0xFFFFFF00, 0xFFFF0000
        };
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
		float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;

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
                              CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                              mCenterPaint);

            mCenterPaint.setStyle(Paint.Style.FILL);
            mCenterPaint.setColor(c);
        }
        canvas.drawText("MATCH THIS COLOR", 0, 2*r, mWinColor);
	}

	@Override
	public Double getScore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		float x = event.getX() - CENTER_X;
        float y = event.getY() - CENTER_Y;
        boolean inCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTrackingCenter = inCenter;
                if (inCenter) {
                    mHighlightCenter = true;
                    Toast.makeText(con, Integer.toString(compare(mCenterPaint.getColor(), mWinColor.getColor())), Toast.LENGTH_SHORT).show();
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter;
                    }
                } else {
                    float angle = (float)java.lang.Math.atan2(y, x);
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    float unit = angle/(2*PI);
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
                    mTrackingCenter = false;    // so we draw w/o halo
                }
                break;
        }
        return true;
	}
	
	private int compare(int chosen, int win) {
		//TODO Needs Fixing
		return Math.abs(chosen - win);
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
        int i = (int)p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i+1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }
    
    private int randColor() {
    	Random rand = new Random();
    	Float f = rand.nextFloat();
    	return interpColor(mColors, f);
    }
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
