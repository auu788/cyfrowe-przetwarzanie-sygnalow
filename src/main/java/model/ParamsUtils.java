package model;

import org.decimal4j.util.DoubleRounder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParamsUtils {
    public static double calculateMSE(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        double noise = calculateDifferenceSumSquared(firstSignal, secondSignal);
        System.out.println("Noise: " + noise);
        double length = secondSignal.size();
        return 1/length * noise;
    }

    public static double calculateSNR(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());

        double noise = calculateDifferenceSumSquared(firstSignal, secondSignal);
        double signal=  0;
        double value;
        for(int i=0; i<firstSignal.size(); i++) {
            value = firstSignalValues.get(i);
            signal += value * value;
        }
        return 10 * Math.log10((signal/noise));
    }

    public static double calculatePSNR(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());

        double max = firstSignalValues.get(0);
        for(int i=1; i<firstSignal.size(); i++){
            if(firstSignalValues.get(i) > max) {
                max = firstSignalValues.get(i);
            }
        }
        double psnr = (max*max)/calculateMSE(firstSignal, secondSignal);
        return 10 * Math.log10(psnr);
    }

    public static double calculateMD(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        List<Double> firstSignalKeys = new ArrayList<>(firstSignal.keySet());
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());
        List<Double> secondSignalKeys = new ArrayList<>(secondSignal.values());
        List<Double> secondSignalValues = new ArrayList<>(secondSignal.values());

        List<Double> difference = new ArrayList<>();
        for (int i = 0; i < secondSignal.size(); i++) {
            for(int j=0; j<firstSignal.size(); j++) {
                if (Math.round(firstSignalKeys.get(j)) == Math.round(secondSignalKeys.get(i))) {
                    double p1 = firstSignalValues.get(j);
                    double p2 = secondSignalValues.get(i);
                    difference.add(Math.abs(p1-p2));
                }
            }
        }
        return Collections.max(difference);
    }

    public static double calculateDifferenceSumSquared(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        double sum_sq = 0;

        List<Double> firstSignalKeys = new ArrayList<>(firstSignal.keySet());
        List<Double> firstSignalValues = new ArrayList<>(firstSignal.values());
        List<Double> secondSignalKeys = new ArrayList<>(secondSignal.values());
        List<Double> secondSignalValues = new ArrayList<>(secondSignal.values());

        for (int i = 0; i < secondSignal.size(); i++) {
            for(int j=0; j<firstSignal.size(); j++) {
                if (DoubleRounder.round(firstSignalKeys.get(j), 2) == DoubleRounder.round(secondSignalKeys.get(i), 2)) {
                    double p1 = firstSignalValues.get(j);
                    double p2 = secondSignalValues.get(i);
//                    System.out.println("Err [" + DoubleRounder.round(secondSignalKeys.get(i), 2) + ", " + DoubleRounder.round(firstSignalKeys.get(j), 2) + "]: " + p2 + " - " + p1 + " =" + (p2 - p1));
                    double err = p2 - p1;
                    sum_sq += (err * err);
                }
            }
        }
        return sum_sq;
    }
}
