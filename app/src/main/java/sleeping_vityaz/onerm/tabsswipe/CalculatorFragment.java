package sleeping_vityaz.onerm.tabsswipe;


import sleeping_vityaz.onerm.Calculations;
import sleeping_vityaz.onerm.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


/**
 * Created by naja-ox on 1/10/15.
 */
public class CalculatorFragment extends Fragment {

    private TextView tv_weight, tv_reps,
            tv_your_onerm, tv_one_rm,
            tv_onerm_big,
            tv_allrepmax,
            tv_your_xrm, tv_xrm,
            tv_xrm_big;

    private EditText et_weight;
    private DiscreteSeekBar discreteSeekBar_reps;
    private DiscreteSeekBar discreteSeekBar_allrepmax;
    private double weight;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.calculator_layout, container, false);
        //DBTools dbTools = DBTools.getInstance(this.getActivity());

        findViewsById(rootView);

        et_weight.requestFocus();


        discreteSeekBar_reps.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.d("SeekBar REPS", "" + weight + " Calculations 1RM = " + Calculations.calculateRepMax(weight, value));

                if (value==1){
                    tv_reps.setText("  1 Rep "+getResources().getString(R.string.tv_reps));
                }else{
                    tv_reps.setText(value+" Reps "+getResources().getString(R.string.tv_reps));
                }

                if (et_weight.getText().toString().equals("")) {
                    tv_onerm_big.setText("");
                } else {
                    weight = Double.parseDouble(et_weight.getText().toString());
                    tv_onerm_big.setText("" + Calculations.calculateRepMax(weight, value));
                }
                return value;
            }
        });

        discreteSeekBar_allrepmax.setNumericTransformer(new org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.NumericTransformer() {
            @Override

            public int transform(int value) {
                return value * 1;
            }
        });

        return rootView;
    }

    private void findViewsById(View view) {
        tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        et_weight = (EditText) view.findViewById(R.id.et_weight);
        tv_reps = (TextView) view.findViewById(R.id.tv_reps);
        tv_your_onerm = (TextView) view.findViewById(R.id.tv_your_onerm);
        tv_onerm_big = (TextView) view.findViewById(R.id.tv_onerm_big);
        tv_allrepmax = (TextView) view.findViewById(R.id.tv_allrepmax);
        tv_your_xrm = (TextView) view.findViewById(R.id.tv_your_xrm);
        tv_xrm = (TextView) view.findViewById(R.id.tv_xrm);
        tv_xrm_big = (TextView) view.findViewById(R.id.tv_xrm_big);

        discreteSeekBar_reps = (DiscreteSeekBar) view.findViewById(R.id.discrete_reps);
        discreteSeekBar_allrepmax = (DiscreteSeekBar) view.findViewById(R.id.discrete_allrepmax);
    }
}
