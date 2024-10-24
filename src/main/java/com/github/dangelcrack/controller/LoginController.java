package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.model.repo.Sesion;
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

/**
 * Controller responsible for handling user login and registration views.
 */
public class LoginController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    /**
     * Constructor initializes the UserDAO instance to manage users.
     */
    public LoginController() {
        userDAO = new UserDAO(); // Initialize the DAO for managing users
    }

    /**
     * Handles the login action. Authenticates the user and, if successful,
     * transitions to the main application view.
     *
     * @param actionEvent The event triggered by pressing the login button.
     */
    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            Sesion.iniciateSesion(user);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.MAIN.getURL()));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Usuario: " + username);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    /**
     * Handles the registration action. Transitions to the registration view when the
     * user clicks the register button.
     *
     * @param actionEvent The event triggered by pressing the register button.
     */
    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.REGISTER.getURL()));
            Parent registerRoot = loader.load();
            Scene registerScene = new Scene(registerRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(registerScene);
            currentStage.setTitle("Register");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Handle any exceptions during the view loading
        }
    }

    /**
     * Displays an alert to the user with the specified title and message.
     *
     * @param alertType The type of alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method called when the login view is opened.
     *
     * @param input Optional input passed to the login controller.
     * @throws IOException In case any I/O errors occur during the open operation.
     */
    @Override
    public void onOpen(Object input){
    }

    /**
     * Method called when the login view is closed.
     *
     * @param output Optional output returned when the login view is closed.
     */
    @Override
    public void onClose(Object output) {
    }
}
