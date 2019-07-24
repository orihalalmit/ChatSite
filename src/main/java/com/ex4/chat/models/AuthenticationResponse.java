package com.ex4.chat.models;

import java.io.Serializable;

/**
 * Class that helps the AuthController check if the user is logged in
 */
public class   AuthenticationResponse implements Serializable {
    private String userName;
    private boolean isAuthenticated;

    /**
     * Default Constructor
     */
    public AuthenticationResponse() {
        this.userName = "";
        this.isAuthenticated = false;
    }
    /**
     * Constructor that gets the userName and if the user is Authenticated.
     */
    public AuthenticationResponse(String userName, boolean isAuthenticated) {
        this.userName = userName;
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return user name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param isValid
     * update authenticated
     */
    public void setIsAuthenticated(boolean isValid) {
        this.isAuthenticated = isValid;
    }

    /**
     * @return if authenticated
     */
    public boolean getIsAuthenticated() {
        return this.isAuthenticated;
    }
}
