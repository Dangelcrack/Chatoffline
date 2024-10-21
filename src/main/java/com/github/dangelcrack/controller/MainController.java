package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.repo.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import com.github.dangelcrack.App;
import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.model.entity.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends Controller {

    @FXML
    private ListView<User> friendsListView;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button startChatButton;
    @FXML
    private Button logoutButton;

    private UserDAO userDAO;
    private List<User> friendsList;
    private User currentUser;
    public MainController() {
        this.friendsList = new ArrayList<>();
        this.userDAO = new UserDAO(); // Inicializar el UserDAO
    }

    @FXML
    public void initialize() {
        currentUser = Sesion.getInstance().getUser();
        loadFriendsList();

    }

    private void loadFriendsList() {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }
        friendsList.clear(); // Limpia la lista antes de cargar los amigos
        List<User> friends = userDAO.getFriendsForUser(currentUser); // Método para obtener amigos
        friendsList.addAll(friends);
        friendsListView.getItems().setAll(friendsList); // Actualiza la vista con los amigos
    }
    @FXML
    public void handleFriendSelected(MouseEvent event) {
        if (event.getClickCount() == 2) { // Doble clic para iniciar el chat
            User user = friendsListView.getSelectionModel().getSelectedItem();

            if (user != null) {
                User selectedFriend = userDAO.findByName(user.getUsername());
                if (selectedFriend != null) {
                    startChatWithFriend(selectedFriend.getUsername()); // Envía el objeto User al iniciar el chat
                }
            } else {
                System.out.println("No friend selected.");
            }
        }
    }
    public void openModal(Scenes scenes, String title, Controller controller, Object input) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scenes.getURL())); // Usa scenes.getURL()
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            if (controller != null) {
                controller.onOpen(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addFriend() throws IOException {
        openModal(Scenes.ADDFRIEND, "Adding a friend", this, null);
    }

    @FXML
    public void handleStartChatButton(ActionEvent actionEvent) {
        User selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            startChatWithFriend(selectedFriend.getUsername());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a friend to chat with.");
        }
    }

    @FXML
    public void startChatWithFriend(String friendName) {
        showAlert(Alert.AlertType.INFORMATION, "Chat Started", "Starting chat with " + friendName + "...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Chat.fxml"));
            Parent chatRoot = loader.load();
            ChatController chatController = loader.getController();
            chatController.setFriendName(friendName); // Establecer el nombre del amigo
            Scene chatScene = new Scene(chatRoot);
            Stage currentStage = (Stage) startChatButton.getScene().getWindow();
            currentStage.setScene(chatScene);
            currentStage.setTitle("Chat with " + friendName);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to start chat.");
        }
    }

    @FXML
    public void handleLogout() {
        try {
            // Cerrar la sesión
            Sesion.closeSession();

            // Cargar la vista de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Login.fxml"));
            Parent loginRoot = loader.load();

            // Obtener la escena actual y cambiarla a la nueva escena
            Stage currentStage = (Stage) logoutButton.getScene().getWindow(); // Asegúrate de que "logoutButton" sea accesible
            Scene loginScene = new Scene(loginRoot);
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login"); // Título de la ventana de inicio de sesión
            currentStage.show();

            // Si necesitas obtener el controlador de la escena de inicio de sesión
            LoginController loginController = loader.getController();
            // Puedes realizar cualquier inicialización necesaria en el controlador de inicio de sesión aquí.

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login scene.");
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
        // Lógica cuando se abre la vista principal
    }

    @Override
    public void onClose(Object output) {
        // Lógica cuando se cierra la vista principal
    }
}
