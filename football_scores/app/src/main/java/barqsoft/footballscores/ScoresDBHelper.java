package barqsoft.footballscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.DatabaseContract.ScoresTable;

class ScoresDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE + " ("
                + ScoresTable._ID + " INTEGER PRIMARY KEY,"
                + ScoresTable.DATE_COL + " TEXT NOT NULL,"
                + ScoresTable.TIME_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_COL + " TEXT NOT NULL,"
                + ScoresTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.MATCH_ID + " INTEGER NOT NULL,"
                + ScoresTable.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE (" + ScoresTable.MATCH_ID + ") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE);
    }
}
