package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Signal;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainAppController {
    public static ObservableList<Signal> signalItems = FXCollections.observableArrayList();

    private Stage stage;

//    @FXML NumberAxis lineXAxis;
//    @FXML NumberAxis lineYAxis;

    @FXML Label avgLbl;
    @FXML Label absoluteAvgLbl;
    @FXML Label avgSignalPowerLbl;
    @FXML Label varianceLbl;
    @FXML Label rmsLbl;

    @FXML Slider bucketsNumSlider;
    @FXML Label bucketsNumLbl;

    @FXML LineChart<String, Double> lineChart;
    @FXML BarChart<String, Integer> barChart;

    @FXML ListView<Signal> signalList;
    @FXML Label noSignalLbl;

    public void init(Stage stage) {
        this.stage = stage;

        signalItems.addListener(
                (ListChangeListener<Signal>) c -> {
                    while (c.next()) {
                        if (c.getAddedSize() > 0) noSignalLbl.setVisible(false);
                        else noSignalLbl.setVisible(true);
                    }
                }
        );
    }

    @FXML
    private void initialize() {
        signalList.setItems(signalItems);

        bucketsNumLbl.setText(String.valueOf((int) bucketsNumSlider.getValue()));

        bucketsNumSlider.valueProperty().addListener(
                (obs, oldValue, newValue) -> {
                    bucketsNumSlider.setValue(newValue.intValue());
                    bucketsNumLbl.setText(String.valueOf((int) bucketsNumSlider.getValue()));

                    if (signalList.getSelectionModel().getSelectedItem() != null)
                        generateBarChart(signalList.getSelectionModel().getSelectedItem(), (int) bucketsNumSlider.getValue());
                }
        );

        signalList.setOnMouseClicked(
                event -> {
                    Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();

                    avgLbl.setText(String.format("%.2f", selectedSignal.getAvg()));
                    absoluteAvgLbl.setText(String.format("%.2f", selectedSignal.getAbsoluteAvg()));
                    avgSignalPowerLbl.setText(String.format("%.2f", selectedSignal.getAvgSignalPower()));
                    varianceLbl.setText(String.format("%.2f", selectedSignal.getVariance()));
                    rmsLbl.setText(String.format("%.2f", selectedSignal.getRms()));

                    generateLineChart(selectedSignal);
                    generateBarChart(selectedSignal, (int) bucketsNumSlider.getValue());
                }
        );
    }

    @FXML
    private void createSignal(ActionEvent e) {
        NewSignalWindow newSignal = new NewSignalWindow();
        newSignal.create();
    }

    @FXML
    private void loadFromFile(ActionEvent e) {
        System.out.println("Załaduj z pliku");
        noSignalLbl.setDisable(false);
    }

    @FXML
    private void saveToFile(ActionEvent e) {
        System.out.println("Zapisz do pliku");
    }

    private void generateLineChart(Signal signal) {
        lineChart.getData().clear();

        Integer startTime = signal.getStartTime();
        Double frequencySampling = signal.getFrequencySampling();
        Integer duration = signal.getDuration();
        int minValue = signal.getMinValue();
        int maxValue = signal.getMaxValue();
        Map<Integer, Double> signalData = signal.getData();

//        lineXAxis.setLowerBound(startTime);
//        lineXAxis.setUpperBound(startTime + duration);
//
//        lineYAxis.setLowerBound(minValue);
//        lineYAxis.setUpperBound(maxValue);

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (int i = 0; i < signal.getData().size(); i++) {
            XYChart.Data<String, Double> d = new XYChart.Data<String, Double>(
                    String.format("%.2f", (i / frequencySampling) + startTime), signalData.get(i));

            series.getData().add(d);
        }

        lineChart.setTitle("Wykres liniowy");
        lineChart.getData().add(series);
    }

    private void generateBarChart(Signal signal, Integer binsNum) {
        barChart.getData().clear();

        BarChart.Series<String, Integer> series = new BarChart.Series<>();
        Map<String, Integer> histogramData = getHistogramData(signal, binsNum);

        histogramData.forEach(
                (k, v) -> series.getData().add(new BarChart.Data<String, Integer>(k, v))
        );

        barChart.setTitle("Histogram");
        barChart.getData().add(series);

    }

    public Map<String, Integer> getHistogramData(Signal signal, Integer binsNum) {
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
            int index = String.valueOf(i).indexOf('.');

            histogramData.put(String.format("%.2f", i), counter);
            counter = 0;
        }

        return histogramData;
    }

}
