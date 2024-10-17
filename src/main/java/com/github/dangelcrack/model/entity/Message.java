package com.github.dangelcrack.model.entity;

public class Message {
    private String remitent;
    private String destinatary;
    private String contains;
    private String date;

    public Message() {
    }

    public Message(String remitent, String destinatary, String contains, String date) {
        this.remitent = remitent;
        this.destinatary = destinatary;
        this.contains = contains;
        this.date = date;
    }

    public String getRemitent() {
        return remitent;
    }

    public void setRemitent(String remitent) {
        this.remitent = remitent;
    }

    public String getDestinatary() {
        return destinatary;
    }

    public void setDestinatary(String destinatary) {
        this.destinatary = destinatary;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
