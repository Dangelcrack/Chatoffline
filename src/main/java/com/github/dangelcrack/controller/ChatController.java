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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
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

    /**
     * Opens a file dialog to let the user export the conversation history to a CSV file.
     * The default file name is set based on the current user and friend's name.
     * If the user selects a valid location, the conversation is saved in CSV format.
     */
    @FXML
    private void handleExportConversation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Conversation");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("conversation_" + currentUser.getUsername() + "_" + friendName + ".csv");
        File file = fileChooser.showSaveDialog(messageListView.getScene().getWindow());
        if (file != null) {
            exportConversationToFile(file.toPath());
        }
    }

    /**
     * Exports the conversation to the given file path in CSV format.
     * It writes the messages, including the sender, message content, and timestamp, to the CSV file.
     * A header ("Sender,Message,Timestamp") is included.
     * If an error occurs during writing, an alert is shown to the user.
     *
     * @param filePath the path where the conversation CSV will be saved
     */
    private void exportConversationToFile(Path filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ObservableList<String> messages = messageListView.getItems();
            writer.write("Sender,Message,Timestamp\n"); // CSV Header
            messages.stream()
                    .map(this::formatMessage)
                    .forEach(message -> {
                        try {
                            writer.write(message);
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Conversation exported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Could not export the conversation.");
        }
    }

    /**
     * Formats a message string by extracting the sender, message content, and timestamp.
     * Returns the message formatted as a CSV line with the sender, content, and timestamp.
     * If the message format is invalid, it returns the original message.
     *
     * @param message the raw message string
     * @return the formatted CSV line with sender, content, and timestamp
     */
    private String formatMessage(String message) {
        try {
            int firstBracketIndex = message.indexOf('[');
            int secondBracketIndex = message.indexOf(']', firstBracketIndex);
            int colonIndex = message.indexOf(':', secondBracketIndex);

            if (firstBracketIndex == -1 || secondBracketIndex == -1 || colonIndex == -1) {
                return message;
            }

            String timestamp = message.substring(firstBracketIndex + 1, secondBracketIndex);
            String sender = message.substring(secondBracketIndex + 1, colonIndex).trim();
            String content = message.substring(colonIndex + 1).trim();

            return String.format("%s,\"%s\",%s", sender, content, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
    }

    /**
     * Generates a comprehensive summary of the conversation between the current user and friend.
     * The summary includes:
     * - Total number of messages exchanged
     * - Most common words in the conversation
     * - Simple message length statistics
     * - The user with the highest activity in the conversation
     * - Optional metrics like the busiest time of day (if needed)
     */
    @FXML
    private void handleGenerateConversationSummary() {
        if (currentUser == null || friendName == null) {
            showAlert(Alert.AlertType.ERROR, "Resumen no disponible", "Debe seleccionar un usuario y amigo para ver el resumen.");
            return;
        }

        List<Message> allMessages = messageDAO.findAll();
        List<Message> conversation = allMessages.stream()
                .filter(m -> (m.getRemitent().equals(currentUser.getUsername()) && m.getDestinatary().equals(friendName)) ||
                        (m.getRemitent().equals(friendName) && m.getDestinatary().equals(currentUser.getUsername())))
                .collect(Collectors.toList());
        long totalMessages = conversation.size();
        Map<String, Long> wordFrequency = conversation.stream()
                .flatMap(msg -> Arrays.stream(msg.getContains().toLowerCase().split("\\s+")))
                .filter(word -> word.length() > 3)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        List<Map.Entry<String, Long>> mostCommonWords = wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
        IntSummaryStatistics messageLengthStats = conversation.stream()
                .mapToInt(msg -> msg.getContains().length())
                .summaryStatistics();
        Map<String, Long> userMessageCount = conversation.stream()
                .collect(Collectors.groupingBy(Message::getRemitent, Collectors.counting()));
        String mostActiveUser = userMessageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        StringBuilder summary = new StringBuilder();
        summary.append("Resumen de la conversación:\n");
        summary.append("Total de mensajes: ").append(totalMessages).append("\n");
        summary.append("Longitud promedio del mensaje: ").append(messageLengthStats.getAverage()).append(" caracteres\n");
        summary.append("Mensaje más largo: ").append(messageLengthStats.getMax()).append(" caracteres\n");
        summary.append("Mensaje más corto: ").append(messageLengthStats.getMin()).append(" caracteres\n");
        summary.append("Usuario más activo: ").append(mostActiveUser).append("\n");
        summary.append("Palabras más comunes:\n");
        mostCommonWords.forEach(entry ->
                summary.append("Palabra: '").append(entry.getKey()).append("' - Frecuencia: ").append(entry.getValue()).append("\n"));
        showAlert(Alert.AlertType.INFORMATION, "Resumen de la Conversación", summary.toString());
    }

    /**
     * Displays an alert dialog to the user with the specified title and message.
     * The type of alert (information, error, etc.) is determined by the alertType parameter.
     *
     * @param alertType the type of alert to show (e.g., information, error)
     * @param title     the title of the alert dialog
     * @param message   the message to display in the alert
     */
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
