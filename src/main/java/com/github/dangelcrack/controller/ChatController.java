package com.github.dangelcrack.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatController extends Controller {

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextArea messageInput;

    private String friendName;

    public void setFriendName(String friendName) {
        this.friendName = friendName;

    }

    @FXML
    public void handleSend() {
        String message = messageInput.getText();
        if (message != null && !message.trim().isEmpty()) {
            messageListView.getItems().add("You: " + message);
            messageInput.clear();
        }
    }

    @Override
    public void onOpen(Object input) {
        // Aquí puedes manejar la lógica cuando se abre la vista de chat
    }

    @Override
    public void onClose(Object output) {
        // Aquí puedes manejar la lógica cuando se cierra la vista de chat
    }
}
