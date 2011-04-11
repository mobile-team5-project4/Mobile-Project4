package mobile.team5.Project4;

import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class Minigame {
	
	abstract public void gameDraw(Canvas c);
	
	abstract public Double getScore();
	
	abstract public boolean selectPoint(MotionEvent e);
	
	// Will need a series of functions here for each of the gestures
	// we use during all minigames. The Game class can then call within
	// it's respective gesture functions, the minigame version of these,
	// overriden by the subclasses, with minigame.onTouchEvent(), or
	// minigame.onFling(), etc...
}
