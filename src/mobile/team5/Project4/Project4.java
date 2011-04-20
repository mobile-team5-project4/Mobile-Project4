package mobile.team5.Project4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Project4 extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button start = (Button) findViewById(R.id.buttonStart);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setContentView(new Game(Project4.this));
			}
		});
	}
}