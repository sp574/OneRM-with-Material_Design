package sleeping_vityaz.onerm.tabsswipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter {

    final private int NUM_TABS = 2;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CalculatorFragment();
            case 1: return new HistoryFragment();
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
            case 1: return "History";
        }
        return null;
    }

}