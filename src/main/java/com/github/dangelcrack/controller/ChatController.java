package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.MessageDAO;
import com.github.dangelcrack.model.entity.Message;
import com.github.dangelcrack.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ChatController extends Controller {
    @FXML
    private ListView<String> messageListView;
    @FXML
    private TextArea messageInput;

    private User currentUser;    // Usuario actual
    private String friendName;   // Persona con la que está chateando
    private MessageDAO messageDAO;
    private SimpleDateFormat dateFormat;

    public ChatController() {
        messageDAO = new MessageDAO();  // Inicializa el DAO para los mensajes
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    // Método para establecer el usuario actual
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        System.out.println("currentUser has been set: " + currentUser.getUsername());
        if (friendName != null) {
            loadMessages();
        }
    }


    // Método para establecer el nombre del amigo con el que está chateando
    public void setFriendName(String friendName) {
        this.friendName = friendName;
        System.out.println("Friend set: " + friendName);  // Mensaje de depuración
        // Intentar cargar los mensajes si currentUser ya está establecido
        if (currentUser != null) {
            loadMessages();
        }
    }

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

    // Cargar todos los mensajes entre currentUser y friendName, y mostrarlos ordenados por fecha
    private void loadMessages() {
        // Verificar si currentUser o friendName son null antes de proceder
        if (currentUser == null || friendName == null) {
            System.out.println("Error: Cannot load messages because currentUser or friendName is not set.");
            return;  // Salir si currentUser o friendName no están configurados
        }
        List<Message> allMessages = messageDAO.findAll();
        // Filtrar los mensajes solo entre currentUser y friendName
        List<Message> conversation = allMessages.stream()
                .filter(m -> (m.getRemitent().equals(currentUser.getUsername()) && m.getDestinatary().equals(friendName)) ||
                        (m.getRemitent().equals(friendName) && m.getDestinatary().equals(currentUser.getUsername())))
                .sorted((m1, m2) -> m1.getDate().compareTo(m2.getDate()))  // Ordenar por fecha
                .collect(Collectors.toList());
        // Limpiar la lista de mensajes en la interfaz
        messageListView.getItems().clear();
        // Mostrar todos los mensajes en la interfaz
        for (Message message : conversation) {
            String formattedMessage = message.getRemitent() + ": " + message.getContains() + " [" + message.getDate() + "]";
            messageListView.getItems().add(formattedMessage);
        }
    }

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
        } else {
            System.out.println("Error: Invalid input for ChatController");
        }
    }

    @Override
    public void onClose(Object output) {
    }
}
