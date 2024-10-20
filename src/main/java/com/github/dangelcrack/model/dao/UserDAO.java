package com.github.dangelcrack.model.dao;

import com.github.dangelcrack.model.entity.User;
import com.github.dangelcrack.model.entity.Message;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            // Crear el elemento "user"
            Element userElement = document.createElement("user");

            // Agregar elementos con datos del usuario (nombre y contraseña)
            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(user.getUsername()));
            userElement.appendChild(username);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(user.getPassword()));
            userElement.appendChild(password);

            // Crear el elemento de los IDs de los mensajes
            Element messagesElement = document.createElement("messages");
            for (Message message : user.getListMessage()) {
                // Guardar cada mensaje en el MessageDAO y obtener el ID o referencia del mensaje
                Message savedMessage = messageDAO.save(message);

                // Asumimos que el remitente + destinatario + fecha es un ID único para simplificar
                Element messageIdElement = document.createElement("messageId");
                messageIdElement.appendChild(document.createTextNode(savedMessage.getRemitent() + "-" + savedMessage.getDestinatary() + "-" + savedMessage.getDate()));
                messagesElement.appendChild(messageIdElement);
            }
            userElement.appendChild(messagesElement);

            root.appendChild(userElement);

            // Guardar los cambios en el archivo XML
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
                            // Recuperar mensaje usando remitente, destinatario y fecha como ID
                            Message message = messageDAO.findByName(parts[0]);  // Aquí puedes ajustar según tu MessageDAO
                            messages.add(message);
                        }

                        return new User(userName, password, messages);
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

                    // Obtener los IDs de los mensajes
                    NodeList messageIds = element.getElementsByTagName("messageId");
                    for (int j = 0; j < messageIds.getLength(); j++) {
                        String messageId = messageIds.item(j).getTextContent();
                        String[] parts = messageId.split("-");
                        // Recuperar mensaje usando remitente, destinatario y fecha como ID
                        Message message = messageDAO.findByName(parts[0]);  // Ajustar según tu lógica
                        messages.add(message);
                    }

                    userList.add(new User(username, password, messages));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void close() throws IOException {
        // No es necesario hacer nada aquí ya que guardamos cada vez que modificamos el archivo
    }
}
