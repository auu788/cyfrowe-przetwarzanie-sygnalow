package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilteringUtils {

    public static Signal weaveSignal(Signal first, Signal second) {
        TreeMap<Double, Double> weaveOfSignals = new TreeMap<>();
        int signalLength;
        double sum, time;
        List<Double> firstValues;
        List<Double> secondValues;
        String name;

        firstValues = new ArrayList<>( first.getData().values() );
        secondValues = new ArrayList<>( second.getData().values() );
        signalLength = firstValues.size() + secondValues.size() - 1;
        time = (first.getData().lastKey() + second.getData().lastKey() - first.getData().firstKey() - second.getData().firstKey()) / signalLength;

        for (int i = 0; i < signalLength; i++) {
            sum = 0;
            for (int j = 0; j < firstValues.size(); j++) {
                if (i - j >= 0 && i - j < secondValues.size()) {
                    sum += firstValues.get( j ) * secondValues.get( secondValues.size() - (i - j) - 1 );
                }
            }

            weaveOfSignals.put( time * i, sum );
        }
        name = "( " + first.name + " * " + second.getName() + " )( n )";

        return new OperationalSignal( name, 1 / time, weaveOfSignals );
    }

    public static TreeMap<Double, Double> weaveMap(TreeMap<Double, Double> first, TreeMap<Double, Double> second) {
        TreeMap<Double, Double> weaveOfSignals = new TreeMap<>();
        int signalLength;
        double sum, time;
        List<Double> firstValues = new ArrayList<>( first.values() );
        List<Double> secondValues = new ArrayList<>( second.values() );

        signalLength = firstValues.size() + secondValues.size() - 1;
        time = second.keySet().stream().max( Double::compareTo ).get() / signalLength;

        for (int i = 0; i < signalLength; i++) {
            sum = 0;
            for (int j = 0; j < firstValues.size(); j++) {
                if (i - j < secondValues.size() && i - j >= 0) {
                    sum += firstValues.get( j ) * secondValues.get( secondValues.size() - (i - j) - 1 );
                }
            }

            weaveOfSignals.put( time * i, sum );
        }

        return weaveOfSignals;
    }

    public static Map<Double, Double> lowPassFilter(Integer filterRow) {
        TreeMap<Double, Double> result = new TreeMap<>();
        Integer center = (filterRow - 1) / 2;
        double factor;

        for (int i = 0; i < filterRow; i++) {
            if (i == center) {
                factor = 2.0 / filterRow;
            } else {
                factor = (Math.sin( (2 * Math.PI * (i - center)) / filterRow ) / (Math.PI * (i - center)));
            }
            result.put( (double) i, factor );
        }

        return result;
    }

    public static Map<Double, Double> bandPassFilter(Map<Double, Double> factors) {
        List<Double> values = new ArrayList<>( factors.values() );
        List<Double> keys = new ArrayList<>( factors.keySet() );
        TreeMap<Double, Double> result = new TreeMap<>();
        double factor;

        for (int i = 0; i < values.size(); i++) {
            factor = values.get( i ) * 2 * Math.sin( (Math.PI * i) / 2.0 );
            result.put( keys.get( i ), factor );
        }

        return result;
    }

    public static Map<Double, Double> highPassFilter(Map<Double, Double> factors) {
        List<Double> values = new ArrayList<>( factors.values() );
        List<Double> keys = new ArrayList<>( factors.keySet() );
        TreeMap<Double, Double> result = new TreeMap<>();
        double factor;

        for (int i = 0; i < values.size(); i++) {
            factor = values.get( i ) * Math.pow( -1.0, i );
            result.put( keys.get( i ), factor );
        }

        return result;
    }

    public static Map<Double, Double> hammingWindow(Map<Double, Double> factors) {
        List<Double> values = new ArrayList<>( factors.values() );
        List<Double> keys = new ArrayList<>( factors.keySet() );
        TreeMap<Double, Double> result = new TreeMap<>();
        double factor;

        for (int i = 0; i < values.size(); i++) {
            factor = (0.53836 - (0.46164 * Math.cos( (2 * Math.PI * i) / values.size() )));
            result.put( keys.get( i ), factor * values.get( i ) );
        }

        return result;
    }

    public static Map<Double, Double> hanningWindow(Map<Double, Double> factors) {
        List<Double> values = new ArrayList<>( factors.values() );
        List<Double> keys = new ArrayList<>( factors.keySet() );
        TreeMap<Double, Double> result = new TreeMap<>();
        double factor;

        for (int i = 0; i < values.size(); i++) {
            factor = (0.5 - (0.5 * Math.cos( (2 * Math.PI * i) / values.size() )));
            result.put( keys.get( i ), factor * values.get( i ) );
        }

        return result;
    }

    public static Map<Double, Double> blackmanWindow(Map<Double, Double> factors) {
        List<Double> values = new ArrayList<>( factors.values() );
        List<Double> keys = new ArrayList<>( factors.keySet() );
        TreeMap<Double, Double> result = new TreeMap<>();
        double factor;

        for (int i = 0; i < values.size(); i++) {
            factor = (0.42 - (0.5 * Math.cos( (2 * Math.PI * i) / values.size() )) + 0.08 * Math.cos( (4 * Math.PI * i) / values.size() ));
            result.put( keys.get( i ), factor * values.get( i ) );
        }

        return result;
    }
}
