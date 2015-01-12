package sleeping_vityaz.onerm.tabsswipe;


import sleeping_vityaz.onerm.Calculations;
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

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


/**
 * Created by naja-ox
 */
public class CalculatorFragment extends Fragment {

    private TextView tv_reps, tv_weight,
            tv_your_onerm,
            tv_onerm_big,
            tv_your_xrm, tv_xrm,
            tv_xrm_big;

    private FrameLayout touchInterceptor;


    private EditText et_weight;
    private DiscreteSeekBar discreteSeekBar_reps;
    private DiscreteSeekBar discreteSeekBar_allrepmax;
    private double weight;

    private boolean visible;
    private boolean firstTime;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.calculator_layout, container, false);
        //DBTools dbTools = DBTools.getInstance(this.getActivity());

        findViewsById(rootView);

        YoYo.with(Techniques.SlideInLeft)
                .duration(1500)
                .playOn(rootView.findViewById(R.id.tv_weight));
        YoYo.with(Techniques.SlideInLeft)
                .duration(1500)
                .playOn(rootView.findViewById(R.id.et_weight));

        et_weight.addTextChangedListener(watch);

        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (et_weight.isFocused()) {
                        Rect outRect = new Rect();
                        et_weight.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
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
        discreteSeekBar_reps.setVisibility(TextView.INVISIBLE);
        tv_onerm_big.setVisibility(TextView.INVISIBLE);
        tv_your_onerm.setVisibility(TextView.INVISIBLE);

        discreteSeekBar_allrepmax.setVisibility(TextView.INVISIBLE);
        tv_your_xrm.setVisibility(TextView.INVISIBLE);
        tv_xrm.setVisibility(TextView.INVISIBLE);
        tv_xrm_big.setVisibility(TextView.INVISIBLE);

        firstTime = true;

        discreteSeekBar_reps.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d("SeekBar REPS", "" + weight + " Calculations 1RM = " + Calculations.calculateRepMax(weight, value));

                if (value == 1) {
                    tv_reps.setText("1 Rep " + getResources().getString(R.string.tv_reps));
                } else if (value == 0) {
                    tv_reps.setText("Reps " + getResources().getString(R.string.tv_reps));
                } else {
                    tv_reps.setText(value + " Reps " + getResources().getString(R.string.tv_reps));
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
                tv_xrm.setText(" "+value+" "+getResources().getString(R.string.tv_repmax));
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
                discreteSeekBar_reps.setVisibility(TextView.VISIBLE);
                tv_onerm_big.setVisibility(TextView.VISIBLE);
                tv_your_onerm.setVisibility(TextView.VISIBLE);
                discreteSeekBar_allrepmax.setVisibility(TextView.VISIBLE);
                tv_your_xrm.setVisibility(TextView.VISIBLE);
                tv_xrm.setVisibility(TextView.VISIBLE);
                tv_xrm_big.setVisibility(TextView.VISIBLE);

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
        }else if (!visible && firstTime){
            yoyoHelper(false, R.id.tv_reps);
            yoyoHelper(false, R.id.discrete_reps);
            yoyoHelper(false, R.id.tv_onerm_big);
            yoyoHelper(false, R.id.tv_your_onerm);
            yoyoHelper(false, R.id.discrete_allrepmax);
            yoyoHelper(false, R.id.tv_your_xrm);
            yoyoHelper(false, R.id.tv_xrm);
            yoyoHelper(false, R.id.tv_xrm_big);
        }
    }

    private void yoyoHelper(boolean in, int id){
        if (in) {
            YoYo.with(Techniques.SlideInLeft)
                    .duration(700)
                    .playOn(getActivity().findViewById(id));
        }else{
            YoYo.with(Techniques.SlideOutRight)
                    .duration(700)
                    .playOn(getActivity().findViewById(id));
        }

    }

    private void findViewsById(View view) {

        touchInterceptor = (FrameLayout) view.findViewById(R.id.touchInterceptor);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
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
}
