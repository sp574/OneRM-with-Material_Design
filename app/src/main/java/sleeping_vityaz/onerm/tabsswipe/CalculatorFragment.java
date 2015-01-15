package sleeping_vityaz.onerm.tabsswipe;


import sleeping_vityaz.onerm.Calculations;
import sleeping_vityaz.onerm.DBTools;
import sleeping_vityaz.onerm.R;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.melnykov.fab.FloatingActionButton;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.EventListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by naja-ox
 */
public class CalculatorFragment extends Fragment {

    // Table Names
    private static final String TABLE = "history_table";

    // Common column names
    private static final String KEY_ID = "col_id";
    private static final String WEIGHT = "col_weight_lifted";
    private static final String REPS = "col_reps";
    private static final String ONERM = "col_onerm";
    private static final String DATE_CREATED = "col_date_created";


    private TextView tv_reps, tv_weight,
            tv_your_onerm,
            tv_onerm_big,
            tv_your_xrm, tv_xrm,
            tv_xrm_big;

    private FrameLayout touchInterceptor;

    ListView listView;
    FloatingActionButton fab;

    private SimpleDateFormat dateFormatter;


    private EditText et_weight;
    private DiscreteSeekBar discreteSeekBar_reps;
    private DiscreteSeekBar discreteSeekBar_allrepmax;
    private double weight;
    private int reps;

