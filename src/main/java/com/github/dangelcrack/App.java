package com.github.dangelcrack;

import com.github.dangelcrack.controller.AppController;
import com.github.dangelcrack.controller.LoginController;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static Scene scene;
    public static Stage stage;
    public static AppController currentControler;

    @Override
    public void start(Stage stage) throws IOException {
        View view = AppController.loadFXML(Scenes.LOGIN);
        scene = new Scene(view.scene, 1280, 720);

        // Verificar si el controlador es una instancia de AppController
        if (view.controller instanceof AppController) {
            currentControler = (AppController) view.controller;
            currentControler.onOpen(null);
        } else if (view.controller instanceof LoginController) {
            LoginController loginController = (LoginController) view.controller;
            loginController.onOpen(null);
        }

        stage.setScene(scene);
        stage.setTitle("Correo");
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}