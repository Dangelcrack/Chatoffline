package com.github.dangelcrack.model.entity;

import com.github.dangelcrack.controller.AddFriendController;

public enum Scenes {
    MAIN("view/Main.fxml"),
    LOGIN("view/Login.fxml"),
    ADDFRIEND("view/AddFriend.fxml");
    private String url;
    /**
     * Constructor for the enum constant.
     * @param url The URL of the FXML file.
     */
    Scenes(String url){
        this.url = url;
    }
    /**
     * Retrieves the URL of the FXML file.
     * @return The URL as a String.
     */
    public String getURL(){
        return url;
    }
}