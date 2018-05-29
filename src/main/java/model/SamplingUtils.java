package model;

import org.apache.commons.math3.analysis.function.Sinc;
import org.decimal4j.util.DoubleRounder;

import java.util.*;

public class SamplingUtils {

    public static Map<Double, Double> generateSampleSignal(Signal originalSignal, int samplingTime) {

        /*
        if(frequency > original.getSamplingFrequency()) {
			throw new Exception("Nieprawidłowa częstotliwość!");
		}
		if(original.getSamplingFrequency() % frequency != 0) {
			throw new Exception("Nieprawidłowa częstotliwość!");
		}

		int step = (int) (original.getSamplingFrequency() / frequency);

		double[] values = new double [(int) (frequency*original.getDuration() + 1)];
		for(int i = 0, j = 0; i < values.length-1; i++, j+=step) {
			values[i] = original.getValues()[j];
		}

		values[values.length-1] = original.getValues()[original.getValues().length-1];
		sampledValues = values;
		samplingFrequency = frequency;

		return values;
		*/
        Map<Double, Double> sampleData = new TreeMap<>();
        int x = originalSignal.signal.size() / samplingTime;

        List<Double> signalValues = new ArrayList<>(originalSignal.signal.values());
        List<Double> signalKeys = new ArrayList<>(originalSignal.signal.keySet());

//        for (int i = 0, j = 0; i < signalValues.size(); i++, j += step) {
//            if (j < signalValues.size())
//                sampleData.put(signalKeys.get(j), signalValues.get(j));
//        }


        for (long i = Math.round(signalKeys.get(0)); i < signalKeys.size(); i = i + x) {
            for (int j = 0; j < signalKeys.size(); j++) {
                if (j == Math.round(i)) {
                    sampleData.put(signalKeys.get((int) i), signalValues.get((int) i));
                }
            }
        }

        return sampleData;
    }

    public static Map<Double, Double> calculateQuantization(Map<Double, Double> signal, int n) {
        List<Double> signalValues = new ArrayList<>(signal.values());
        List<Double> signalKeys = new ArrayList<>(signal.keySet());

        List<Double> rangeValues = new ArrayList<>();
        Double min = signalValues.stream().min(Comparator.comparing(Double::valueOf)).get();
        Double max = signalValues.stream().max(Comparator.comparing(Double::valueOf)).get();
        Double range = (max - min) / (Math.pow(2, n));

        for (double i = min; i <= max; i += range) {
            rangeValues.add(i);
        }

        Map<Double, Double> quantizedSignal = new TreeMap<>();
        for (int i = 0; i < signal.size(); i++) {
            double minDist = max;
            double value = 0;

            for (double val : rangeValues) {
                double dist = Math.abs(val - signalValues.get(i));
                if (dist < minDist) {
                    minDist = dist;
                    value = val;
                }
            }

            quantizedSignal.put(signalKeys.get(i), value);
        }

        return quantizedSignal;
    }

    public static Map<Double, Double> extrapolateZeroOrderHold(Map<Double, Double> sampledSignal, Double samplingFreq) {
        List<Double> signalKeys = new ArrayList<>(sampledSignal.keySet());
        List<Double> signalValues = new ArrayList<>(sampledSignal.values());

        Map<Double, Double> upsampledSignal = new TreeMap<>();
        Double T = 1.0 / samplingFreq;

        for(int i = 0; i < signalKeys.size(); i++) {
            double t = signalKeys.get(i);
            double sum = 0;
            for(int j = 0; j < signalValues.size(); j++) {
                double rect = rect( (t - (T / 2.0) - (j * T)) / T );
                sum += signalValues.get(j) * rect;
            }
            upsampledSignal.put(t, sum);
        }

        return upsampledSignal;
    }

    public static Map<Double, Double> sincFunction(Map<Double, Double> sampledSignal, Double samplingFreq) {
        List<Double> signalKeys = new ArrayList<>(sampledSignal.keySet());
        List<Double> signalValues = new ArrayList<>(sampledSignal.values());

        Map<Double, Double> upsampledSignal = new TreeMap<>();
        Double T = 1.0 / samplingFreq;

        Sinc sinc = new Sinc(true);

        for(int i = 0; i < signalValues.size(); i++) {
            double t = signalKeys.get(i);
            double sum = 0;
            for(int j = 0; j < signalValues.size(); j++) {
                sum += signalValues.get(j) * sinc.value((t / T) - j);
            }
            upsampledSignal.put(t, sum);
        }

        return upsampledSignal;
    }

    public static Map<Double, Double> interpolateFirstOrderHold(Map<Double, Double> sampledSignal, Double samplingFreq) {
        List<Double> signalKeys = new ArrayList<>(sampledSignal.keySet());
        List<Double> signalValues = new ArrayList<>(sampledSignal.values());

        Map<Double, Double> upsampledSignal = new TreeMap<>();
        Double rectVal = 0.0;
        Double T = 1.0 / samplingFreq;

        for (Double t : signalKeys) {
            Double interpolationSum = 0.0;

            for (int i = 0; i < signalValues.size(); i++) {
                rectVal = tri((t - i * T) / T);
                interpolationSum += signalValues.get(i) * rectVal;
            }

            upsampledSignal.put(t, interpolationSum);
        }

        return upsampledSignal;
    }

    private static Double rect(Double t) {
        if (Math.abs(t) > 0.5) return 0.0;
        if (Math.abs(t) == 0.5) return 0.5;

        return 1.0;
    }

    private static Double tri(Double t) {
        if (Math.abs(t) < 1.0) {
            return 1.0 - Math.abs(t);
        } else {
            return 0.0;
        }
    }
}
