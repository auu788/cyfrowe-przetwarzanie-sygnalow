package app;

import controller.MainAppController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = null;
        Parent root = null;

        try {
            loader = new FXMLLoader(getClass().getResource("/MainApp.fxml"));
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        MainAppController mainAppController = loader.getController();
        mainAppController.init(stage);

        stage.setTitle("Generator sygnałów");
        stage.setResizable(false);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("modena-dark.css");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
