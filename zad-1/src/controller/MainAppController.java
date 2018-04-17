package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Signal;
import model.Utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainAppController {
    public static ObservableList<Signal> signalItems = FXCollections.observableArrayList();

    private Stage stage;

//    @FXML NumberAxis lineXAxis;
//    @FXML NumberAxis lineYAxis;

    @FXML Label signalNameLbl;
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
    }

    public void createSignalDetailsWindow(Signal signal) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SignalDetailsWindow.fxml"));
            SignalDetailsWindow signalDetailsWindow = new SignalDetailsWindow();
            if (signal != null)
                signalDetailsWindow.edit(signal);
            fxmlLoader.setController(signalDetailsWindow);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("view/modena-dark.css");

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Utwórz nowy sygnał");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        signalList.setItems(signalItems);
        bucketsNumLbl.setText(String.valueOf((int) bucketsNumSlider.getValue()));

        setupListeners();
    }

    @FXML
    private void createSignal(ActionEvent e) {
        createSignalDetailsWindow(null);
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
        Map<String, Integer> histogramData = Utils.getHistogramData(signal, binsNum);

        histogramData.forEach(
                (k, v) -> series.getData().add(new BarChart.Data<String, Integer>(k, v))
        );

        barChart.setTitle("Histogram");
        barChart.getData().add(series);

    }

    private void setupListeners() {
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
                    if (signalList.getSelectionModel().getSelectedItem() == null) return;
                    Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();

                    signalNameLbl.setText(selectedSignal.getName());
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
    private void removeSignal(ActionEvent e) {
        signalItems.remove(signalList.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editSignal(ActionEvent e) {
        createSignalDetailsWindow(signalList.getSelectionModel().getSelectedItem());
    }

    private void createSignalChooseWindow(String operation) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SignalChooser.fxml"));
            SignalChooser signalChooser = new SignalChooser();
            signalChooser.setOperation(operation);

            fxmlLoader.setController(signalChooser);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("view/modena-dark.css");

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Wybierz sygnał");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addSignal(ActionEvent e) {
        createSignalChooseWindow("Dodaj");
    }

    @FXML
    private void substractSignal(ActionEvent e) {
        createSignalChooseWindow("Odejmij");
    }

    @FXML
    private void multiplySignal(ActionEvent e) {
        createSignalChooseWindow("Pomnóż");
    }

    @FXML
    private void divideSignal(ActionEvent e) {
        createSignalChooseWindow("Podziel");
    }

}
