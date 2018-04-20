package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Signal;

import java.net.URL;
import java.util.ResourceBundle;

public class SignalChooser implements Initializable {
    private String operation;

    @FXML
    private ListView<Signal> chooserSignalsList;

    @FXML
    private Button chooseBtn;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setSignalItems(ObservableList<Signal> signalItems) {
        chooserSignalsList.setItems(signalItems);
    }

    @FXML
    private void choose(ActionEvent e) {
        System.out.println("Zrób akcję, TODO");
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
        chooserSignalsList.setItems(MainAppController.signalItems);
    }
}
