package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SignalChooser implements Initializable {
    private String operation;
    private Signal signal;

    @FXML
    private ListView<Signal> chooserSignalsList;

    @FXML
    private Button chooseBtn;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    @FXML
    private void choose(ActionEvent e) {
        Signal productSignal = null;

        if (this.operation == Utils.ADD_OPERATION) {
            productSignal = ArithmeticUtils.addSignals(signal, chooserSignalsList.getSelectionModel().getSelectedItem());
        } else if (this.operation == Utils.SUB_OPERATION) {
            productSignal = ArithmeticUtils.subtractSignals(signal, chooserSignalsList.getSelectionModel().getSelectedItem());
        } else if (this.operation == Utils.MUL_OPERATION) {
            productSignal = ArithmeticUtils.multiplySignals(signal, chooserSignalsList.getSelectionModel().getSelectedItem());
        } else if (this.operation == Utils.DIV_OPERATION) {
            productSignal = ArithmeticUtils.divideSignals(signal, chooserSignalsList.getSelectionModel().getSelectedItem());
        } else if (this.operation == Utils.WEAVE) {
            double frequency = (double) MainAppController.numOfSamples / signal.getDuration();
            productSignal = FilteringUtils.weave( signal, chooserSignalsList.getSelectionModel().getSelectedItem(), frequency, MainAppController.numOfSamples );
        }

        MainAppController.signalItems.add( productSignal );

        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent e) {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chooseBtn.setText(operation);
        ObservableList<Signal> validSignals = FXCollections.observableArrayList();

        MainAppController.signalItems.forEach(
                sig -> {
                    if (signal.getFrequencySampling().equals(sig.getFrequencySampling())) {
                        validSignals.add(sig);
                    }
                }
        );

        chooserSignalsList.setItems(validSignals);
    }
}
