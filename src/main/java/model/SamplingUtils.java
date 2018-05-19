package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SamplingUtils {

    public static Map<Double, Double> generateSampleSignal(Signal originalSignal, Integer samplingTime) {
        Map<Double, Double> sampleData = new HashMap<Double, Double>();
        int x = originalSignal.signal.size() / samplingTime;

        List<Double> signalValues = new ArrayList<>(originalSignal.signal.values());
        List<Double> signalKeys = new ArrayList<>(originalSignal.signal.keySet());

        for (long i = Math.round(signalKeys.get(0)); i < signalKeys.size(); i = i + x) {
            for (int j = 0; j < signalKeys.size(); j++) {
                if (j == Math.round(i)) {
                    sampleData.put(signalKeys.get((int) i), signalValues.get((int) i));
                }
            }
        }

        return sampleData;
    }
}
