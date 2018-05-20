package model;

import org.decimal4j.util.DoubleRounder;

import java.util.*;

public class SamplingUtils {

    public static Map<Double, Double> generateSampleSignal(Signal originalSignal, Integer samplingTime) {
        Map<Double, Double> sampleData = new TreeMap<>();

        List<Double> signalValues = new ArrayList<>(originalSignal.signal.values());
        List<Double> signalKeys = new ArrayList<>(originalSignal.signal.keySet());

        for (int i = 0; i < signalKeys.size(); i += originalSignal.getFrequencySampling() / samplingTime) {
            sampleData.put(signalKeys.get(i), signalValues.get(i));
        }

        return sampleData;
    }

    /*static class Pair {
        public double first;
        public double second;

        public Pair(double first, double second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Point {
        double x;
        double y;

        Point (double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static Map<Double, Double> generateSampleSignal(Signal originalSignal, Integer samplingTime) {
        Map<Double, Double> sampleData = new TreeMap<>();

        List<Double> signalValues = new ArrayList<>(originalSignal.signal.values());
        List<Double> signalKeys = new ArrayList<>(originalSignal.signal.keySet());

        System.out.println("Analog size: " + thi);

        for (double i = 0; i < signalKeys.size(); i += 1 / samplingTime) {
            double analogValue = originalSignal.analogSignal.get((int) i * 100);

            sampleData.put(i, analogValue);
        }


        return sampleData;
    }

    private static double getLinearValue(Point a, Point b, double x) {
        double m = (b.x-a.x)/(b.y-a.y);
        return (x-a.x)*m+a.y;
    }

    private static Pair getNearestKeys(List<Double> list, Double value) {
        int nearestIndex = 0;
        double smallestDiff = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); i++) {
            if (Math.abs(value - list.get(i)) < smallestDiff) {
                nearestIndex = i;
                smallestDiff = Math.abs(value - list.get(i));
            }
        }

        if (nearestIndex == list.size() - 1)
            return new Pair(list.get(nearestIndex), list.get(nearestIndex - 1));
        else if (nearestIndex == 0) {
            return new Pair(list.get(nearestIndex), list.get(nearestIndex + 1));
        }
        else {
            if (Math.abs(list.get(nearestIndex - 1) - value) < Math.abs(list.get(nearestIndex + 1) - value))
                return new Pair(list.get(nearestIndex - 1), list.get(nearestIndex));
            else
                return new Pair(list.get(nearestIndex), list.get(nearestIndex + 1));
        }
    }*/
}
