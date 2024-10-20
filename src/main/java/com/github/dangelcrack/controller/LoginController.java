package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends Controller {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;
    private MainController mainController;
    public LoginController() {
        userDAO = new UserDAO(); // Inicializar el DAO para gestionar usuarios
    }

    public static View loadFXML(Scenes scenes) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource(scenes.getURL()));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(scenes.name());
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDAO.findByName(username);

        if (user != null && user.getPassword().equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Main.fxml"));
                Parent mainView = loader.load();
                MainController mainController = loader.getController(); // Obtén la instancia del controlador
                mainController.setCurrentUser(user); // Asigna el usuario actual al controlador

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(mainView);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the main view.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect username or password.");
        }
    }


    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Register.fxml"));
            Parent registerRoot = loader.load();
            Scene registerScene = new Scene(registerRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(registerScene);
            currentStage.setTitle("Register");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMainView(ActionEvent actionEvent) {
        try {
            // Cargar la vista principal Main.fxml después del login exitoso
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Main.fxml"));
            Parent mainRoot = loader.load();
            Scene mainScene = new Scene(mainRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(mainScene);
            currentStage.setTitle("Main Screen");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void onOpen(Object input) throws IOException {
        // Lógica cuando se abre la vista de login
    }

    @Override
    public void onClose(Object output) {
        // Lógica cuando se cierra la vista de login
    }
    public void openModal(Scenes scenes, String title, Controller controller, Object input) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scenes.getURL()));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            // Pass data to the controller if needed
            if (controller != null) {
                controller.onOpen(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
