package com.github.dangelcrack.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private List<Message> listMessage;
    private List<User> friends; // Nueva lista de amigos

    public User(String username, String password, List<Message> listMessage,List<User> userList) {
        this.username = username;
        this.password = password;
        this.listMessage = listMessage;
        this.friends = userList;
    }

    // Getters y Setters
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

    public List<User> getFriends() {
        return friends; // Devuelve la lista de amigos
    }

    public void addFriend(User friendUsername) {
        if (!friends.contains(friendUsername)) {
            friends.add(friendUsername); // Agregar amigo
        }
    }

    public void removeFriend(String friendUsername) {
        friends.remove(friendUsername); // Eliminar amigo
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
        return username;
    }
}
