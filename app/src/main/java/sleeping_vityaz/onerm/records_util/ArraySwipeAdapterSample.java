package sleeping_vityaz.onerm.records_util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;

/**
 * Created by naja-ox
 */
public class ArraySwipeAdapterSample<T> extends ArraySwipeAdapter {

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yy", Locale.getDefault());
    private List<RecordObject> recOBjects;

    public ArraySwipeAdapterSample(Context context, int resource) {
        super(context, resource);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ArraySwipeAdapterSample(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    public ArraySwipeAdapterSample(Context context, int resource, int textViewResourceId, List<RecordObject> objects) {
        super(context, 0, textViewResourceId, objects);
        this.recOBjects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_entry, parent, false);
        }

        final RecordObject recordObject = (RecordObject) getItem(position);

        final TextView recordsID = (TextView) convertView.findViewById(R.id.key_id);

        TextView weight = (TextView) convertView.findViewById(R.id.weight);
        final TextView reps = (TextView) convertView.findViewById(R.id.reps);
        TextView oneRM = (TextView) convertView.findViewById(R.id.onerm);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        newFormat.setLenient(false);
        try {
            date.setText(newFormat.format(oldFormat.parse(recordObject.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recordsID.setText(""+recordObject.getRecordsID());
        weight.setText(getContext().getString(R.string.hf_weight_lifted)+" " + recordObject.getWeight());
        reps.setText((getContext().getString(R.string.hf_reps_performed)+" " + recordObject.getReps()));
        oneRM.setText(getContext().getString(R.string.hf_1rm)+" " + recordObject.getOneRM());


        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_trash);


        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);

        final View finalConvertView = convertView;
        swipeLayout.findViewById(R.id.img_trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView = (ListView) finalConvertView.getParent();
                final int position = listView.getPositionForView(finalConvertView);

                //Toast.makeText(getContext(), "Click Trash", Toast.LENGTH_SHORT).show();

                String keyID = "" + recOBjects.get(position).getRecordsID();
                String weight = "" + recOBjects.get(position).getWeight();
                Log.d("OnClick", "keyID: " + keyID + " Weight " + weight);
                Log.d("OnClick", "Position: " + position);

                DBTools dbTools = DBTools.getInstance(getContext());
                dbTools.deleteRecord(keyID);
                remove(getItem(position));
                notifyDataSetChanged();
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .duration(1000) // make it shorter
                                .text(getContext().getString(R.string.hf_rdeleted)));
            }
        });
        swipeLayout.close();

        return convertView;
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}