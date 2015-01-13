package sleeping_vityaz.onerm.records_util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;

/**
 * Created by naja-ox on 1/12/15.
 */
public class ArraySwipeAdapterSample<T> extends ArraySwipeAdapter {

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yy", Locale.US);

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_entry, parent, false);
        }

        RecordObject recordObject = (RecordObject) getItem(position);

        TextView recordsID = (TextView) convertView.findViewById(R.id.key_id);

        TextView weight = (TextView) convertView.findViewById(R.id.weight);
        TextView reps = (TextView) convertView.findViewById(R.id.reps);
        TextView oneRM = (TextView) convertView.findViewById(R.id.onerm);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        newFormat.setLenient(false);
        try {
            date.setText(newFormat.format(oldFormat.parse(recordObject.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weight.setText("Weight lifted "+recordObject.getWeight());
        reps.setText(("Reps performed "+recordObject.getReps()));
        oneRM.setText("1RM: "+recordObject.getOneRM());

        return convertView;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}