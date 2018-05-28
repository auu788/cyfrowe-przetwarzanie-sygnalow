package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.signal.*;
import model.Signal;
import model.Utils;

public class SignalDetailsWindow {
    private enum VALIDATION_TYPE { TEXT, INTEGER, DOUBLE }
    private boolean isInputValid;
    private Signal signal;
    private boolean isEdited;

    @FXML private ComboBox<String> signalTypeComboBox;
    @FXML private Button generateBtn;

    @FXML private Label signalBaseIntervalLbl;
    @FXML private Label signalFillFactorLbl;
    @FXML private Label jumpNumLbl;
    @FXML private Label jumpTimeLbl;
    @FXML private Label amplitudeProbabilityLbl;

    @FXML private TextField signalNameTxt;
    @FXML private TextField signalAmplitudeTxt;
    @FXML private TextField signalStartTimeTxt;
    @FXML private TextField signalDurationTxt;
    @FXML private TextField signalBaseIntervalTxt;
    @FXML private TextField frequencySamplingTxt;
    @FXML private TextField signalFillFactorTxt;
    @FXML private TextField jumpNumTxt;
    @FXML private TextField jumpTimeTxt;
    @FXML private TextField amplitudeProbabilityTxt;


    public void edit(Signal signal) {
        this.signal = signal;
        this.isEdited = true;
    }

