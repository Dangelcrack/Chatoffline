package com.github.dangelcrack.model.entity;

public enum Scenes {
    LOGIN("view/Login.fxml");
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