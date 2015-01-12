package sleeping_vityaz.onerm.tabsswipe;

import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;
import sleeping_vityaz.onerm.records_util.RecordArrayAdapter;
import sleeping_vityaz.onerm.records_util.RecordObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by naja-ox
 */
public class HistoryFragment extends Fragment {

    public RecordArrayAdapter adapter;
    View rootView = null;
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

        if (isVisibleToUser){
            loadScreen();
        }
        else
            Log.d("MyFragment", "Fragment is not visible.");
    }

    private void loadScreen(){
        dbTools = DBTools.getInstance(this.getActivity());
        List<RecordObject> recordsList = dbTools.getAllRecords();
        if (recordsList.size() != 0) {
            ListView listView = (ListView) rootView.findViewById(R.id.records_list);
            adapter = new RecordArrayAdapter(this.getActivity(), recordsList);
            listView.setAdapter(adapter);
        }
    }


}
