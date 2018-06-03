package model;

import controller.MainAppController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilteringUtils {

    public static Signal weave(Signal first, Signal second, Double samplingFrequency, int n) {
        TreeMap<Double, Double> weaveOfSignals = new TreeMap<>();
        int signalLength;
        double sum, time;
        List<Double> firstValues, secondValues;
        String name;

        firstValues = new ArrayList<>( SamplingUtils.generateSampleSignal( first, n ).values() );
        secondValues = new ArrayList<>( SamplingUtils.generateSampleSignal( second, n ).values() );
        signalLength = firstValues.size() + secondValues.size() - 1;
        time = firstValues.size() / samplingFrequency;

        for (int i = 0; i < signalLength; i++) {
            sum = 0;
            for (int j = 0; j < firstValues.size(); j++) {
                if (i - j >= 0 && i - j < secondValues.size()) {
                    sum += firstValues.get( j ) * secondValues.get( i - j );
                }
            }

            weaveOfSignals.put( (time / signalLength) * i, sum );
            System.out.println( "i: " + i + " - " + sum );
        }
        name = "( " + first.name + " * " + second.getName() + " )( " + signalLength + " )";

        return new OperationalSignal( name, samplingFrequency, weaveOfSignals );
    }
}
