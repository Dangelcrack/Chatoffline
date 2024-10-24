package com.github.dangelcrack;

import com.github.dangelcrack.controller.LoginController;
import com.github.dangelcrack.model.entity.Scenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The App class serves as the main entry point for the JavaFX application.
 * It initializes the application and loads the initial user interface.
 */
public class App extends Application {
    public static LoginController currentControler;

    /**
     * The start method is called when the application is launched.
     * It sets up the primary stage and loads the login view.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.LOGIN.getURL()));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Main Screen");
        try {
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/github/dangelcrack/resources/WhatsApp2.png"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.show();
        currentControler = loader.getController();
    }

    /**
     * The main method is the entry point of the JavaFX application.
     * It launches the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
