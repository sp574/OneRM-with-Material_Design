package sleeping_vityaz.onerm.tabsswipe;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import sleeping_vityaz.onerm.R;

public class TabsAdapter extends FragmentPagerAdapter {

    private Context c;
    final private int NUM_TABS = 3;

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm); this.c = c;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CalculatorFragment();
            case 1: return new PercentageFragment();
            case 2: return new HistoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle (int position) {

        switch (position) {
            case 0: return c.getString(R.string.ta_calculator);
            case 1: return c.getString(R.string.ta_percentage);
            case 2: return c.getString(R.string.ta_history);
            default:return null;
        }
    }
}