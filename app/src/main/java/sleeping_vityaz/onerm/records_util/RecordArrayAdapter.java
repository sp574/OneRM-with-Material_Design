package sleeping_vityaz.onerm.records_util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;

/**
 * Created by naja-ox on 1/12/15.
 */
public class RecordArrayAdapter extends ArrayAdapter<RecordObject> {

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yy", Locale.US);

    private LayoutInflater inflater;
    DBTools dbTools = new DBTools(this.getContext());

    public RecordArrayAdapter(Context context, List<RecordObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_entry, parent, false);
        }

        RecordObject recordObject = getItem(position);

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


}
