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

import barqsoft.footballscores.service.myFetchService;

public class DayScoreFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String SELECTED_DETAIL_MATCH_ID = "selected_detail_match_id";
    private static final String DATE_IN_MILLIS = "dateInMillis";

    private ScoresAdapter mAdapter;
    private static final int SCORES_LOADER = 0;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static DayScoreFragment newInstance(long dateInMillis) {
        DayScoreFragment fragment = new DayScoreFragment();

        Bundle args = new Bundle();
        args.putLong(DATE_IN_MILLIS, dateInMillis);
        fragment.setArguments(args);

        return fragment;
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
        final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity(), null, 0);
        if (savedInstanceState != null) {
            mAdapter.setSelectedDetailMatchId(savedInstanceState.getInt(SELECTED_DETAIL_MATCH_ID));
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
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{getDate()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
