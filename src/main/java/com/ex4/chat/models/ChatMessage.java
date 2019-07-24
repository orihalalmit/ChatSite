package com.ex4.chat.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * Define the chatMessage object and how it will be in the DataBase
 */
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Username is mandatory")
    private String userName;

    @NotBlank(message = "Messgae is mandatory")
    private String message;

    /**
     * @param id
     * sets id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param userName
     * set user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param message
     * set message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
