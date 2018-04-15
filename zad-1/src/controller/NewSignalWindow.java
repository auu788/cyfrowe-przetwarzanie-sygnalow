package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class NewSignalWindow {
    private enum VALIDATION_TYPE { TEXT, INTEGER, DOUBLE }
    private boolean isInputValid;

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
    private ComboBox<String> signalTypeComboBox;

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
        setupValidation(signalAmplitudeTxt, VALIDATION_TYPE.DOUBLE);
        setupValidation(signalStartTimeTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(signalDurationTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(signalBaseTimeTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(signalFillFactorTxt, VALIDATION_TYPE.INTEGER);
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
        isInputValid = true;
        validate(signalNameTxt, VALIDATION_TYPE.TEXT);
        validate(signalAmplitudeTxt, VALIDATION_TYPE.DOUBLE);
        validate(signalStartTimeTxt, VALIDATION_TYPE.INTEGER);
        validate(signalDurationTxt, VALIDATION_TYPE.INTEGER);
        validate(signalBaseTimeTxt, VALIDATION_TYPE.INTEGER);
        validate(signalFillFactorTxt, VALIDATION_TYPE.INTEGER);
        if (!isInputValid) return;

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
        if (textField.isDisabled())
            return;

        ObservableList<String> textFieldStyle = textField.getStyleClass();

        if (textField.getText().trim().isEmpty() && !textFieldStyle.contains("error")) {
            textFieldStyle.add("error");
        }
        else {
            switch (validationType) {
                case INTEGER: {
                    if (!isInteger(textField.getText())) {
                        if (!textFieldStyle.contains("error")) {
                            textFieldStyle.add("error");
                        }
                    } else {
                        textFieldStyle.remove("error");
                        return;
                    }
                    break;
                }
                case DOUBLE: {
                    if (!isDouble(textField.getText())) {
                        if (!textFieldStyle.contains("error")) {
                            textFieldStyle.add("error");
                        }
                    } else {
                        textFieldStyle.remove("error");
                        return;
                    }
                    break;
                }
                case TEXT: {
                    if (!textField.getText().isEmpty()) {
                        textFieldStyle.remove("error");
                        return;
                    }
                }
            }
        }

        isInputValid = false;
    }

    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        }
        catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
