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
import barqsoft.footballscores.Match;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;

/**
 * Created by maximyudin on 27.10.15.
 */
@TargetApi(VERSION_CODES.JELLY_BEAN)
public class ScoresWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoresRemoteViewsFactory(getApplicationContext());
    }

    private static class ScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Match> mWidgetItems = new ArrayList<>();

        private Context context;

        public ScoresRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            queryData();
        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Match match = mWidgetItems.get(position);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.match_item);

            views.setTextViewText(R.id.home_name, match.getHomeTeam());
            views.setContentDescription(R.id.home_name, match.getHomeTeam());
            views.setTextViewCompoundDrawables(R.id.home_name, 0,
                    Utilities.getTeamCrestByTeamName(context, match.getHomeTeam()), 0, 0);

            views.setTextViewText(R.id.away_name, match.getAwayTeam());
            views.setContentDescription(R.id.home_name, match.getAwayTeam());
            views.setTextViewCompoundDrawables(R.id.away_name, 0,
                    Utilities.getTeamCrestByTeamName(context, match.getAwayTeam()), 0, 0);

            views.setTextViewText(R.id.date_textview, match.getDate());
            views.setContentDescription(R.id.date_textview, match.getDate());

            String score = Utilities.getScores(context, match.getHomeGoals(), match.getAwayGoals());
            views.setTextViewText(R.id.score_textview, score);
            views.setContentDescription(R.id.score_textview, score);

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

        private void queryData() {
            Date fragmentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String matchDate = format.format(fragmentDate);

            Cursor cursor = context.getContentResolver().query(
                    ScoresTable.buildScoreWithDate(), null, null,
                    new String[]{matchDate}, null);
            if (null == cursor || cursor.getCount() < 1) {
                mWidgetItems = new ArrayList<>();
            } else {
                final int homeIndex = cursor.getColumnIndex(ScoresTable.HOME_COL);
                final int awayIndex = cursor.getColumnIndex(ScoresTable.AWAY_COL);
                final int homeGoalsIndex = cursor.getColumnIndex(ScoresTable.HOME_GOALS_COL);
                final int awayGoalsIndex = cursor.getColumnIndex(ScoresTable.AWAY_GOALS_COL);
                final int dateIndex = cursor.getColumnIndex(ScoresTable.DATE_COL);
                while (cursor.moveToNext()) {
                    String homeName = cursor.getString(homeIndex);
                    String awayName = cursor.getString(awayIndex);
                    int homeGoals = cursor.getInt(homeGoalsIndex);
                    int awayGoals = cursor.getInt(awayGoalsIndex);
                    String date = cursor.getString(dateIndex);
                    mWidgetItems.add(new Match(homeName, awayName, homeGoals, awayGoals, date));
                }
                cursor.close();
            }
        }
    }
}