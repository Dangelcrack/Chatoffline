package com.github.dangelcrack.model.repo;

import com.github.dangelcrack.model.entity.User;

/**
 * The Sesion class represents a singleton session for a logged-in user.
 * It contains methods to manage user sessions, including starting and closing sessions.
 */
public class Sesion {
    private static Sesion _instance;
    private User user;

    /**
     * Private constructor to prevent direct instantiation.
     *
     * @param user the user associated with the session
     */
    private Sesion(User user) {
        this.user = user;
    }

    /**
     * Initiates a session for the specified user.
     * If a session already exists, it will not create a new one.
     *
     * @param user the user to log in
     */
    public static void iniciateSesion(User user) {
        if (_instance == null) {
            _instance = new Sesion(user);
        }
    }

    /**
     * Returns the current session instance.
     *
     * @return the current Sesion instance
     */
    public static Sesion getInstance() {
        return _instance;
    }

    /**
     * Private default constructor to prevent direct instantiation.
     */
    private Sesion() {
    }

    /**
     * Gets the user associated with the session.
     *
     * @return the user of the session
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user for the session.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Closes the current session, setting the instance to null.
     */
    public static void closeSession() {
        _instance = null;
    }
}
