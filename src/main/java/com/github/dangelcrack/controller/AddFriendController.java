package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.model.repo.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddFriendController extends Controller implements Initializable {
    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<String> usersListView;

    private UserDAO userDAO;
    private ObservableList<String> usersList;
    private User currentUser;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onOpen(Object input) {
    }

    @Override
    public void onClose(Object output) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currentUser = Sesion.getInstance().getUser();
        userDAO = new UserDAO();
        usersList = FXCollections.observableArrayList();
        usersListView.setItems(usersList);
        loadUsersIntoListView(userDAO.findAll());
    }

    private void loadUsersIntoListView(List<User> users) {
        usersList.clear();
        // Filtra los usuarios antes de añadirlos a la lista
        List<User> filteredUsers = users.stream()
                .filter(user -> !user.getUsername().equalsIgnoreCase(currentUser.getUsername())) // Excluir al usuario actual
                .filter(user -> !currentUser.getFriends().contains(user)) // Excluir amigos
                .collect(Collectors.toList());

        for (User user : filteredUsers) {
            usersList.add(user.getUsername());
        }
    }

    @FXML
    public void handleSearchFriend() {
        currentUser = Sesion.getInstance().getUser();

        String searchQuery = searchTextField.getText().trim().toLowerCase();
        List<User> allUsers = userDAO.findAll();
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> !user.getUsername().equalsIgnoreCase(currentUser.getUsername()))
                .filter(user -> !currentUser.getFriends().contains(user))
                .filter(user -> user.getUsername().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());

        loadUsersIntoListView(filteredUsers);
    }

    @FXML
    private void addFriendAndCloseWindow(Event event) {
        String selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !selectedUser.isEmpty()) {
            User friend = userDAO.findByName(selectedUser); // Busca al amigo por nombre
            if (friend != null && !currentUser.getFriends().contains(friend)) {
                // Agrega al amigo en la lista de amigos del usuario actual
                currentUser.addFriend(friend);

                // También agrega al usuario actual en la lista de amigos del amigo (bidireccional)
                friend.addFriend(currentUser);

                // Actualiza el almacenamiento para ambos usuarios
                userDAO.save(currentUser); // Guarda el usuario actual con la lista de amigos actualizada
                userDAO.save(friend);      // Guarda al amigo con la lista de amigos actualizada

                // Recargar la lista de amigos en la interfaz
                mainController.loadFriendsList();
            }
        }

        // Regresa a la escena principal en lugar de cerrar la ventana
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/dangelcrack/view/Main.fxml"));
            Parent mainRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(mainRoot));
            currentStage.setTitle("Main");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
