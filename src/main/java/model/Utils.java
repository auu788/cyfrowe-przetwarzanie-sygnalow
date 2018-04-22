package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static ObservableList<String> signalTypes = FXCollections.observableArrayList(
            "Szum o rozkładzie jednostajnym",
            "Szum gaussowski",
            "Sygnał sinusoidalny",
            "Sygnał sinusoidalny wyprostowany jednopołówkowo",
            "Sygnał sinusoidalny wyprostowany dwupołówkowo",
            "Sygnał prostokątny",
            "Sygnał prostokątny symetryczny",
            "Sygnał trójkątny",
            "Skok jednostkowy",
            "Impuls jednostkowy",
            "Szum impulsowy"
    );

    public static Map<String, Integer> getHistogramData(Signal signal, Integer binsNum) {
        Map<String, Integer> histogramData = new LinkedHashMap<>();
        double interval = (signal.getMaxValue() - signal.getMinValue()) / (double) binsNum;

        int counter = 0;
        Map<Integer, Double> values = signal.getData();

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

    public Signal addSignals(Signal s1, Signal s2) {
        return s1;
    }

    public Signal subtractSignals(Signal s1, Signal s2) {
        return s1;
    }

    public Signal multiplySignals(Signal s1, Signal s2) {
        return s1;
    }

    public Signal divideSignals(Signal s1, Signal s2) {
        return s1;
    }
}
