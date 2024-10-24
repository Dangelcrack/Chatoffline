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

/**
 * Controller responsible for handling the main view where the user can manage friends,
 * start chats, and log out.
 */
public class MainController extends Controller {

    @FXML
    private ListView<User> friendsListView;
    @FXML
    private Button startChatButton;
    @FXML
    private Button logoutButton;

    private UserDAO userDAO;
    private List<User> friendsList;
    private User currentUser;

    /**
     * Constructor that initializes the UserDAO and friends list.
     */
    public MainController() {
        this.friendsList = new ArrayList<>();
        this.userDAO = new UserDAO();  // Initialize the UserDAO
    }

    /**
     * This method is called to initialize the controller after the root element has been completely processed.
     * It sets the current user and loads their friends list.
     */
    @FXML
    public void initialize() {
        currentUser = Sesion.getInstance().getUser();
        loadFriendsList();
    }

    /**
     * Loads the list of friends for the current user and populates the ListView.
     */
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

    /**
     * Handles the selection of a friend from the ListView. If the user double-clicks on a friend,
     * it starts a chat with that friend.
     *
     * @param event MouseEvent that triggered the selection (double-click).
     */
    @FXML
    public void handleFriendSelected(MouseEvent event) {
        if (event.getClickCount() == 2) {  // Double-click to start a chat
            User user = friendsListView.getSelectionModel().getSelectedItem();

            if (user != null) {
                User selectedFriend = userDAO.findByName(user.getUsername());
                if (selectedFriend != null) {
                    startChatWithFriend(selectedFriend.getUsername());  // Start chat with the selected friend
                }
            }
        }
    }

    /**
     * Handles the action to add a new friend by opening the add friend modal.
     */
    @FXML
    public void addFriend() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.ADDFRIEND.getURL()));
        Parent root = loader.load();
        AddFriendController addFriendController = loader.getController();
        addFriendController.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Adding a Friend");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    /**
     * Handles the action to start a chat when the "Start Chat" button is clicked.
     *
     */
    @FXML
    public void handleStartChatButton() {
        User selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            startChatWithFriend(selectedFriend.getUsername());
        }
    }

    /**
     * Starts a chat with the selected friend by opening the chat window.
     *
     * @param friendName The name of the friend to chat with.
     */
    @FXML
    public void startChatWithFriend(String friendName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.CHAT.getURL()));
            Parent chatRoot = loader.load();
            ChatController chatController = loader.getController();
            chatController.onOpen(new Object[]{currentUser, friendName});  // Pass current user and friend to the chat
            Scene chatScene = new Scene(chatRoot);
            Stage currentStage = (Stage) startChatButton.getScene().getWindow();
            currentStage.setScene(chatScene);
            currentStage.setTitle("Chat with " + friendName);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the logout action. Ends the current session and returns to the login view.
     */
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

    /**
     * Method called when the main view is opened.
     *
     * @param input Optional input passed to the main controller.
     */
    @Override
    public void onOpen(Object input) {
    }

    /**
     * Method called when the main view is closed.
     *
     * @param output Optional output returned when the main view is closed.
     */
    @Override
    public void onClose(Object output) {
    }
}
