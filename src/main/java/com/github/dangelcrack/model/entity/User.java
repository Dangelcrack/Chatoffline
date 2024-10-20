package com.github.dangelcrack.model.entity;

import java.util.List;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private List<Message> listMessage;

    public User() {
    }

    public User(String username, String password, List<Message> listMessage) {
        this.username = username;
        this.password = password;
        this.listMessage = listMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Message> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<Message> listMessage) {
        this.listMessage = listMessage;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", listMessage=" + listMessage +
                '}';
    }
}
