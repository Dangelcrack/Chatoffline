package com.github.dangelcrack.model.dao;

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

public class MessageDAO implements DAO<Message, String> {

    private final String xmlFilePath = "messages.xml";
    private Document document;

    public MessageDAO() {
        try {
            loadXML();
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
        Element root = document.createElement("messages");
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
    public Message save(Message message) {
        try {
            Element root = document.getDocumentElement();

            // Crear el elemento "message"
            Element messageElement = document.createElement("message");

            Element remitent = document.createElement("remitent");
            remitent.appendChild(document.createTextNode(message.getRemitent()));
            messageElement.appendChild(remitent);

            Element destinatary = document.createElement("destinatary");
            destinatary.appendChild(document.createTextNode(message.getDestinatary()));
            messageElement.appendChild(destinatary);

            Element contains = document.createElement("contains");
            contains.appendChild(document.createTextNode(message.getContains()));
            messageElement.appendChild(contains);

            Element date = document.createElement("date");
            date.appendChild(document.createTextNode(message.getDate()));
            messageElement.appendChild(date);

            root.appendChild(messageElement);

            // Guardar los cambios en el archivo XML
            saveXML();

            return message;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message delete(Message message) {
        try {
            NodeList messages = document.getElementsByTagName("message");
            for (int i = 0; i < messages.getLength(); i++) {
                Node node = messages.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String remitent = element.getElementsByTagName("remitent").item(0).getTextContent();
                    String destinatary = element.getElementsByTagName("destinatary").item(0).getTextContent();
                    if (remitent.equals(message.getRemitent()) && destinatary.equals(message.getDestinatary())) {
                        element.getParentNode().removeChild(element);
                        saveXML();
                        return message;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message findByName(String identifier) {
        // Aquí el identificador puede ser la combinación remitente + destinatario, o algún otro campo único
        try {
            NodeList messages = document.getElementsByTagName("message");
            for (int i = 0; i < messages.getLength(); i++) {
                Node node = messages.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String remitent = element.getElementsByTagName("remitent").item(0).getTextContent();
                    if (remitent.equals(identifier)) {
                        String destinatary = element.getElementsByTagName("destinatary").item(0).getTextContent();
                        String contains = element.getElementsByTagName("contains").item(0).getTextContent();
                        String date = element.getElementsByTagName("date").item(0).getTextContent();
                        return new Message(remitent, destinatary, contains, date);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messageList = new ArrayList<>();
        try {
            NodeList messages = document.getElementsByTagName("message");
            for (int i = 0; i < messages.getLength(); i++) {
                Node node = messages.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String remitent = element.getElementsByTagName("remitent").item(0).getTextContent();
                    String destinatary = element.getElementsByTagName("destinatary").item(0).getTextContent();
                    String contains = element.getElementsByTagName("contains").item(0).getTextContent();
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    messageList.add(new Message(remitent, destinatary, contains, date));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }

    @Override
    public void close() throws IOException {
        // No es necesario hacer nada aquí ya que guardamos cada vez que modificamos el archivo
    }
}
