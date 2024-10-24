package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.MessageDAO;
import com.github.dangelcrack.model.entity.Message;
import com.github.dangelcrack.model.entity.Scenes;
import com.github.dangelcrack.model.entity.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChatController extends Controller {

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextArea messageInput;

    private User currentUser;
    private String friendName;
    private MessageDAO messageDAO;
    private SimpleDateFormat dateFormat;

    /**
     * ChatController constructor, initializes the MessageDAO and date format.
     */
    public ChatController() {
        messageDAO = new MessageDAO();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Sets the current user.
     *
     * @param currentUser The user who is logged into the application.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (friendName != null) {
            loadMessages();
        }
    }

    /**
     * Sets the friend's name the user is chatting with.
     *
     * @param friendName The friend's username.
     */
    public void setFriendName(String friendName) {
        this.friendName = friendName;
        if (currentUser != null) {
            loadMessages();
        }
    }

    /**
     * Handles sending a message. The message is saved and displayed in the chat window.
     */
    @FXML
    public void handleSend() {
        if (currentUser == null) {
            System.out.println("Error: currentUser is not set.");
            return;
        }
        String messageText = messageInput.getText();
        if (messageText != null && !messageText.trim().isEmpty()) {
            String currentTime = dateFormat.format(new Date());
            Message message = new Message(currentUser.getUsername(), friendName, messageText, currentTime);
            messageDAO.save(message);
            messageListView.getItems().add(currentUser.getUsername() + ": " + messageText + " [" + currentTime + "]");
            messageInput.clear();
            loadMessages();
        }
    }

    /**
     * Loads all messages between the current user and the friend, displaying them in order by date.
     */
    private void loadMessages() {
        if (currentUser == null || friendName == null) {
            System.out.println("Error: Cannot load messages because currentUser or friendName is not set.");
            return;
        }

        List<Message> allMessages = messageDAO.findAll();
        List<Message> conversation = allMessages.stream()
                .filter(m -> (m.getRemitent().equals(currentUser.getUsername()) && m.getDestinatary().equals(friendName)) ||
                        (m.getRemitent().equals(friendName) && m.getDestinatary().equals(currentUser.getUsername())))
                .sorted((m1, m2) -> m1.getDate().compareTo(m2.getDate()))  // Sort by date
                .collect(Collectors.toList());

        messageListView.getItems().clear();
        for (Message message : conversation) {
            String formattedMessage = " [" + message.getDate() + "] " + message.getRemitent() + ": " + message.getContains();
            messageListView.getItems().add(formattedMessage);
        }
    }

    /**
     * Opens the chat window with the given input (either a User or a friend's name).
     *
     * @param input The input object (either User, String, or Object[]).
     */
    @Override
    public void onOpen(Object input) {
        if (input instanceof User) {
            setCurrentUser((User) input);
        } else if (input instanceof String) {
            setFriendName((String) input);
        } else if (input instanceof Object[]) {
            Object[] inputs = (Object[]) input;
            if (inputs[0] instanceof User) {
                setCurrentUser((User) inputs[0]);
            }
            if (inputs[1] instanceof String) {
                setFriendName((String) inputs[1]);
            }
        }
    }

    /**
     * Handles the action to go back to the main scene.
     *
     * @param actionEvent The action event triggered by the button click.
     */
    @FXML
    public void handleBackToMain(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Scenes.MAIN.getURL()));
            Parent registerRoot = loader.load();
            Scene registerScene = new Scene(registerRoot);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setScene(registerScene);
            currentStage.setTitle("Main");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleExportConversation(ActionEvent event) {
        // Open a file chooser dialog to let the user select where to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Conversation");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Suggest a default file name
        fileChooser.setInitialFileName("conversation.txt");

        Path filePath = fileChooser.showSaveDialog(messageListView.getScene().getWindow()).toPath();

        if (filePath != null) {
            exportConversationToFile(filePath);
        }
    }

    // Method to export the conversation to a file
    private void exportConversationToFile(Path filePath) {
        try {
            // Get all messages from the ListView and join them as lines
            ObservableList<String> messages = messageListView.getItems();

            // Create the file with the conversation content
            List<String> messageLines = messages.stream()
                    .map(String::toString)
                    .collect(Collectors.toList());

            // Write the conversation to the selected file
            Files.write(filePath, messageLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Conversation exported successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Could not export the conversation.");
        }
    }

    // Helper method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Handles actions when closing the window (if needed).
     *
     * @param output The output object (if any).
     */
    @Override
    public void onClose(Object output) {
        // Method to handle actions when closing the window (if necessary)
    }
}
