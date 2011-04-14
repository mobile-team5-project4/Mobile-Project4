package mobile.team5.Project4;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback,
		OnGestureListener, OnDoubleTapListener {

	GameLoopThread _thread;
	Minigame minigame;
	Context context;

	GestureDetector gd;

	public Game(Context con) {
		super(con);
		getHolder().addCallback(this);
		_thread = new GameLoopThread(getHolder(), this);
		setFocusable(true);

		gd = new GestureDetector(this);
		gd.setOnDoubleTapListener(this);
		context = con;
	}

	public void init() {
		// minigame = new CircleMinigame(context, getWidth(), getHeight());
		// minigame = new RightAngleMinigame(context, getWidth(), getHeight());
		minigame = new BisectAngleMinigame(context, getWidth(), getHeight());
	}

	public Double getScore() {
		return minigame.getScore();
	}

	public void updateVideo(Canvas c) {
		minigame.gameDraw(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("Game", "changed.");
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		init();
		_thread.setRunning(true);
		_thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gd.onTouchEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		minigame.onScroll(e1, e2, distanceX, distanceY);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;

	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		minigame.onDoubleTap(e);
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		minigame.onSingleTapConfirmed(e);
		return false;
	}
}
