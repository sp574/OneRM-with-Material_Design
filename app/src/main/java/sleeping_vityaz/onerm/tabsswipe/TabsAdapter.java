package sleeping_vityaz.onerm.tabsswipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {

    final private int NUM_TABS = 3;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
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
            case 0: return "Calculator";
            case 1: return "Percentage";
            case 2: return "History";
            default:return null;
        }
    }
}