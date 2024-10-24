package com.github.dangelcrack.model.entity;

/**
 * The Message class represents a message that can be sent between users.
 * It includes attributes such as the sender (remitent), recipient (destinatary),
 * the content of the message, and the date the message was sent.
 */
public class Message {
    private String remitent;
    private String destinatary;
    private String contains;
    private String date;
    /**
     * Default constructor for Message.
     */
    public Message() {
    }

    /**
     * Constructs a Message object with the specified attributes.
     *
     * @param remitent     the username of the sender
     * @param destinatary  the username of the recipient
     * @param contains     the content of the message
     * @param date         the date the message was sent
     */
    public Message(String remitent, String destinatary, String contains, String date) {
        this.remitent = remitent;
        this.destinatary = destinatary;
        this.contains = contains;
        this.date = date;
    }

    /**
     * Gets the username of the sender.
     *
     * @return the sender's username
     */
    public String getRemitent() {
        return remitent;
    }

    /**
     * Sets the username of the sender.
     *
     * @param remitent the username to set
     */
    public void setRemitent(String remitent) {
        this.remitent = remitent;
    }

    /**
     * Gets the username of the recipient.
     *
     * @return the recipient's username
     */
    public String getDestinatary() {
        return destinatary;
    }

    /**
     * Sets the username of the recipient.
     *
     * @param destinatary the username to set
     */
    public void setDestinatary(String destinatary) {
        this.destinatary = destinatary;
    }

    /**
     * Gets the content of the message.
     *
     * @return the message content
     */
    public String getContains() {
        return contains;
    }

    /**
     * Sets the content of the message.
     *
     * @param contains the content to set
     */
    public void setContains(String contains) {
        this.contains = contains;
    }

    /**
     * Gets the date the message was sent.
     *
     * @return the date the message was sent
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the message was sent.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns a string representation of the Message.
     *
     * @return a string representation of the Message
     */
    @Override
    public String toString() {
        return "Message{" +
                "remitent='" + remitent + '\'' +
                ", destinatary='" + destinatary + '\'' +
                ", contains='" + contains + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
