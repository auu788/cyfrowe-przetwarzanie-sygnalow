package controller;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewSignalWindow {
    private enum VALIDATION_TYPE { TEXT, NUMBER };

    @FXML
    private Label signalFillFactorLbl;

    @FXML
    private TextField signalFillFactorTxt;

    @FXML
    private TextField signalAmplitudeTxt;

    @FXML
    private TextField signalStartTimeTxt;

    @FXML
    private TextField signalDurationTxt;

    @FXML
    private TextField signalBaseTimeTxt;

    @FXML
    private TextField signalNameTxt;

    @FXML
    private ComboBox signalTypeComboBox;

    public void create() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../view/NewSignalWindow.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
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
        ObservableList<String> signalTypes = FXCollections.observableArrayList(
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

        signalTypeComboBox.setItems(signalTypes);
        signalTypeComboBox.setValue("Szum o rozkładzie jednostajnym");

        setupValidation(signalNameTxt, VALIDATION_TYPE.TEXT);
        setupValidation(signalAmplitudeTxt, VALIDATION_TYPE.NUMBER);
        setupValidation(signalStartTimeTxt, VALIDATION_TYPE.NUMBER);
        setupValidation(signalDurationTxt, VALIDATION_TYPE.NUMBER);
        setupValidation(signalBaseTimeTxt, VALIDATION_TYPE.NUMBER);
        setupValidation(signalFillFactorTxt, VALIDATION_TYPE.NUMBER);
    }

    @FXML
    private void signalTypeChanged(ActionEvent e) {
        if (signalTypeComboBox.getValue().equals("Sygnał trójkątny") ||
                signalTypeComboBox.getValue().equals("Sygnał prostokątny")) {

            signalFillFactorTxt.setDisable(false);
            signalFillFactorLbl.setDisable(false);
        } else {
            signalFillFactorTxt.getStyleClass().remove("error");
            signalFillFactorTxt.setDisable(true);
            signalFillFactorLbl.setDisable(true);
        }
    }

    @FXML
    private void generateSignal(ActionEvent e) {
        if (signalNameTxt.getText().trim().isEmpty()) return;

        MainAppController.signalItems.add(signalNameTxt.getText());
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

    private void setupValidation(TextField textField, VALIDATION_TYPE validationType) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!textField.isDisabled())
                        validate(textField, validationType);
                }
        );
    }

    private void validate(TextField textField, VALIDATION_TYPE validationType) {
        if (textField.getText().trim().isEmpty()) {
            textField.getStyleClass().add("error");
        } else {
            if (validationType == VALIDATION_TYPE.NUMBER) {
                if (!textField.getText().matches("\\d*")) {
                    textField.getStyleClass().add("error");
                    return;
                }
            }

            textField.getStyleClass().remove("error");
        }
    }
}
