package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract.ScoresTable;
import barqsoft.footballscores.service.myFetchService;

public class DayScoreFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String SELECTED_DETAIL_MATCH_ID = "selected_detail_match_id";
    private static final String DATE_IN_MILLIS = "dateInMillis";

    private ScoresAdapter mAdapter;
    private static final int SCORES_LOADER = 0;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private ListView score_list;

    boolean isScrollToSelectedMatch = false;

    public static DayScoreFragment newInstance(long dateInMillis, int selectedMatchId) {
        DayScoreFragment fragment = new DayScoreFragment();

        Bundle args = new Bundle();
        args.putLong(DATE_IN_MILLIS, dateInMillis);
        args.putInt(SELECTED_DETAIL_MATCH_ID, selectedMatchId);
        fragment.setArguments(args);

        return fragment;
    }

    public int getSelectedMatchId() {
        if (getArguments() != null) {
            return getArguments().getInt(SELECTED_DETAIL_MATCH_ID);
        }
        return 0;
    }

    public long getDateInMillis() {
        if (getArguments() != null) {
            return getArguments().getLong(DATE_IN_MILLIS, 0);
        }
        return 0;
    }

    private String getDate() {
        Date date = new Date(getDateInMillis());
        return dateFormat.format(date);
    }

    private void updateScores() {
        Intent fetchServiceIntent = new Intent(getActivity(), myFetchService.class);
        getActivity().startService(fetchServiceIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        updateScores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity());
        if (savedInstanceState != null) {
            isScrollToSelectedMatch = false;
            mAdapter.setSelectedDetailMatchId(savedInstanceState.getInt(SELECTED_DETAIL_MATCH_ID));
        } else {
            if (getSelectedMatchId() != 0) {
                isScrollToSelectedMatch = true;
                mAdapter.setSelectedDetailMatchId(getSelectedMatchId());
            }
        }
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selectedMatch = (ViewHolder) view.getTag();
                mAdapter.setSelectedDetailMatchId(selectedMatch.match_id);
                mAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_DETAIL_MATCH_ID, mAdapter.getSelectedDetailMatchId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ScoresTable.buildScoreWithDate(),
                null, null, new String[]{getDate()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int currentMatchPosition = 0;
        int selectedMatchPosition = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(ScoresAdapter.COL_ID) == mAdapter.getSelectedDetailMatchId()) {
                selectedMatchPosition = currentMatchPosition;
            }
            cursor.moveToNext();
            ++currentMatchPosition;
        }
        mAdapter.swapCursor(cursor);
        if (isScrollToSelectedMatch && currentMatchPosition != 0) {
            // scroll to selected match from widget
            score_list.smoothScrollToPosition(selectedMatchPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
