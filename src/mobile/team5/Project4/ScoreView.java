package mobile.team5.Project4;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoreView extends TableLayout {

	public ScoreView(Context context, Double[][] scores, String[] names,
			int numRounds) {
		super(context);
		// TODO Auto-generated constructor stub
		setBackgroundColor(Color.WHITE);

		TableRow header = new TableRow(context);
		TextView tvGame = new TextView(context);
		tvGame.setText("Game name");
		tvGame.setTextColor(Color.BLACK);
		header.addView(tvGame);

		for (int x = 0; x < numRounds; x++) {
			TextView tvRound = new TextView(context);
			tvRound.setText("Round \nnumber " + (x + 1));
			tvRound.setTextColor(Color.BLACK);
			header.addView(tvRound);
		}

		TextView tvAves = new TextView(context);
		tvAves.setText("Game averages");
		tvAves.setTextColor(Color.BLACK);
		header.addView(tvAves);

		addView(header);

		Double score = 0.0;
		int x = 0;
		for (Double[] game : scores) {
			TableRow newRow = new TableRow(context);

			TextView newText = new TextView(context);
			newText.setText(names[x++]);
			newText.setTextSize(12);
			newText.setTextColor(Color.BLACK);

			newRow.addView(newText);

			Double ave = 0.0;

			for (Double round : game) {
				TextView tvScore = new TextView(context);
				tvScore.setText(String.format("%.3f", round));
				tvScore.setTextSize(12);
				tvScore.setTextColor(Color.BLACK);

				newRow.addView(tvScore);

				ave += round;
			}

			ave = ave / numRounds;
			score += ave;

			TextView tvAve = new TextView(context);
			tvAve.setText(String.format("%.3f", ave));
			tvAve.setTextColor(Color.BLACK);
			newRow.addView(tvAve);

			addView(newRow);
		}

		TableRow footer = new TableRow(context);
		TextView tvTotal = new TextView(context);
		tvTotal.setText("Total score");
		tvTotal.setTextColor(Color.BLACK);
		footer.addView(tvTotal);

		for (x = 0; x < numRounds; x++) {
			TextView tvRound = new TextView(context);
			tvRound.setText("");
			header.addView(tvRound);
		}

		TextView tvTotalScore = new TextView(context);
		tvTotalScore.setText(String.format("%.3f", score));
		tvTotalScore.setTextColor(Color.BLACK);
		footer.addView(tvTotalScore);

		addView(footer);
	}
}
