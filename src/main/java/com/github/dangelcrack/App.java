package com.github.dangelcrack;

import com.github.dangelcrack.controller.LoginController;
import com.github.dangelcrack.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static LoginController currentControler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Login.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Main Screen");
        primaryStage.show();
        currentControler = loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
