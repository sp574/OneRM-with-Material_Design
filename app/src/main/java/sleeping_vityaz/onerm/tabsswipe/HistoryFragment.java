package sleeping_vityaz.onerm.tabsswipe;

import sleeping_vityaz.onerm.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by naja-ox on 1/10/15.
 */
public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.history_layout, container, false);
       // DBTools dbTools = DBTools.getInstance(this.getActivity());




        return rootView;

    }
}
