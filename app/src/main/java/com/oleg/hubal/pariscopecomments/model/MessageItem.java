package com.oleg.hubal.pariscopecomments.model;

/**
 * Created by User on 07.12.2016.
 */

public class MessageItem {

    private String message;
    private String imageUri;
    private String userName;

    public MessageItem() {

    }

    public MessageItem(String message, String imageUri, String userName) {
        this.message = message;
        this.imageUri = imageUri;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
