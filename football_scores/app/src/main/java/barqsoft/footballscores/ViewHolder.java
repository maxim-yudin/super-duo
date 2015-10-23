package barqsoft.footballscores;

import android.view.View;
import android.widget.TextView;

class ViewHolder {
    public final TextView home_name;
    public final TextView away_name;
    public final TextView score;
    public final TextView date;
    public int match_id;

    public ViewHolder(View view) {
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
    }
}
