package com.ex4.chat.models;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Helps get online users
 */
@Configuration
public class MyConfig {
    @Bean
    public OnlineUsers getOnlineUsers() {
        return new OnlineUsers();
    }
}
