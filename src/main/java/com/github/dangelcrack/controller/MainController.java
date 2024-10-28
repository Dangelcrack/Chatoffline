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
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        currentUser = Sesion.getInstance().getUser();
        loadFriendsList();
    }

    public void loadFriendsList() {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }
        friendsList.clear();
        List<User> friends = userDAO.getFriendsForUser(currentUser);
        friendsList.addAll(friends);
        friendsListView.getItems().setAll(friendsList);
    }

    @FXML
    public void handleFriendSelected(MouseEvent event) {
        if (event.getClickCount() == 2) {  // Doble clic para iniciar un chat
            User user = friendsListView.getSelectionModel().getSelectedItem();

            if (user != null) {
                User selectedFriend = userDAO.findByName(user.getUsername());
                if (selectedFriend != null) {
                    startChatWithFriend(selectedFriend.getUsername());
                }
            }
        }
    }

    @FXML
    public void addFriend() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.ADDFRIEND.getURL()));
            Parent root = loader.load();

            AddFriendController addFriendController = loader.getController();
            addFriendController.setMainController(this);

            Stage currentStage = (Stage) addFriendButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Adding a Friend");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStartChatButton() {
        User selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            startChatWithFriend(selectedFriend.getUsername());
        }
    }

    @FXML
    public void startChatWithFriend(String friendName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.CHAT.getURL()));
            Parent chatRoot = loader.load();
            ChatController chatController = loader.getController();
            chatController.onOpen(new Object[]{currentUser, friendName});
            Scene chatScene = new Scene(chatRoot);
            Stage currentStage = (Stage) startChatButton.getScene().getWindow();
            currentStage.setScene(chatScene);
            currentStage.setTitle("Chat with " + friendName);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        try {
            Sesion.closeSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.LOGIN.getURL()));
            Parent loginRoot = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene loginScene = new Scene(loginRoot);
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(Object input) {
    }

    @Override
    public void onClose(Object output) {
    }
}
