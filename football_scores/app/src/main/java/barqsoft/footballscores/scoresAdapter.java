package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ScoresAdapter extends CursorAdapter {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCH_DAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCH_TIME = 2;

    private int selectedDetailMatchId = 0;

    public ScoresAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        final String homeTeamName = cursor.getString(COL_HOME);
        mHolder.home_name.setContentDescription(homeTeamName);
        mHolder.home_name.setText(homeTeamName);
        mHolder.home_name.setCompoundDrawablesWithIntrinsicBounds(0,
                Utilities.getTeamCrestByTeamName(context, homeTeamName), 0, 0);
        final String awayTeamName = cursor.getString(COL_AWAY);
        mHolder.away_name.setContentDescription(awayTeamName);
        mHolder.away_name.setText(awayTeamName);
        mHolder.away_name.setCompoundDrawablesWithIntrinsicBounds(0,
                Utilities.getTeamCrestByTeamName(context, awayTeamName), 0, 0);
        final String date = cursor.getString(COL_MATCH_TIME);
        mHolder.date.setContentDescription(date);
        mHolder.date.setText(date);
        final String score = Utilities.getScores(context, cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
        mHolder.score.setContentDescription(score);
        mHolder.score.setText(score);
        mHolder.match_id = cursor.getInt(COL_ID);

        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        View v = vi.inflate(R.layout.detail_fragment, container, false);
        if (mHolder.match_id == selectedDetailMatchId) {
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView matchday_textview = (TextView) v.findViewById(R.id.matchday_textview);
            String match = Utilities.getMatchDay(context, cursor.getInt(COL_MATCH_DAY),
                    cursor.getInt(COL_LEAGUE));
            matchday_textview.setContentDescription(match);
            matchday_textview.setText(match);
            TextView league_textview = (TextView) v.findViewById(R.id.league_textview);
            String league = Utilities.getLeague(context, cursor.getInt(COL_LEAGUE));
            league_textview.setContentDescription(league);
            league_textview.setText(league);
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action)
                    context.startActivity(createShareMatchIntent(context.getString(R.string.share_match,
                            mHolder.home_name.getText(), mHolder.score.getText(), mHolder.away_name.getText())));
                }
            });
        } else {
            container.removeAllViews();
        }
    }

    private Intent createShareMatchIntent(String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            //noinspection deprecation
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return shareIntent;
    }

    public int getSelectedDetailMatchId() {
        return selectedDetailMatchId;
    }

    public void setSelectedDetailMatchId(int selectedDetailMatchId) {
        this.selectedDetailMatchId = selectedDetailMatchId;
    }
}
