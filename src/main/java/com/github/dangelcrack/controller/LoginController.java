package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.model.repo.Sesion;
import com.github.dangelcrack.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            Sesion.iniciateSesion(user);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
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
}
