package com.github.dangelcrack;

import com.github.dangelcrack.controller.AppController;
import com.github.dangelcrack.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public  static Scene scene;
    public  static  Stage stage;
    public  static AppController currentControler;

    @Override
    public void start(Stage stage) throws IOException {
        View view = AppController.loadFXML(Scenes.ROOT);
    }

    public static void main(String[] args) {
        launch();
    }
}