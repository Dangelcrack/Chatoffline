package com.github.dangelcrack.model.dao;

import com.github.dangelcrack.model.entity.Message;
import com.github.dangelcrack.model.entity.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDAO implements DAO<User, String> {

    private final String xmlFilePath = "users.xml";
    private Document document;
    private MessageDAO messageDAO;  // Dependencia de MessageDAO

    public UserDAO() {
        try {
            loadXML();
            this.messageDAO = new MessageDAO(); // Inicializamos el MessageDAO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cargar el archivo XML al inicializar el DAO
    private void loadXML() throws Exception {
        File xmlFile = new File(xmlFilePath);
        if (xmlFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
        } else {
            // Si el archivo no existe, crear uno nuevo
            createXML();
        }
    }

    // Crear una estructura XML básica si el archivo no existe
    private void createXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();

        // Crear el elemento raíz
        Element root = document.createElement("users");
        document.appendChild(root);

        // Guardar el nuevo documento XML
        saveXML();
    }

    // Guardar el documento XML en el archivo
    private void saveXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, streamResult);
    }

    @Override
    public User save(User user) {
        try {
            Element root = document.getDocumentElement();
            Element userElement = document.createElement("user");

            // Crear el elemento de nombre de usuario
            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(user.getUsername()));
            userElement.appendChild(username);

            // Crear el elemento de contraseña
            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(user.getPassword()));
            userElement.appendChild(password);

            // Crear el elemento de mensajes
            Element messagesElement = document.createElement("messages");
            for (Message message : user.getListMessage()) {
                Message savedMessage = messageDAO.save(message);
                Element messageIdElement = document.createElement("messageId");
                messageIdElement.appendChild(document.createTextNode(savedMessage.getRemitent() + "-" + savedMessage.getDestinatary() + "-" + savedMessage.getDate()));
                messagesElement.appendChild(messageIdElement);
            }
            userElement.appendChild(messagesElement);

            // Crear el elemento de amigos
            Element friendsElement = document.createElement("friends");
            for (User friend : user.getFriends()) {
                Element friendElement = document.createElement("friend");
                friendElement.appendChild(document.createTextNode(friend.getUsername())); // Usar getUsername() para obtener el nombre de usuario del amigo
                friendsElement.appendChild(friendElement);
            }
            userElement.appendChild(friendsElement);

            // Agregar el nuevo usuario al documento XML
            root.appendChild(userElement);
            saveXML(); // Guardar el XML después de añadir el nuevo usuario
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User delete(User user) {
        try {
            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    if (username.equals(user.getUsername())) {
                        // Eliminar los mensajes asociados
                        NodeList messageIds = element.getElementsByTagName("messageId");
                        for (int j = 0; j < messageIds.getLength(); j++) {
                            String messageId = messageIds.item(j).getTextContent();
                            String[] parts = messageId.split("-");
                            // Borrar mensaje usando remitente, destinatario y fecha como ID
                            Message messageToDelete = new Message(parts[0], parts[1], "", parts[2]);
                            messageDAO.delete(messageToDelete);
                        }

                        element.getParentNode().removeChild(element);
                        saveXML();
                        return user;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public User findByName(String username) {
        return findByName(username, new HashSet<>());
    }
    private User findByName(String username, Set<String> processed) {
        if (processed.contains(username)) {
            return null;
        }

        processed.add(username); // Marcar este usuario como procesado

        try {
            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String userName = element.getElementsByTagName("username").item(0).getTextContent();

                    if (userName.equals(username)) {
                        String password = element.getElementsByTagName("password").item(0).getTextContent();
                        List<Message> messages = new ArrayList<>();

                        // Obtener los IDs de los mensajes
                        NodeList messageIds = element.getElementsByTagName("messageId");
                        for (int j = 0; j < messageIds.getLength(); j++) {
                            String messageId = messageIds.item(j).getTextContent();
                            String[] parts = messageId.split("-");
                            Message message = messageDAO.findByName(parts[0]);
                            messages.add(message);
                        }

                        // Obtener la lista de amigos
                        List<User> friends = new ArrayList<>();
                        NodeList friendNodes = element.getElementsByTagName("friend");
                        for (int j = 0; j < friendNodes.getLength(); j++) {
                            String friendUsername = friendNodes.item(j).getTextContent();
                            // No buscar amigos si ya fueron procesados
                            User friend = findByName(friendUsername, processed);
                            if (friend != null) {
                                friends.add(friend);
                            }
                        }

                        // Crear el objeto User con la firma correcta
                        return new User(userName, password, messages, friends);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try {
            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    List<Message> messages = new ArrayList<>();

                    NodeList messageIds = element.getElementsByTagName("messageId");
                    for (int j = 0; j < messageIds.getLength(); j++) {
                        String messageId = messageIds.item(j).getTextContent();
                        String[] parts = messageId.split("-");
                        Message message = messageDAO.findByName(parts[0]);
                        messages.add(message);
                    }

                    // Obtener la lista de amigos
                    List<User> friends = new ArrayList<>();
                    NodeList friendNodes = element.getElementsByTagName("friend");
                    for (int j = 0; j < friendNodes.getLength(); j++) {
                        String friendUsername = friendNodes.item(j).getTextContent();
                        User friend = findByName(friendUsername); // Llama a findByName para obtener el objeto User
                        if (friend != null) {
                            friends.add(friend);
                        }
                    }

                    // Crear el objeto User con la nueva firma del constructor
                    User user = new User(username, password, messages, friends);
                    userList.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<User> getFriendsForUser(User user) {
        User foundUser = findByName(user.getUsername());
        if (foundUser != null) {
            return foundUser.getFriends(); // Devuelve la lista de amigos del usuario encontrado
        }
        return new ArrayList<>(); // Devuelve una lista vacía si no se encuentra el usuario
    }

    @Override
    public void close() throws IOException {
        // No es necesario hacer nada aquí ya que guardamos cada vez que modificamos el archivo
    }

    public User authenticate(String username, String password) {
        try {
            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Node node = users.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String userName = element.getElementsByTagName("username").item(0).getTextContent();
                    String userPassword = element.getElementsByTagName("password").item(0).getTextContent();
                    if (userName.equals(username) && userPassword.equals(password)) {
                        List<Message> messages = new ArrayList<>();
                        NodeList messageIds = element.getElementsByTagName("messageId");
                        for (int j = 0; j < messageIds.getLength(); j++) {
                            String messageId = messageIds.item(j).getTextContent();
                            String[] parts = messageId.split("-");
                            Message message = messageDAO.findByName(parts[0]);
                            messages.add(message);
                        }
                        List<User> friends = new ArrayList<>();
                        NodeList friendNodes = element.getElementsByTagName("friend");
                        for (int j = 0; j < friendNodes.getLength(); j++) {
                            String friendUsername = friendNodes.item(j).getTextContent();
                            User friend = findByName(friendUsername); // Llama a findByName para obtener el objeto User
                            if (friend != null) {
                                friends.add(friend);
                            }
                        }
                        return new User(userName, userPassword, messages, friends);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si no se encuentra un usuario que coincida
    }

}
