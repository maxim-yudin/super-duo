package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build.VERSION_CODES;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract.ScoresTable;
import barqsoft.footballscores.DayScoreFragment;
import barqsoft.footballscores.Match;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.Utilities;

@TargetApi(VERSION_CODES.JELLY_BEAN)
public class ScoresWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoresRemoteViewsFactory(getApplicationContext());
    }

    private static class ScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Match> matchList = new ArrayList<>();

        private final Context context;

        public ScoresRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            Date fragmentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String matchDate = format.format(fragmentDate);

            Cursor cursor = context.getContentResolver().query(
                    ScoresTable.buildScoreWithDate(), null, null,
                    new String[]{matchDate}, null);
            if (cursor == null || cursor.getCount() < 1) {
                matchList = new ArrayList<>();
            } else {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(ScoresAdapter.COL_ID);
                    String homeName = cursor.getString(ScoresAdapter.COL_HOME);
                    String awayName = cursor.getString(ScoresAdapter.COL_AWAY);
                    int homeGoals = cursor.getInt(ScoresAdapter.COL_HOME_GOALS);
                    int awayGoals = cursor.getInt(ScoresAdapter.COL_AWAY_GOALS);
                    String time = cursor.getString(ScoresAdapter.COL_MATCH_TIME);
                    matchList.add(new Match(id, homeName, awayName, homeGoals, awayGoals, time));
                }
                cursor.close();
            }
        }

        @Override
        public void onDestroy() {
            matchList.clear();
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public int getCount() {
            return matchList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Match match = matchList.get(position);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.match_item);

            views.setTextViewText(R.id.home_name, match.homeTeam);
            views.setContentDescription(R.id.home_name, match.homeTeam);
            views.setTextViewCompoundDrawables(R.id.home_name, 0,
                    Utilities.getTeamCrestByTeamName(context, match.homeTeam), 0, 0);

            views.setTextViewText(R.id.away_name, match.awayTeam);
            views.setContentDescription(R.id.home_name, match.awayTeam);
            views.setTextViewCompoundDrawables(R.id.away_name, 0,
                    Utilities.getTeamCrestByTeamName(context, match.awayTeam), 0, 0);

            views.setTextViewText(R.id.time_textview, match.time);
            views.setContentDescription(R.id.time_textview, match.time);

            String score = Utilities.getScores(context, match.homeGoals, match.awayGoals);
            views.setTextViewText(R.id.score_textview, score);
            views.setContentDescription(R.id.score_textview, score);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(DayScoreFragment.SELECTED_DETAIL_MATCH_ID, match.id);
            views.setOnClickFillInIntent(R.id.llMatch, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}