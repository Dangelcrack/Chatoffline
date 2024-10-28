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

/**
 * Controller responsible for managing the registration process for new users.
 */
public class RegisterController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    /**
     * Constructor that initializes the UserDAO for user management.
     */
    public RegisterController() {
        userDAO = new UserDAO();
    }

    /**
     * Handles the user registration when the 'Register' button is clicked.
     * It checks if the username already exists, creates a new user if it does not,
     * and redirects to the login screen upon successful registration.
     *
     * @param actionEvent Event triggered by the register button.
     */
    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (userDAO.findByName(username) != null) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
            return;
        }
        User newUser = new User(username, password, Collections.emptyList(), Collections.emptyList());  // Initialize friends list as empty
        userDAO.save(newUser);
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully.");
        openLoginView(actionEvent);
    }

    /**
     * Handles the action to go back to the login screen when the 'Back' button is clicked.
     *
     * @param actionEvent Event triggered by the back button.
     */
    @FXML
    public void handleBackToLogin(ActionEvent actionEvent) {
        openLoginView(actionEvent);
    }

    /**
     * Opens the login view after registration or when the user wants to go back to the login screen.
     *
     * @param actionEvent The event triggered by clicking the login or back button.
     */
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

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method is called when the registration view is opened.
     *
     * @param input Optional input when opening the registration view.
     */
    @Override
    public void onOpen(Object input) {
        // Logic when the registration view is opened
    }

    /**
     * This method is called when the registration view is closed.
     *
     * @param output Optional output when closing the registration view.
     */
    @Override
    public void onClose(Object output) {
        // Logic when the registration view is closed
    }
}
