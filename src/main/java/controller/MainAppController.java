package controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class MainAppController {
    public static ObservableList<Signal> signalItems = FXCollections.observableArrayList();

    private Stage stage;

    @FXML Label psnrLbl;
    @FXML Label snrLbl;
    @FXML Label mseLbl;
    @FXML Label mdLbl;

    @FXML TabPane tabPane;
    @FXML Pane signalDetailsPane;
    @FXML Tab mainSignalTab;
    @FXML Tab histogramTab;
    @FXML Tab samplingTab;
    @FXML Tab quantTab;
    @FXML Tab reconstructionTab;

    @FXML AnchorPane samplingPane;
    @FXML Pane zad1Panel;
    @FXML Pane zad2Panel;
    @FXML ChoiceBox<String> reconstructionChooser;
    @FXML SwingNode swingNodeReconstruction;
    @FXML SwingNode swingNodeSampling;
    @FXML SwingNode swingNodeQuantization;
    @FXML ScatterChart<String, Double> quantizationLineChart;
    @FXML TextField numOfSamplesTxt;
    @FXML TextField numOfBitsTxt;

    @FXML NumberAxis lineXAxis;
    @FXML NumberAxis lineYAxis;

    @FXML Label amplitudeLbl;
    @FXML Label startTimeLbl;
    @FXML Label durationLbl;
    @FXML Label baseIntervalLbl;
    @FXML Label frequencySamplingLbl;

    @FXML Label signalNameLbl;
    @FXML Label signalTypeLbl;
    @FXML Label avgLbl;
    @FXML Label absoluteAvgLbl;
    @FXML Label avgSignalPowerLbl;
    @FXML Label varianceLbl;
    @FXML Label rmsLbl;

    @FXML Slider bucketsNumSlider;
    @FXML Label bucketsNumLbl;

    @FXML ScatterChart<Double, Double> scatterChart;
    @FXML BarChart<String, Integer> barChart;

    @FXML ListView<Signal> signalList;
    @FXML Label noSignalLbl;

    private Integer numOfSamples;
    private Integer numOfBits;
    private Double reconstructionFrequency;

    public void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        signalList.setItems(signalItems);
        bucketsNumLbl.setText(String.valueOf((int) bucketsNumSlider.getValue()));
//
        lineXAxis.setAutoRanging(false);
        lineXAxis.setLabel("t[s]");

        lineYAxis.setAutoRanging(false);
        lineYAxis.setLabel("A");

        barChart.setLegendVisible(false);
        scatterChart.setLegendVisible(false);

//        reconstructionChooser.setItems(Utils.reconstructionTypes);
        setupListeners();
    }

    @FXML
    private void createSignal(ActionEvent e) {
        createSignalDetailsWindow(null);
    }

    @FXML
    private void loadFromFile(ActionEvent e) {
        FileChooser.ExtensionFilter fcExtension = new FileChooser.ExtensionFilter("JSON Files", "*.json");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj sygnały");
        fileChooser.getExtensionFilters().add(fcExtension);
        File file = fileChooser.showOpenDialog(this.stage);
        if (file == null)
            return;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Signal.class, new SignalDeserializer())
                .create();

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            Signal loadedSignal = gson.fromJson(reader, Signal.class);
            signalItems.add(loadedSignal);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void saveToFile(ActionEvent e) {
        FileChooser.ExtensionFilter fcExtension = new FileChooser.ExtensionFilter("JSON Files", "*.json");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz sygnały");
        fileChooser.getExtensionFilters().add(fcExtension);
        File file = fileChooser.showSaveDialog(this.stage);
        if (file == null)
            return;

        Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();

        Gson gson = new Gson();
        String userJson = gson.toJson(selectedSignal);

        try {
            Files.write(file.toPath(), userJson.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void removeSignal(ActionEvent e) {
        Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();
        scatterChart.getData().clear();
        barChart.getData().clear();
        removeSignalInfo();

        signalItems.remove(selectedSignal);
    }

    @FXML
    private void editSignal(ActionEvent e) {
        Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();

        if (selectedSignal != null && !selectedSignal.getSignalType().equals(""))
            createSignalDetailsWindow(signalList.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void addSignal(ActionEvent e) { createSignalChooseWindow(Utils.ADD_OPERATION); }

    @FXML
    private void substractSignal(ActionEvent e) {
        createSignalChooseWindow(Utils.SUB_OPERATION);
    }

    @FXML
    private void multiplySignal(ActionEvent e) {
        createSignalChooseWindow(Utils.MUL_OPERATION);
    }

    @FXML
    private void divideSignal(ActionEvent e) {
        createSignalChooseWindow(Utils.DIV_OPERATION);
    }

    @FXML
    private void generateSamplingChart(ActionEvent e) {
        if (signalList.getSelectionModel().getSelectedItem() == null || numOfSamplesTxt.getText().isEmpty()) return;

        this.numOfSamples = Integer.valueOf(numOfSamplesTxt.getText());
        if (numOfSamples > signalList.getSelectionModel().getSelectedItem().getDuration() * signalList.getSelectionModel().getSelectedItem().getFrequencySampling()) return;

        this.reconstructionFrequency = (double) numOfSamples / signalList.getSelectionModel().getSelectedItem().getDuration();

        Map<Double, Double> samplingSignalData = SamplingUtils.generateSampleSignal(signalList.getSelectionModel().getSelectedItem(), this.numOfSamples);

        SwingCharts.createSamplingChart(swingNodeSampling, signalList.getSelectionModel().getSelectedItem().getData(), samplingSignalData);

        setSamplingStats(signalList.getSelectionModel().getSelectedItem().getData(), samplingSignalData);
    }

    @FXML
    private void calculateQuantization(ActionEvent e) {
        if (signalList.getSelectionModel().getSelectedItem() == null || numOfBitsTxt.getText().isEmpty() || numOfSamples == null) return;

        this.numOfBits = Integer.valueOf(numOfBitsTxt.getText());
        Map<Double, Double> samplingSignalData = SamplingUtils.generateSampleSignal(signalList.getSelectionModel().getSelectedItem(), this.numOfSamples);
        Map<Double, Double> quant = SamplingUtils.calculateQuantization(samplingSignalData, this.numOfBits);
        SwingCharts.createQuantizationChart(swingNodeQuantization, quant, samplingSignalData);

        setSamplingStats(samplingSignalData, quant);
    }

    @FXML
    private void reconstructeSignal(ActionEvent e) {
        if (signalList.getSelectionModel().getSelectedItem() == null ||
                reconstructionChooser.getSelectionModel().getSelectedItem() == null ||
                reconstructionFrequency == null ||
                numOfSamples == null) return;

        Map<Double, Double> samplingSignalData = SamplingUtils.generateSampleSignal(signalList.getSelectionModel().getSelectedItem(), this.numOfSamples);

        Map<Double, Double> reconstruction = null;

        switch(reconstructionChooser.getValue()) {
            case Utils.RECONSTRUCTION_ZERO: {
                reconstruction = SamplingUtils.extrapolateZeroOrderHold(samplingSignalData, this.reconstructionFrequency);
                break;
            }
            case Utils.RECONSTRUCTION_FIRST: {
                reconstruction = SamplingUtils.interpolateFirstOrderHold(samplingSignalData, this.reconstructionFrequency);
                break;
            }
            case Utils.INTERPOLATION_SINC: {
                reconstruction = SamplingUtils.sincFunction(samplingSignalData, this.reconstructionFrequency);
                break;
            }
        }

        SwingCharts.createSamplingChart(swingNodeReconstruction, signalList.getSelectionModel().getSelectedItem().getData(), reconstruction);

        setSamplingStats(signalList.getSelectionModel().getSelectedItem().getData(), reconstruction);
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
                    if (histogramTab.isSelected() || mainSignalTab.isSelected()) loadGeneralBottomPane();

                    generateLineChart(selectedSignal);
                    generateBarChart(selectedSignal, (int) bucketsNumSlider.getValue());
                }
        );

        mainSignalTab.setOnSelectionChanged(
                event -> {
                    if (mainSignalTab.isSelected()) {
                        loadGeneralBottomPane();
                    }
                }
        );

        histogramTab.setOnSelectionChanged(
                event -> {
                    if (histogramTab.isSelected()) {
                        loadGeneralBottomPane();
                    }
                }
        );

        samplingTab.setOnSelectionChanged(
                event -> {
                    if (samplingTab.isSelected()) {
                        loadSamplingBottomPane();
                    }
                }
        );

        quantTab.setOnSelectionChanged(
                event -> {
                    if (quantTab.isSelected()) {
                        loadSamplingBottomPane();
                    }
                }
        );

        reconstructionTab.setOnSelectionChanged(
                event -> {
                    if (reconstructionTab.isSelected()) {
                        loadSamplingBottomPane();
                    }
                }
        );
    }

    private void loadSamplingBottomPane() {
        signalDetailsPane.getChildren().removeAll(zad2Panel, zad1Panel);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SamplingDetails.fxml"));
            loader.setController(this);
            Pane root = loader.load();
            root.setLayoutY(297);

            signalDetailsPane.getChildren().add(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

        reconstructionChooser.setItems(Utils.reconstructionTypes);
        reconstructionChooser.setValue(Utils.reconstructionTypes.get(0));
        setSamplingInfo();
    }

    private void loadGeneralBottomPane() {
        signalDetailsPane.getChildren().removeAll(zad2Panel, zad1Panel);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GeneralStats.fxml"));
            loader.setController(this);
            Pane root = loader.load();
            root.setLayoutY(297);

            signalDetailsPane.getChildren().add(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Signal selectedSignal = signalList.getSelectionModel().getSelectedItem();
        if (selectedSignal != null) setSignalInfo(selectedSignal);
    }

    private void generateLineChart(Signal signal) {
        scatterChart.getData().clear();

        Integer startTime = signal.getStartTime();
        Double frequencySampling = signal.getFrequencySampling();
        Integer duration = signal.getDuration();
        int minValue = signal.getMinValue();
        int maxValue = signal.getMaxValue();
        double ampltiude = signal.getAmplitude();
        Map<Double, Double> signalData = signal.getData();

        lineXAxis.setLowerBound(startTime - 0.5);
        lineXAxis.setUpperBound(startTime + duration + 0.5);

        lineYAxis.setLowerBound(minValue - 0.3 * ampltiude);
        lineYAxis.setUpperBound(maxValue + 0.3 * ampltiude);

        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        XYChart.Data<Double, Double> d;
        signalData.forEach(
                (k, v) -> {
                    series.getData().add(new XYChart.Data<>(k, v));
                }
        );

        scatterChart.setTitle("Wykres liniowy");
        scatterChart.getData().add(series);
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

    private void createSignalChooseWindow(String operation) {
        if (signalList.getSelectionModel().getSelectedItem() == null)
            return;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SignalChooser.fxml"));
            SignalChooser signalChooser = new SignalChooser();
            signalChooser.setOperation(operation);
            signalChooser.setSignal(signalList.getSelectionModel().getSelectedItem());

            fxmlLoader.setController(signalChooser);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("modena-dark.css");

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Wybierz sygnał");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSignalDetailsWindow(Signal signal) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SignalDetailsWindow.fxml"));
            SignalDetailsWindow signalDetailsWindow = new SignalDetailsWindow();
            String titleText = "Utwórz nowy sygnał";

            if (signal != null) {
                signalDetailsWindow.edit(signal);
                titleText = "Edytuj sygnał";
            }

            fxmlLoader.setController(signalDetailsWindow);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("modena-dark.css");

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(titleText);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSignalInfo(Signal selectedSignal) {
        signalNameLbl.setText(selectedSignal.getName());
        signalTypeLbl.setText(selectedSignal.getSignalType());
        avgLbl.setText(String.format("%.2f", selectedSignal.getAvg()));
        absoluteAvgLbl.setText(String.format("%.2f", selectedSignal.getAbsoluteAvg()));
        avgSignalPowerLbl.setText(String.format("%.2f", selectedSignal.getAvgSignalPower()));
        varianceLbl.setText(String.format("%.2f", selectedSignal.getVariance()));
        rmsLbl.setText(String.format("%.2f", selectedSignal.getRms()));

        amplitudeLbl.setText(String.format("%.2f", (selectedSignal.getAmplitude())));
        startTimeLbl.setText(String.valueOf(selectedSignal.getStartTime()));
        durationLbl.setText(String.valueOf(selectedSignal.getDuration()));
        if (selectedSignal.getBaseInterval() != null) {
            baseIntervalLbl.setText(String.valueOf(selectedSignal.getBaseInterval()));
        } else {
            baseIntervalLbl.setText("");
        }
        frequencySamplingLbl.setText(String.format("%.2f", (selectedSignal.getFrequencySampling())));
    }

    private void removeSignalInfo() {
        signalNameLbl.setText("");
        signalTypeLbl.setText("");
        avgLbl.setText("");
        absoluteAvgLbl.setText("");
        avgSignalPowerLbl.setText("");
        varianceLbl.setText("");
        rmsLbl.setText("");

        amplitudeLbl.setText("");
        startTimeLbl.setText("");
        durationLbl.setText("");
        baseIntervalLbl.setText("");
        frequencySamplingLbl.setText("");
    }

    private void setSamplingInfo() {
        if (numOfBits != null) numOfBitsTxt.setText(String.valueOf(numOfBits));
        else numOfBitsTxt.setText("");

        if (numOfSamples != null) numOfSamplesTxt.setText(String.valueOf(numOfSamples));
        else numOfSamplesTxt.setText("");
    }

    private void setSamplingStats(Map<Double, Double> firstSignal, Map<Double, Double> secondSignal) {
        psnrLbl.setText(String.format("%.2f", ParamsUtils.calculatePSNR(firstSignal, secondSignal)));
        snrLbl.setText(String.format("%.2f", ParamsUtils.calculateSNR(firstSignal, secondSignal)));
        mseLbl.setText(String.format("%.2f", ParamsUtils.calculateMSE(firstSignal, secondSignal)));
        mdLbl.setText(String.format("%.2f", ParamsUtils.calculateMD(firstSignal, secondSignal)));
    }
}
