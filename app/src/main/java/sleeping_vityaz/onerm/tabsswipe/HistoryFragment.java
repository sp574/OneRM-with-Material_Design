package sleeping_vityaz.onerm.tabsswipe;

import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;
import sleeping_vityaz.onerm.records_util.ArraySwipeAdapterSample;
import sleeping_vityaz.onerm.records_util.RecordArrayAdapter;
import sleeping_vityaz.onerm.records_util.RecordObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by naja-ox
 */
public class HistoryFragment extends Fragment {

    public RecordArrayAdapter adapter;
    ArraySwipeAdapterSample mAdapter;
    View rootView;
    DBTools dbTools = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.history_layout, container, false);
        loadScreen();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            loadScreen();
        } else {
            Log.d("MyFragment", "Fragment is not visible.");
        }
    }


    private void loadScreen() {
        dbTools = DBTools.getInstance(this.getActivity());
        final List<RecordObject> recordsList = dbTools.getAllRecords();

        if (recordsList.size() != 0) {
            final ListView listView = (ListView) rootView.findViewById(R.id.records_list);
            mAdapter = new ArraySwipeAdapterSample<String>(this.getActivity(), R.layout.record_entry, R.id.key_id, recordsList);

            listView.setAdapter(mAdapter);

            mAdapter.setMode(SwipeItemMangerImpl.Mode.Single);
            SwipeLayout swipeLayout = (SwipeLayout) getActivity().findViewById(R.id.swipe);

            /*adapter = new RecordArrayAdapter(this.getActivity(), recordsList);
            listView.setAdapter(adapter);*/
        }


    }


}
