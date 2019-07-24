package com.ex4.chat.models;

import com.ex4.chat.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Class that helps hold the data of a online user
 */
public class OnlineUser {
    /**
     * The session id of the user
     */
    @JsonIgnore
    private String sessionId;

    /**
     * The username
     */
    @JsonProperty
    private String userName;

    /**
     * The last time the user send ping to the server
     */
    @JsonProperty
    private Date lastPing;

    public OnlineUser(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.lastPing = new Date();
    }

    /**
     * Username setter
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Last ping setter
     */
    public void setLastPing() {
        this.lastPing = new Date();
    }

    /**
     * Checks if the user is online
     * @return true if the user is still online
     * checking if passed 10 seconds after the user closed the tab
     */
    @JsonIgnore
    public boolean isOnline() {
        return Math.abs(new Date().getTime()-lastPing.getTime())/1000 < Constants.OnlinePingMaxTime;
    }
}
