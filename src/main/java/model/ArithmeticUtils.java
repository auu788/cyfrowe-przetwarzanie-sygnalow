package model;

import org.decimal4j.util.DoubleRounder;

import java.util.TreeMap;

public class ArithmeticUtils {
    public static Signal addSignals(Signal s1, Signal s2) {
        TreeMap<Double, Double> map = new TreeMap<>();

        s1.getData().forEach(
                (k, v) -> {
                    System.out.println(DoubleRounder.round(k, 1) + ": " + v);
                    map.put(DoubleRounder.round(k, 1), v);
                }
        );
        s2.getData().forEach(
                (k, v) -> {
                    if (map.get(DoubleRounder.round(k, 1)) != null) {
                        map.put(DoubleRounder.round(k, 1), v + map.get(DoubleRounder.round(k, 1)));
                    }
                    else map.put(DoubleRounder.round(k, 1), v);
                }
        );

        String name = s1.getName() + " + " + s2.getName();
        Double frequencySampling = s1.getFrequencySampling();

        return new OperationalSignal(name, frequencySampling, map);
    }

    public static Signal subtractSignals(Signal s1, Signal s2) {
        TreeMap<Double, Double> map = new TreeMap<>();

        s1.getData().forEach(
                (k, v) -> {
                    map.put(DoubleRounder.round(k, 1), v);
                }
        );

        s2.getData().forEach(
                (k, v) -> {
                    if (map.get(DoubleRounder.round(k, 1)) != null) {
                        map.put(DoubleRounder.round(k, 1), map.get(DoubleRounder.round(k, 1) - v));
                    }
                    else map.put(DoubleRounder.round(k, 1), v);
                }
        );

        String name = s1.getName() + " - " + s2.getName();
        Double frequencySampling = s1.getFrequencySampling();

        return new OperationalSignal(name, frequencySampling, map);
    }

    public static Signal multiplySignals(Signal s1, Signal s2) {
        TreeMap<Double, Double> map = new TreeMap<>();

        s1.getData().forEach(
                (k, v) -> {
                    map.put(DoubleRounder.round(k, 1), v);
                }
        );

        s2.getData().forEach(
                (k, v) -> {
                    if (map.get(DoubleRounder.round(k, 1)) != null) {
                        map.put(DoubleRounder.round(k, 1), v * map.get(DoubleRounder.round(k, 1)));
                    }
                    else map.put(DoubleRounder.round(k, 1), v);
                }
        );

        String name = s1.getName() + " * " + s2.getName();
        Double frequencySampling = s1.getFrequencySampling();

        return new OperationalSignal(name, frequencySampling, map);
    }

    public static Signal divideSignals(Signal s1, Signal s2) {
        TreeMap<Double, Double> map = new TreeMap<>();

        s1.getData().forEach(
                (k, v) -> map.put(DoubleRounder.round(k, 1), v)
        );

        s2.getData().forEach(
                (k, v) -> {
                    if (map.get(DoubleRounder.round(k, 1)) != null) {
                        double tmp = map.get(DoubleRounder.round(k, 1)) / v;
                        map.put(DoubleRounder.round(k, 1), tmp);
                    }
                    else map.put(DoubleRounder.round(k, 1), v);
                }
        );

        String name = s1.getName() + " / " + s2.getName();
        Double frequencySampling = s1.getFrequencySampling();

        return new OperationalSignal(name, frequencySampling, map);
    }
}
