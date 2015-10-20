package barqsoft.footballscores;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainFragment extends Fragment {
    private static final String CURRENT_FRAGMENT = "current_fragment";
    private static final int DEFAULT_FRAGMENT = 2;

    private static final int NUM_FRAGMENTS = 5;

    private static final int ONE_DAY_IN_MILLIS = 86400000;

    private ViewPager vpScoreDays;

    private final DayScoreFragment[] dayScoreFragments = new DayScoreFragment[5];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        vpScoreDays = (ViewPager) rootView.findViewById(R.id.vpScoreDays);
        ScoreDaysPagerAdapter scoreDaysPagerAdapter = new ScoreDaysPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < NUM_FRAGMENTS; i++) {
            dayScoreFragments[i] = DayScoreFragment.newInstance(System.currentTimeMillis() + ((i - 2) * ONE_DAY_IN_MILLIS));
        }
        vpScoreDays.setAdapter(scoreDaysPagerAdapter);
        vpScoreDays.setCurrentItem((savedInstanceState != null) ?
                savedInstanceState.getInt(CURRENT_FRAGMENT) : DEFAULT_FRAGMENT);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, vpScoreDays.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private class ScoreDaysPagerAdapter extends FragmentStatePagerAdapter {
        @Override
        public DayScoreFragment getItem(int i) {
            return dayScoreFragments[i];
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }

        public ScoreDaysPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getActivity(), getItem(position).getDateInMillis());
        }

        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.
            Calendar gc = GregorianCalendar.getInstance();
            int currentJulianDay = gc.get(GregorianCalendar.DAY_OF_YEAR);
            gc.setTime(new Date(dateInMillis));
            int julianDay = gc.get(GregorianCalendar.DAY_OF_YEAR);

            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);
            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);
            } else {
                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
