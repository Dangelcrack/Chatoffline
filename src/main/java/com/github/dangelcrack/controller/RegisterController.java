package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.User;
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
import java.util.Collections;

public class RegisterController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    public RegisterController() {
        userDAO = new UserDAO(); // Inicializamos el DAO para gestionar usuarios
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verificar si el usuario ya existe
        if (userDAO.findByName(username) != null) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
            return;
        }

        // Registrar nuevo usuario
        User newUser = new User(username, password, Collections.emptyList(), Collections.emptyList()); // Inicializar amigos como lista vacía
        userDAO.save(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully.");

        // Redirigir a la pantalla de inicio de sesión después del registro
        openLoginView(actionEvent);
    }

    @FXML
    public void handleBackToLogin(ActionEvent actionEvent) {
        openLoginView(actionEvent);
    }

    private void openLoginView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
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
    public void onOpen(Object input) {
        // Lógica cuando se abre la vista de registro
    }

    @Override
    public void onClose(Object output) {
        // Lógica cuando se cierra la vista de registro
    }
}
