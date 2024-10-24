package com.github.dangelcrack.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The User class represents a user in the system.
 * It includes attributes such as the username, password,
 * a list of messages, and a list of friends.
 */
public class User {
    private String username;
    private String password;
    private List<Message> listMessage;
    private List<User> friends;

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param username    the username of the user
     * @param password    the password of the user
     * @param listMessage the list of messages associated with the user
     * @param userList    the list of friends for this user
     */
    public User(String username, String password, List<Message> listMessage, List<User> userList) {
        this.username = username;
        this.password = password;
        this.listMessage = listMessage;
        this.friends = userList;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the list of messages associated with the user.
     *
     * @return the list of messages
     */
    public List<Message> getListMessage() {
        return listMessage;
    }

    /**
     * Sets the list of messages associated with the user.
     *
     * @param listMessage the list of messages to set
     */
    public void setListMessage(List<Message> listMessage) {
        this.listMessage = listMessage;
    }

    /**
     * Gets the list of friends for this user.
     *
     * @return the list of friends
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * Adds a friend to the user's friends list.
     *
     * @param friendUsername the friend to add
     */
    public void addFriend(User friendUsername) {
        if (!friends.contains(friendUsername) ) {
            friends.add(friendUsername);
        }
    }

    /**
     * Removes a friend from the user's friends list.
     *
     * @param friendUsername the username of the friend to remove
     */
    public void removeFriend(String friendUsername) {
        friends.remove(friendUsername);
    }

    /**
     * Checks if this User is equal to another object.
     * Two users are considered equal if they have the same username.
     *
     * @param object the object to compare with
     * @return true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(username, user.username);
    }

    /**
     * Returns the hash code for this User.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Returns a string representation of the User.
     *
     * @return a string representation of the User
     */
    @Override
    public String toString() {
        return username;
    }
}
