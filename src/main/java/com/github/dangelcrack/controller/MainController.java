package com.github.dangelcrack.controller;
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
    private User currentUser; // Campo para almacenar el usuario actual

    public MainController() {
        this.friendsList = new ArrayList<>();
        this.userDAO = new UserDAO(); // Inicializar el UserDAO
    }

    // Método para establecer el usuario actual
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadFriendsList(); // Cargar la lista de amigos al establecer el usuario
    }

    @FXML
    public void initialize() {
        // Inicialización adicional si es necesario
    }

    private void loadFriendsList() {
        if (currentUser == null) {
            System.out.println("Current user is not set.");
            return; // Maneja el caso si currentUser es null
        }
        friendsList.clear(); // Limpia la lista antes de cargar los amigos
        List<User> friends = userDAO.getFriendsForUser(currentUser); // Método hipotético para obtener amigos
        friendsList.addAll(friends);
        // Actualiza el ListView con solo los nombres de los amigos
        friendsListView.getItems().setAll(friendsList);
    }

    @FXML
    public void handleFriendSelected(MouseEvent event) {
        if (event.getClickCount() == 2) { // Doble clic para iniciar el chat
            User user = friendsListView.getSelectionModel().getSelectedItem(); // Obtiene el nombre de usuario

            if (user != null) {
                User selectedFriend = userDAO.findByName(user.getUsername()); // Método para obtener el objeto User por nombre
                if (selectedFriend != null) {
                    startChatWithFriend(selectedFriend.getUsername()); // Envía el objeto User al iniciar el chat
                }
            } else {
                System.out.println("No friend selected.");
            }
        }
    }

    @FXML
    public void addFriend() throws IOException {
        App.currentControler.openModal(Scenes.ADDFRIEND, "Adding a friend", this, null);
    }

    @FXML
    public void handleStartChatButton(ActionEvent actionEvent) {
        // Obtener el amigo seleccionado
        User selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            // Llamar a startChatWithFriend con el nombre del amigo seleccionado
            startChatWithFriend(selectedFriend.getUsername());
        } else {
            // Mostrar alerta si no hay un amigo seleccionado
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a friend to chat with.");
        }
    }

    @FXML
    public void startChatWithFriend(String friendName) {
        // Lógica para iniciar un chat con el amigo seleccionado
        showAlert(Alert.AlertType.INFORMATION, "Chat Started", "Starting chat with " + friendName + "...");

        // Cargar la vista del chat
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Chat.fxml"));
            Parent chatRoot = loader.load();
            ChatController chatController = loader.getController();
            chatController.setFriendName(friendName); // Establecer el amigo con el que se está chateando
            Scene chatScene = new Scene(chatRoot);
            Stage currentStage = (Stage) startChatButton.getScene().getWindow(); // Asegúrate de que el botón esté disponible
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
        // Lógica para cerrar la sesión
        showAlert(Alert.AlertType.INFORMATION, "Logged Out", "You have logged out successfully.");
        // Volver a la vista de inicio de sesión
        // ... (lógica de inicio de sesión)
    }

    // Método para mostrar alertas
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void onOpen(Object input) {
        // Aquí puedes manejar la lógica cuando se abre la vista principal
        // Puedes actualizar la vista o realizar acciones basadas en el objeto de entrada si es necesario
    }

    @Override
    public void onClose(Object output) {
        // Aquí puedes manejar la lógica cuando se cierra la vista principal
    }
    // Método para abrir un modal

}
