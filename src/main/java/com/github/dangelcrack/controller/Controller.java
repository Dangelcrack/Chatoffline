package com.github.dangelcrack.controller;

import com.github.dangelcrack.App;

import java.io.IOException;

/**
 * Abstract base class for all controllers in the application. It provides a structure for
 * managing common behavior such as opening and closing controllers, and linking the app context.
 */
public abstract class Controller {

    protected App app; // Reference to the main App instance

    /**
     * Sets the application instance to this controller.
     * Allows the controller to access application-wide functionalities.
     *
     * @param app The main application instance.
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * This method is called when the controller is opened.
     * Subclasses must implement their specific behavior when opening.
     *
     * @param input An input object that provides necessary data when the controller opens.
     * @throws IOException In case any I/O error occurs during the opening process.
     */
    public abstract void onOpen(Object input) throws IOException;

    /**
     * This method is called when the controller is closed.
     * Subclasses must implement their specific behavior when closing.
     *
     * @param output An output object that provides any necessary data when the controller closes.
     */
    public abstract void onClose(Object output);

}
