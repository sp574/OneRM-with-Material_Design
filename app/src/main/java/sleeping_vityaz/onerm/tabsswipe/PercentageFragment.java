package sleeping_vityaz.onerm.tabsswipe;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.melnykov.fab.FloatingActionButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import sleeping_vityaz.onerm.Calculations;
import sleeping_vityaz.onerm.R;

/**
 * Created by naja-ox
 */
public class PercentageFragment extends Fragment {

    private View rootView;

    TextView tv_1rm, tv_percent, tv_result1, tv_result2;
    EditText et_1rm;
    private DiscreteSeekBar discreteSeekBar_percent;
    private FrameLayout touchInterceptor;

    private int percent;

    private boolean visible;
    private boolean firstTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.percentage_layout, container, false);
        findViewsById(rootView);

        et_1rm.addTextChangedListener(watch);


        firstTime = true;

        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (et_1rm.isFocused()) {
                        Rect outRect = new Rect();
                        et_1rm.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            et_1rm.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

        tv_percent.setVisibility(TextView.INVISIBLE);
        tv_result1.setVisibility(TextView.INVISIBLE);
        tv_result2.setVisibility(TextView.INVISIBLE);
        discreteSeekBar_percent.setVisibility(View.INVISIBLE);

        discreteSeekBar_percent.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override

            public int transform(int value) {
                percent = value;
                tv_percent.setText("" + value + " " + getResources().getString(R.string.tv_percentage));
                tv_result1.setText("" + value + " " + getResources().getString(R.string.tv_result));

                if (!et_1rm.getText().toString().equals("")) {
                    tv_result2.setText("" + Calculations.round((value * Double.parseDouble(et_1rm.getText().toString()) / 100), 1));
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

            if (!et_1rm.getText().toString().equals("")) {
                tv_percent.setVisibility(TextView.VISIBLE);
                tv_result1.setVisibility(TextView.VISIBLE);
                tv_result2.setVisibility(TextView.VISIBLE);
                discreteSeekBar_percent.setVisibility(View.VISIBLE);

                visible = true;
                setAnimations();
                firstTime = false;

                tv_result2.setText("" + Calculations.round((percent * Double.parseDouble(et_1rm.getText().toString()) / 100), 1));

            } else {
                visible = false;
                firstTime = true;
                setAnimations();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setAnimations() {
        if (visible && firstTime) {
            yoyoHelper(true, R.id.tv_percent);
            yoyoHelper(true, R.id.tv_result1);
            yoyoHelper(true, R.id.tv_result2);
            yoyoHelper(true, R.id.discrete_percent);
        } else if (!visible && firstTime) {
            yoyoHelper(false, R.id.tv_percent);
            yoyoHelper(false, R.id.tv_result1);
            yoyoHelper(false, R.id.tv_result2);
            yoyoHelper(false, R.id.discrete_percent);
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

        tv_1rm = (TextView) view.findViewById(R.id.tv_1rm);
        tv_percent = (TextView) view.findViewById(R.id.tv_percent);
        tv_result1 = (TextView) view.findViewById(R.id.tv_result1);
        tv_result2 = (TextView) view.findViewById(R.id.tv_result2);
        et_1rm = (EditText) view.findViewById(R.id.et_1rm);


        discreteSeekBar_percent = (DiscreteSeekBar) view.findViewById(R.id.discrete_percent);
    }
}
