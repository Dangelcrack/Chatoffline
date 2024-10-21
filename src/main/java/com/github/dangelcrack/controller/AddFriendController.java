package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.model.repo.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * The AddFriendController class is responsible for handling the UI logic for adding friends in the application.
 * It implements the Initializable interface to initialize the controller after its root element has been completely processed.
 */
public class AddFriendController extends Controller implements Initializable {

    @FXML
    private TextField searchTextField; // TextField for user search input

    @FXML
    private ListView<String> usersListView; // ListView to display the list of users

    private UserDAO userDAO; // DAO to access user data

    private ObservableList<String> usersList; // Observable list to manage the ListView items
    private User currentUser;
    private MainController controller; // Reference to the main controller

    /**
     * This method is called when the controller is opened. It sets the controller reference.
     */
    @Override
    public void onOpen(Object input) {
        this.controller = (MainController) input; // Assume MainController is used here, replace with the actual controller if needed
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    /**
     * This method is called when the controller is closed. Currently, it has no implementation.
     */
    @Override
    public void onClose(Object output) {
        // No specific actions required on close
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currentUser = Sesion.getInstance().getUser();
        userDAO = new UserDAO(); // Initialize the DAO to load users
        usersList = FXCollections.observableArrayList(); // Initialize the observable list for the ListView
        usersListView.setItems(usersList); // Set the ListView's items to the observable list
        loadUsersIntoListView(userDAO.findAll()); // Load all users initially into the ListView
    }

    /**
     * Loads the given list of users into the ListView.
     * @param users The list of users to be displayed.
     */
    private void loadUsersIntoListView(List<User> users) {
        usersList.clear(); // Clear the current list before adding new items
        for (User user : users) {
            usersList.add(user.getUsername()); // Add each user's username to the observable list
        }
    }

    /**
     * Handles the search action. Filters the users by the search input and updates the ListView.
     */
    @FXML
    public void handleSearchFriend() {
        if(currentUser!=null){
            currentUser = Sesion.getInstance().getUser();
        }
        String searchQuery = searchTextField.getText().trim().toLowerCase(); // Get the search query and convert to lowercase
        if (!searchQuery.isEmpty()) {
            // Filter the users whose username contains the search query
            List<User> filteredUsers = userDAO.findAll().stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
            loadUsersIntoListView(filteredUsers);
        } else {
            loadUsersIntoListView(userDAO.findAll());
        }
    }
    @FXML
    private void addFriendAndCloseWindow(Event event) {
        String selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !selectedUser.isEmpty()) {
            User friend = userDAO.findByName(selectedUser);
            if (friend != null && !currentUser.getFriends().contains(friend)) {
                this.currentUser.addFriend(friend);
                friend.addFriend(this.currentUser);
                userDAO.save(friend);
                userDAO.save(currentUser);
                System.out.println("Amigo " + friend.getUsername() + " añadido correctamente.");
            } else {
                System.out.println("El usuario ya es tu amigo o no existe.");
            }
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    /**
     * Handles the closing of the window. It closes the window when triggered.
     */
    @FXML
    private void closeWindow(Event event) {
        ((Node) (event.getSource())).getScene().getWindow().hide(); // Close the current window
    }
}
