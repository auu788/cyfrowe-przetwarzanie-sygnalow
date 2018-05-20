package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class Utils {
    public static final String UNIFORM_NOISE            = "Szum o rozkładzie jednostajnym";
    public static final String GAUSSIAN_NOISE           = "Szum gaussowski";
    public static final String SINE_SIGNAL              = "Sygnał sinusoidalny";
    public static final String ONE_HALF_SINE_SIGNAL     = "Sygnał sinusoidalny wyprostowany jednopołówkowo";
    public static final String TWO_HALF_SINE_SIGNAL     = "Sygnał sinusoidalny wyprostowany dwupołówkowo";
    public static final String RECT_SIGNAL              = "Sygnał prostokątny";
    public static final String SYMMETRIC_RECT_SIGNAL    = "Sygnał prostokątny symetryczny";
    public static final String TRIANGLE_SIGNAL          = "Sygnał trójkątny";
    public static final String UNIT_JUMP_SIGNAL         = "Skok jednostkowy";
    public static final String UNIT_PULSE               = "Impuls jednostkowy";
    public static final String PULSE_NOISE              = "Szum impulsowy";

    public static final String ADD_OPERATION            = "Dodaj";
    public static final String SUB_OPERATION            = "Odejmij";
    public static final String MUL_OPERATION            = "Pomnóż";
    public static final String DIV_OPERATION            = "Podziel";

    public static ObservableList<String> signalTypes = FXCollections.observableArrayList(
            UNIFORM_NOISE,
            GAUSSIAN_NOISE,
            SINE_SIGNAL,
            ONE_HALF_SINE_SIGNAL,
            TWO_HALF_SINE_SIGNAL,
            RECT_SIGNAL,
            SYMMETRIC_RECT_SIGNAL,
            TRIANGLE_SIGNAL,
            UNIT_JUMP_SIGNAL,
            UNIT_PULSE,
            PULSE_NOISE
    );

    public static Map<String, Integer> getHistogramData(Signal signal, Integer binsNum) {
        Map<String, Integer> histogramData = new LinkedHashMap<>();
        double interval = (signal.getMaxValue() - signal.getMinValue()) / (double) binsNum;

        int counter = 0;
        List<Double> values = new ArrayList<>(signal.getData().values());

        for (double i = signal.getMinValue(); i < signal.getMaxValue(); i += interval) {

        }
        for(double i = signal.getMinValue(); i < signal.getMaxValue(); i += interval) {
            for(int j = 0; j < values.size(); j++) {
                if(values.get(j) >= i && values.get(j) < i + interval) {
                    counter++;
                }
            }

            histogramData.put(String.format("%.2f", i), counter);
            counter = 0;
        }

        return histogramData;
    }
}