    @FXML
    private void initialize() {
        signalTypeComboBox.setItems(Utils.signalTypes);

        if (isEdited) { // Edit
            generateBtn.setText("Zatwierdź edycję");
            signalTypeComboBox.setValue(signal.getSignalType());
            populateDataToEdit();
        } else {
            signalTypeComboBox.setValue(Utils.UNIFORM_NOISE);
        }


        setupAvailableFields();

        setupValidation(signalNameTxt, VALIDATION_TYPE.TEXT);
        setupValidation(signalAmplitudeTxt, VALIDATION_TYPE.DOUBLE);
        setupValidation(signalStartTimeTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(signalDurationTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(signalBaseIntervalTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(frequencySamplingTxt, VALIDATION_TYPE.DOUBLE);
        setupValidation(signalFillFactorTxt, VALIDATION_TYPE.DOUBLE);
        setupValidation(jumpNumTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(jumpTimeTxt, VALIDATION_TYPE.INTEGER);
        setupValidation(amplitudeProbabilityTxt, VALIDATION_TYPE.DOUBLE);
    }

    @FXML
    private void generateSignal(ActionEvent e) {
        isInputValid = true;
        validate(signalNameTxt, VALIDATION_TYPE.TEXT);
        validate(signalAmplitudeTxt, VALIDATION_TYPE.DOUBLE);
        validate(signalStartTimeTxt, VALIDATION_TYPE.INTEGER);
        validate(signalDurationTxt, VALIDATION_TYPE.INTEGER);
        validate(signalBaseIntervalTxt, VALIDATION_TYPE.INTEGER);
        validate(frequencySamplingTxt, VALIDATION_TYPE.DOUBLE);
        validate(signalFillFactorTxt, VALIDATION_TYPE.DOUBLE);
        validate(jumpNumTxt, VALIDATION_TYPE.INTEGER);
        validate(jumpTimeTxt, VALIDATION_TYPE.INTEGER);
        validate(amplitudeProbabilityTxt, VALIDATION_TYPE.DOUBLE);
        if (!isInputValid) return;

        if (isEdited) editSignal();
        else createSignal();

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

    @FXML
    private void signalTypeChanged(ActionEvent e) {
        setupAvailableFields();
    }

    private void populateDataToEdit() {
        if (signal.getName() != null) signalNameTxt.setText(signal.getName());
        if (signal.getAmplitude() != null) signalAmplitudeTxt.setText(String.valueOf(signal.getAmplitude()));
        if (signal.getStartTime() != null) signalStartTimeTxt.setText(String.valueOf(signal.getStartTime()));
        if (signal.getDuration() != null) signalDurationTxt.setText(String.valueOf(signal.getDuration()));
        if (signal.getBaseInterval() != null) signalBaseIntervalTxt.setText(String.valueOf(signal.getBaseInterval()));
        if (signal.getFrequencySampling() != null) frequencySamplingTxt.setText(String.valueOf(signal.getFrequencySampling()));
        if (signal.getFillFactor() != null) signalFillFactorTxt.setText(String.valueOf(signal.getFillFactor()));
        if (signal.getJumpNum() != null) jumpNumTxt.setText(String.valueOf(signal.getJumpNum()));
        if (signal.getJumpTime() != null) jumpTimeTxt.setText(String.valueOf(signal.getJumpTime()));
        if (signal.getAmplitudeProbability() != null) amplitudeProbabilityTxt.setText(String.valueOf(signal.getAmplitudeProbability()));
    }

    private void setupAvailableFields() {
        switch (signalTypeComboBox.getValue()) {
            case Utils.RECT_SIGNAL: {
            }
            case Utils.SYMMETRIC_RECT_SIGNAL: {
            }
            case Utils.TRIANGLE_SIGNAL: {
                jumpNumTxt.getStyleClass().remove("error");
                jumpTimeTxt.getStyleClass().remove("error");
                amplitudeProbabilityTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(false);
                signalBaseIntervalLbl.setDisable(false);
                signalFillFactorTxt.setDisable(false);
                signalFillFactorLbl.setDisable(false);
                jumpNumTxt.setDisable(true);
                jumpNumLbl.setDisable(true);
                jumpTimeTxt.setDisable(true);
                jumpTimeLbl.setDisable(true);
                amplitudeProbabilityTxt.setDisable(true);
                amplitudeProbabilityLbl.setDisable(true);
                break;
            }
            case Utils.UNIT_PULSE: {
                signalFillFactorTxt.getStyleClass().remove("error");
                jumpTimeTxt.getStyleClass().remove("error");
                amplitudeProbabilityTxt.getStyleClass().remove("error");
                signalBaseIntervalTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(true);
                signalBaseIntervalLbl.setDisable(true);
                signalFillFactorTxt.setDisable(true);
                signalFillFactorLbl.setDisable(true);
                jumpNumTxt.setDisable(false);
                jumpNumLbl.setDisable(false);
                jumpTimeTxt.setDisable(true);
                jumpTimeLbl.setDisable(true);
                amplitudeProbabilityTxt.setDisable(true);
                amplitudeProbabilityLbl.setDisable(true);
                break;
            }
            case Utils.PULSE_NOISE: {
                signalFillFactorTxt.getStyleClass().remove("error");
                jumpNumTxt.getStyleClass().remove("error");
                jumpTimeTxt.getStyleClass().remove("error");
                signalBaseIntervalTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(true);
                signalBaseIntervalLbl.setDisable(true);
                signalFillFactorTxt.setDisable(true);
                signalFillFactorLbl.setDisable(true);
                jumpNumTxt.setDisable(true);
                jumpNumLbl.setDisable(true);
                jumpTimeTxt.setDisable(true);
                jumpTimeLbl.setDisable(true);
                amplitudeProbabilityTxt.setDisable(false);
                amplitudeProbabilityLbl.setDisable(false);
                break;
            }
            case Utils.UNIT_JUMP_SIGNAL: {
                signalFillFactorTxt.getStyleClass().remove("error");
                jumpNumTxt.getStyleClass().remove("error");
                amplitudeProbabilityTxt.getStyleClass().remove("error");
                signalBaseIntervalTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(true);
                signalBaseIntervalLbl.setDisable(true);
                signalFillFactorTxt.setDisable(true);
                signalFillFactorLbl.setDisable(true);
                jumpNumTxt.setDisable(true);
                jumpNumLbl.setDisable(true);
                jumpTimeTxt.setDisable(false);
                jumpTimeLbl.setDisable(false);
                amplitudeProbabilityTxt.setDisable(true);
                amplitudeProbabilityLbl.setDisable(true);
                break;
            }
            case Utils.SINE_SIGNAL: {
            }
            case Utils.ONE_HALF_SINE_SIGNAL: {
            }
            case Utils.TWO_HALF_SINE_SIGNAL: {
                signalFillFactorTxt.getStyleClass().remove("error");
                jumpNumTxt.getStyleClass().remove("error");
                jumpTimeTxt.getStyleClass().remove("error");
                amplitudeProbabilityTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(false);
                signalBaseIntervalLbl.setDisable(false);
                signalFillFactorTxt.setDisable(true);
                signalFillFactorLbl.setDisable(true);
                jumpNumTxt.setDisable(true);
                jumpNumLbl.setDisable(true);
                jumpTimeTxt.setDisable(true);
                jumpTimeLbl.setDisable(true);
                amplitudeProbabilityTxt.setDisable(true);
                amplitudeProbabilityLbl.setDisable(true);
                break;
            }
            default: {
                signalFillFactorTxt.getStyleClass().remove("error");
                jumpNumTxt.getStyleClass().remove("error");
                jumpTimeTxt.getStyleClass().remove("error");
                amplitudeProbabilityTxt.getStyleClass().remove("error");

                signalBaseIntervalTxt.setDisable(true);
                signalBaseIntervalLbl.setDisable(true);
                signalFillFactorTxt.setDisable(true);
                signalFillFactorLbl.setDisable(true);
                jumpNumTxt.setDisable(true);
                jumpNumLbl.setDisable(true);
                jumpTimeTxt.setDisable(true);
                jumpTimeLbl.setDisable(true);
                amplitudeProbabilityTxt.setDisable(true);
                amplitudeProbabilityLbl.setDisable(true);
            }
        }
    }

    private void editSignal() {
//        this.signal.setName(this.signalNameTxt.getText());
//        this.signal.setSignalType(this.signalTypeComboBox.getValue());
//        this.signal.setAmplitude(Double.valueOf(this.signalAmplitudeTxt.getText()));
//        this.signal.setStartTime(Integer.valueOf(this.signalStartTimeTxt.getText()));
//        this.signal.setDuration(Integer.valueOf(this.signalDurationTxt.getText()));
//        this.signal.setFrequencySampling(Double.valueOf(this.frequencySamplingTxt.getText()));

        MainAppController.signalItems.remove(this.signal);
        createSignal();
    }

    private void createSignal() {
        switch (signalTypeComboBox.getValue()) {
            case Utils.UNIFORM_NOISE: {
                MainAppController.signalItems.add(
                        new Uniform(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText())));
                break;
            }
            case Utils.GAUSSIAN_NOISE: {
                MainAppController.signalItems.add(
                        new Gaussian(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText())));
                break;
            }
            case Utils.SINE_SIGNAL: {
                MainAppController.signalItems.add(
                        new Sinusoidal(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText())));
                break;
            }
            case Utils.ONE_HALF_SINE_SIGNAL: {
                MainAppController.signalItems.add(
                        new SinusoidalOneHalf(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText())));
                break;
            }
            case Utils.TWO_HALF_SINE_SIGNAL: {
                MainAppController.signalItems.add(
                        new SinusoidalTwoHalf(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText())));
                break;
            }
            case Utils.RECT_SIGNAL: {
                MainAppController.signalItems.add(
                        new Rectangular(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText()),
                                Double.valueOf(this.signalFillFactorTxt.getText())));
                break;
            }
            case Utils.SYMMETRIC_RECT_SIGNAL: {
                MainAppController.signalItems.add(
                        new RectangularSymmetric(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText()),
                                Double.valueOf(this.signalFillFactorTxt.getText())));
                break;
            }
            case Utils.TRIANGLE_SIGNAL: {
                MainAppController.signalItems.add(
                        new Triangular(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.signalBaseIntervalTxt.getText()),
                                Double.valueOf(this.signalFillFactorTxt.getText())));
                break;
            }
            case Utils.UNIT_JUMP_SIGNAL: {
                MainAppController.signalItems.add(
                        new UnitJump(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Integer.valueOf(this.jumpTimeTxt.getText())));
                break;
            }
            case Utils.PULSE_NOISE: {
                MainAppController.signalItems.add(
                        new Pulse(
                                this.signalTypeComboBox.getValue(),
                                this.signalNameTxt.getText(),
                                Double.valueOf(this.signalAmplitudeTxt.getText()),
                                Integer.valueOf(this.signalStartTimeTxt.getText()),
                                Integer.valueOf(this.signalDurationTxt.getText()),
                                Double.valueOf(this.frequencySamplingTxt.getText()),
                                Double.valueOf(this.amplitudeProbabilityTxt.getText())));
                break;
            }
        }
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
