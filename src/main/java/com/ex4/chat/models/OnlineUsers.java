package com.ex4.chat.models;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Class that helps hold the data of the online users
 */
public class OnlineUsers {
    private HashMap<String, OnlineUser> onlineUsers;

    public OnlineUsers() {
        this.onlineUsers = new  HashMap<>();
    }

    /**
     * The function update the last time that user is ping to the server
     * @param sessionId
     * @param userName
     */
    public void onlinePing(String sessionId, String userName) {
        if(!this.onlineUsers.containsKey(sessionId)) {
            this.onlineUsers.put(sessionId, new OnlineUser(sessionId, userName));
        } else {
            this.onlineUsers.get(sessionId).setUserName(userName);
            this.onlineUsers.get(sessionId).setLastPing();
        }
        this.onlineUsers.entrySet().removeIf(e -> !e.getValue().isOnline());
    }

    /**
     * The function removes user from the online users list
     * @param sessionId
     */
    public void remove(String sessionId) {
        this.onlineUsers.remove(sessionId);
    }

    /**
     * The function returns list of the online users
     * @return list of the online users
     */
    public List<OnlineUser> getList() {
        return new ArrayList<>(this.onlineUsers.values());
    }
}
