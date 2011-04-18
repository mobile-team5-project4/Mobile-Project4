package mobile.team5.Project4;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
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
	int curGame;
	Minigame games[];
	boolean enabled = true;

	GestureDetector gd;

	public Game(Context con) {
		super(con);
		getHolder().addCallback(this);
		_thread = new GameLoopThread(getHolder(), this);
		setFocusable(true);

		gd = new GestureDetector(this);
		gd.setOnDoubleTapListener(this);
		context = con;
		games = new Minigame[4];
	}

	public void init() {
		curGame = 0;

		games[0] = new CircleMinigame(context, getWidth(), getHeight());
		games[1] = new RightAngleMinigame(context, getWidth(), getHeight());
		games[2] = new BisectAngleMinigame(context, getWidth(), getHeight());
		games[3] = new TriangleMinigame(context, getWidth(), getHeight());
	}

	private void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				curGame++;
				enabled = true;
			}
		}, 5000);
	}

	public Double getScore() {
		return games[curGame].getScore();
	}

	public void updateVideo(Canvas c) {
		games[curGame].gameDraw(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
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
		if (enabled)
			return gd.onTouchEvent(e);
		else
			return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		games[curGame].onScroll(e1, e2, distanceX, distanceY);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		games[curGame].onDoubleTap(e);
		enabled = false;
		timer();
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		games[curGame].onSingleTapConfirmed(e);
		return false;
	}
}
