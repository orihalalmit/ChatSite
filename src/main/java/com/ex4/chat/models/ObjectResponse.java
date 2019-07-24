package com.ex4.chat.models;


/**
 * Helps
 */
public class ObjectResponse {
    public boolean isError;
    public String errorMessage;
    public Object object;

    public ObjectResponse() {
        this.isError = false;
        this.errorMessage = "";
        this.object = null;
    }
}
