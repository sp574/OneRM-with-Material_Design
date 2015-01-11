package sleeping_vityaz.onerm;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by naja-ox on 1/10/15.
 */
public class Calculations {

    public static double calculateRepMax(double weight, int reps){
        return round((weight*reps/30+weight),1);
    }

    public static double calculateConservativeRepMax(double weight, int reps){
        return round(((weight*36)/(37-reps)),1);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
