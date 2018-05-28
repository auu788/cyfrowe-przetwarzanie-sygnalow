package model;

import org.decimal4j.util.DoubleRounder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParamsUtils {
    public static double calculateMSE(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        double mse_sum = 0;
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());
        List<Double> secondSignalValues = new ArrayList<>(secondSignal.values());

        for (int i = 0; i < secondSignal.size(); i++) {
            mse_sum += Math.pow(secondSignalValues.get(i) - firstSignalValues.get(i), 2);
        }

        return (1.0 / secondSignalValues.size()) * mse_sum;
    }

    // id dB
    public static double calculateSNR(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());
        List<Double> secondSignalValues = new ArrayList<>(secondSignal.values());

        double numeratorSum = 0;

        for (int i = 0; i < firstSignalValues.size(); i++) {
            numeratorSum += Math.pow(firstSignalValues.get(i), 2);
        }

        double denomiatorSum = 0;
        for (int i = 0; i < secondSignalValues.size(); i++) {
            denomiatorSum += Math.pow(secondSignalValues.get(i) - firstSignalValues.get(i), 2);
        }

        return 10.0 * Math.log10(numeratorSum / denomiatorSum);
    }

    public static double calculatePSNR(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());

        double max = firstSignalValues.get(0);
        for(int i=1; i<firstSignal.size(); i++){
            if(firstSignalValues.get(i) > max) {
                max = firstSignalValues.get(i);
            }
        }

        return 10.0 * Math.log10(max / calculateMSE(firstSignal, secondSignal));
    }

    public static double calculateMD(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());
        List<Double> secondSignalValues = new ArrayList<>(secondSignal.values());

        double mdVal = 0;

        for (int i = 0; i < secondSignalValues.size(); i++) {
            double tempMdVal = Math.abs(secondSignalValues.get(i) - firstSignalValues.get(i));
            if (tempMdVal > mdVal) {
                mdVal = tempMdVal;
            }
        }

        return mdVal;
    }
}
