package controller;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MainAppController {
    public static ObservableList<String> signalItems = FXCollections.observableArrayList();

    private Stage stage;

    @FXML
    ListView<String> signalList;

    @FXML
    Label noSignalLbl;

    public void init(Stage stage) {
        this.stage = stage;

        signalItems.addListener(
                (ListChangeListener<String>) c -> {
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
}

