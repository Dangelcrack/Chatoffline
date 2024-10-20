package com.github.dangelcrack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Controller {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button startChatButton;

    @FXML
    private Button logoutButton;

    private String username;

    // Método para establecer el nombre de usuario y actualizar el mensaje de bienvenida
    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    public void handleStartChat(ActionEvent actionEvent) {
        // Lógica para iniciar el chat
        showAlert(Alert.AlertType.INFORMATION, "Chat Started", "Starting the chat...");

        // Cargar la vista del chat (asumiendo que tienes una vista llamada Chat.fxml)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Chat.fxml"));
            Parent chatRoot = loader.load();
            Scene chatScene = new Scene(chatRoot);
            Stage currentStage = (Stage) startChatButton.getScene().getWindow();
            currentStage.setScene(chatScene);
            currentStage.setTitle("Chat");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start chat.");
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        // Lógica para cerrar la sesión
        showAlert(Alert.AlertType.INFORMATION, "Logged Out", "You have logged out successfully.");

        // Volver a la vista de inicio de sesión
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to login.");
        }
    }

    @Override
    public void onOpen(Object input) {
        if (input instanceof String) {
            setUsername((String) input);
        }
    }

    @Override
    public void onClose(Object output) {
        // Lógica cuando se cierra la vista principal
    }

    // Método para mostrar alertas
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