    private boolean visible;
    private boolean firstTime;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.calculator_layout, container, false);
        final DBTools dbTools = DBTools.getInstance(this.getActivity());

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());


        findViewsById(rootView);


        et_weight.addTextChangedListener(watch);

        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (et_weight.isFocused()) {
                        Rect outRect = new Rect();
                        et_weight.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            et_weight.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

        tv_reps.setVisibility(TextView.INVISIBLE);
        discreteSeekBar_reps.setVisibility(View.INVISIBLE);
        tv_onerm_big.setVisibility(TextView.INVISIBLE);
        tv_your_onerm.setVisibility(TextView.INVISIBLE);

        discreteSeekBar_allrepmax.setVisibility(View.INVISIBLE);
        tv_your_xrm.setVisibility(TextView.INVISIBLE);
        tv_xrm.setVisibility(TextView.INVISIBLE);
        tv_xrm_big.setVisibility(TextView.INVISIBLE);
        fab.setVisibility(FloatingActionButton.INVISIBLE);


        fab.setOnClickListener(new View.OnClickListener() {
            boolean isUp = false;

            @Override
            public void onClick(View view) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));

                HashMap<String, String> queryValuesMap = new HashMap<String, String>();
                queryValuesMap.put(WEIGHT, et_weight.getText().toString());
                queryValuesMap.put(REPS, "" + reps);
                queryValuesMap.put(ONERM, tv_onerm_big.getText().toString());
                queryValuesMap.put(DATE_CREATED, changeDateFormat(dateFormatter.format(newDate.getTime())));

                Log.d("ON-Save-Button-Click", queryValuesMap.toString());

                dbTools.insertRecord(queryValuesMap);

                if (!isUp) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity()) // context
                                    .text(getString(R.string.cf_rsaved)) // text to display
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                    .eventListener(new EventListener() {

                                        @Override
                                        public void onShow(Snackbar snackbar) {
                                            //fab.hide(true);//moveUp(snackbar.getHeight());
                                            Log.d("isUp", "" + isUp);
                                            if (!isUp) {
                                                fab.animate().translationYBy(-snackbar.getHeight());
                                            }
                                            isUp = true;
                                        }

                                        @Override
                                        public void onShown(Snackbar snackbar) {
                                            Log.i("FAB v. SNACKBAR", String.format("Snackbar shown. Width: %d Height: %d Offset: %d",
                                                    snackbar.getWidth(), snackbar.getHeight(),
                                                    snackbar.getOffset()));
                                        }

                                        @Override
                                        public void onDismiss(Snackbar snackbar) {
                                            //fab.show(true);//0, -snackbar.getHeight());
                                                fab.animate().translationYBy(snackbar.getHeight());
                                        }

                                        @Override
                                        public void onDismissed(Snackbar snackbar) {
                                            Log.i("FAB v. SNACKBAR", String.format("Snackbar dismissed. Width: %d Height: %d Offset: %d",
                                                    snackbar.getWidth(), snackbar.getHeight(),
                                                    snackbar.getOffset()));
                                            isUp = false;
                                        }
                                    }) // Snackbar's EventListener
                            , getActivity()); // activity where it is displayed

                }
            }
        });


        firstTime = true;

        discreteSeekBar_reps.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d("SeekBar REPS", "" + weight + " Calculations 1RM = " + Calculations.calculateRepMax(weight, value));
                reps = value;

                if (value == 1) {
                    tv_reps.setText(getString(R.string.cf_one_rep)+" " + getResources().getString(R.string.tv_reps));
                } else {
                    tv_reps.setText(value + " "+getString(R.string.cf_reps)+" " + getResources().getString(R.string.tv_reps));
                }

                if (!et_weight.getText().toString().equals("")) {
                    tv_onerm_big.setText("" + Calculations.calculateRepMax(weight, value));
                    tv_xrm_big.setText("" + Calculations.calculateXRM(Double.parseDouble(tv_onerm_big.getText().toString()), discreteSeekBar_allrepmax.getProgress()));
                }
                return value;
            }
        });

        discreteSeekBar_allrepmax.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override

            public int transform(int value) {
                tv_xrm.setText(" " + value + " " + getResources().getString(R.string.tv_repmax));
                if (!et_weight.getText().toString().equals("")) {
                    tv_xrm_big.setText("" + Calculations.calculateXRM(Double.parseDouble(tv_onerm_big.getText().toString()), value));
                }
                return value;
            }
        });


        return rootView;
    }

    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!et_weight.getText().toString().equals("")) {
                weight = Double.parseDouble(et_weight.getText().toString());
                tv_reps.setVisibility(TextView.VISIBLE);
                discreteSeekBar_reps.setVisibility(View.VISIBLE);
                tv_onerm_big.setVisibility(TextView.VISIBLE);
                tv_your_onerm.setVisibility(TextView.VISIBLE);
                discreteSeekBar_allrepmax.setVisibility(View.VISIBLE);
                tv_your_xrm.setVisibility(TextView.VISIBLE);
                tv_xrm.setVisibility(TextView.VISIBLE);
                tv_xrm_big.setVisibility(TextView.VISIBLE);
                fab.setVisibility(FloatingActionButton.VISIBLE);

                visible = true;
                setAnimations();
                firstTime = false;

                tv_onerm_big.setText("" + Calculations.calculateRepMax(weight, discreteSeekBar_reps.getProgress()));
                tv_xrm_big.setText("" + Calculations.calculateXRM(Double.parseDouble(tv_onerm_big.getText().toString()), discreteSeekBar_allrepmax.getProgress()));


            } else {
                visible = false;
                firstTime = true;
                setAnimations();
                /*tv_reps.setVisibility(TextView.INVISIBLE);
                discreteSeekBar_reps.setVisibility(TextView.INVISIBLE);
                tv_onerm_big.setVisibility(TextView.INVISIBLE);
                tv_your_onerm.setVisibility(TextView.INVISIBLE);
                discreteSeekBar_allrepmax.setVisibility(TextView.INVISIBLE);
                tv_your_xrm.setVisibility(TextView.INVISIBLE);
                tv_xrm.setVisibility(TextView.INVISIBLE);
                tv_xrm_big.setVisibility(TextView.INVISIBLE);
*/
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setAnimations() {
        if (visible && firstTime) {
            yoyoHelper(true, R.id.tv_reps);
            yoyoHelper(true, R.id.discrete_reps);
            yoyoHelper(true, R.id.tv_onerm_big);
            yoyoHelper(true, R.id.tv_your_onerm);
            yoyoHelper(true, R.id.discrete_allrepmax);
            yoyoHelper(true, R.id.tv_your_xrm);
            yoyoHelper(true, R.id.tv_xrm);
            yoyoHelper(true, R.id.tv_xrm_big);
            yoyoHelper(true, R.id.fab);
        } else if (!visible && firstTime) {
            yoyoHelper(false, R.id.tv_reps);
            yoyoHelper(false, R.id.discrete_reps);
            yoyoHelper(false, R.id.tv_onerm_big);
            yoyoHelper(false, R.id.tv_your_onerm);
            yoyoHelper(false, R.id.discrete_allrepmax);
            yoyoHelper(false, R.id.tv_your_xrm);
            yoyoHelper(false, R.id.tv_xrm);
            yoyoHelper(false, R.id.tv_xrm_big);
            yoyoHelper(false, R.id.fab);
        }
    }

    private void yoyoHelper(boolean in, int id) {
        if (in) {
            YoYo.with(Techniques.SlideInLeft)
                    .duration(700)
                    .playOn(getActivity().findViewById(id));
        } else {
            YoYo.with(Techniques.SlideOutRight)
                    .duration(400)
                    .playOn(getActivity().findViewById(id));
        }

    }

    private void findViewsById(View view) {

        touchInterceptor = (FrameLayout) view.findViewById(R.id.touchInterceptor);

        listView = (ListView) view.findViewById(android.R.id.list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(listView);


        tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        et_weight = (EditText) view.findViewById(R.id.et_weight);
        tv_reps = (TextView) view.findViewById(R.id.tv_reps);
        tv_your_onerm = (TextView) view.findViewById(R.id.tv_your_onerm);
        tv_onerm_big = (TextView) view.findViewById(R.id.tv_onerm_big);
        tv_your_xrm = (TextView) view.findViewById(R.id.tv_your_xrm);
        tv_xrm = (TextView) view.findViewById(R.id.tv_xrm);
        tv_xrm_big = (TextView) view.findViewById(R.id.tv_xrm_big);

        discreteSeekBar_reps = (DiscreteSeekBar) view.findViewById(R.id.discrete_reps);
        discreteSeekBar_allrepmax = (DiscreteSeekBar) view.findViewById(R.id.discrete_allrepmax);
    }

    private String changeDateFormat(String oldDateFormatString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyy");
            Date d = sdf.parse(oldDateFormatString);
            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
