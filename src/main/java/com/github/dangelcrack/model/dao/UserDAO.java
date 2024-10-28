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
    private MessageDAO messageDAO;

    public UserDAO() {
        try {
            loadXML();
            this.messageDAO = new MessageDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadXML() throws Exception {
        File xmlFile = new File(xmlFilePath);
        if (xmlFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
        } else {
            createXML();
        }
    }

    private void createXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();

        Element root = document.createElement("users");
        document.appendChild(root);

        saveXML();
    }

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
            NodeList userList = root.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Element existingUser = (Element) userList.item(i);
                Element usernameElement = (Element) existingUser.getElementsByTagName("username").item(0);
                if (usernameElement != null && usernameElement.getTextContent().equals(user.getUsername())) {
                    root.removeChild(existingUser);
                    break;
                }
            }
            Element userElement = document.createElement("user");

            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(user.getUsername()));
            userElement.appendChild(username);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(user.getPassword()));
            userElement.appendChild(password);

            Element messagesElement = document.createElement("messages");
            for (Message message : user.getListMessage()) {
                Message savedMessage = messageDAO.save(message);
                Element messageIdElement = document.createElement("messageId");
                messageIdElement.appendChild(document.createTextNode(savedMessage.getRemitent() + "-" + savedMessage.getDestinatary() + "-" + savedMessage.getDate()));
                messagesElement.appendChild(messageIdElement);
            }
            userElement.appendChild(messagesElement);

            Element friendsElement = document.createElement("friends");
            for (User friend : user.getFriends()) {
                Element friendElement = document.createElement("friend");
                friendElement.appendChild(document.createTextNode(friend.getUsername()));
                friendsElement.appendChild(friendElement);
            }
            userElement.appendChild(friendsElement);
            root.appendChild(userElement);
            saveXML();
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
                        NodeList messageIds = element.getElementsByTagName("messageId");
                        for (int j = 0; j < messageIds.getLength(); j++) {
                            String messageId = messageIds.item(j).getTextContent();
                            String[] parts = messageId.split("-");
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

        processed.add(username);

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
                            User friend = findByName(friendUsername, processed);
                            if (friend != null) {
                                friends.add(friend);
                            }
                        }

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

                    List<User> friends = new ArrayList<>();
                    NodeList friendNodes = element.getElementsByTagName("friend");
                    for (int j = 0; j < friendNodes.getLength(); j++) {
                        String friendUsername = friendNodes.item(j).getTextContent();
                        User friend = findByName(friendUsername);
                        if (friend != null) {
                            friends.add(friend);
                        }
                    }

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
            return foundUser.getFriends();
        }
        return new ArrayList<>();
    }

    @Override
    public void close(){
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
                            User friend = findByName(friendUsername);
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
        return null;
    }
}
