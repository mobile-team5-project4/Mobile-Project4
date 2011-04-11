package mobile.team5.Project4;

import android.app.Activity;
import android.os.Bundle;

public class Project4 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Game(this));
    }
}