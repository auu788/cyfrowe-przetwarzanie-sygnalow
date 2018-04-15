package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Signal;

import java.util.Map;

public class MainAppController {
    public static ObservableList<Signal> signalItems = FXCollections.observableArrayList();

    private Stage stage;

    @FXML Slider bucketsNumSlider;
    @FXML Label bucketsNumLbl;
    @FXML LineChart<String, Double> lineChart;
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
                }
        );

        signalList.setOnMouseClicked(
                event -> generateLineChart(signalList.getSelectionModel().getSelectedItem())
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

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (Map.Entry<Integer, Double> entry : signal.getData().entrySet()) {
            XYChart.Data<String, Double> d = new XYChart.Data<>(
                    entry.getKey().toString(), entry.getValue());

            series.getData().add(d);
        }

        lineChart.setTitle("Wykres liniowy");
        lineChart.getData().add(series);
    }
}

