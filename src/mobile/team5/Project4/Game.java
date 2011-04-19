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
import android.widget.Toast;

public class Game extends SurfaceView implements SurfaceHolder.Callback,
		OnGestureListener, OnDoubleTapListener {
	final static private int NUM_GAMES = 5;
	final static private int NUM_ROUNDS = 2;
	GameLoopThread _thread;
	Minigame minigame;
	Context context;
	int curGame;
	int curRound;
	boolean enabled = true;
	Double scores[][] = new Double[NUM_GAMES][NUM_ROUNDS];

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
		curGame = 0;
		curRound = 0;
	}

	private void timer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				curGame++;

				if (curGame == NUM_GAMES) {
					curGame = 0;
					curRound++;
				}

				enabled = true;

				switchGame();
			}
		}, 3000);
	}

	private void endGame() {
		Double score = 0.0;
		for (Double[] game : scores) {
			Double ave = 0.0;

			for (Double round : game) {
				ave += round;
			}

			ave = ave / NUM_ROUNDS;
			score += ave;
		}

		Toast.makeText(context, "Your final score is: " + score.toString(),
				Toast.LENGTH_LONG).show();
	}

	private void switchGame() {
		switch (curGame) {
		case 0:
			minigame = new CircleMinigame(context, getWidth(), getHeight());
			break;
		case 1:
			minigame = new RightAngleMinigame(context, getWidth(), getHeight());
			break;
		case 2:
			minigame = new BisectAngleMinigame(context, getWidth(), getHeight());
			break;
		case 3:
			minigame = new TriangleMinigame(context, getWidth(), getHeight());
			break;
		case 4:
			minigame = new ColorMinigame(context, getWidth(), getHeight());
			break;
		}
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
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		init();
		switchGame();
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
		minigame.onScroll(e1, e2, distanceX, distanceY);
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
		minigame.onDoubleTap(e);
		scores[curGame][curRound] = getScore();
		enabled = false;
		if (curGame == NUM_GAMES - 1 && curRound == NUM_ROUNDS - 1)
			endGame();
		else
			timer();
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		minigame.onSingleTapConfirmed(e);
		return false;
	}
}
