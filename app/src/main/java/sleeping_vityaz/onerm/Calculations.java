package sleeping_vityaz.onerm;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by naja-ox
 */
public class Calculations {

    public static double calculateRepMax(double weight, int reps){
        return round((weight*reps/30+weight),1);
    }

    /*public static double calculateConservativeRepMax(double weight, int reps){
        return round(((weight*36)/(37-reps)),1);
    }*/

    public static double calculateXRM(double weight, int reps){
        switch (reps){
            case 1: return weight;
            case 2: return round(weight/1.066,1);
            case 3: return round(weight/1.099,1);
            case 4: return round(weight/1.132,1);
            case 5: return round(weight/1.165,1);
            case 6: return round(weight/1.198,1);
            case 7: return round(weight/1.231,1);
            case 8: return round(weight/1.264,1);
            case 9: return round(weight/1.297,1);
            case 10: return round(weight/1.330,1);
        }
        Log.e("CALCULATIONS", "ERROR: Calculations shouldn't get to this point");
        return weight;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
