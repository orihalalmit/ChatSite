package com.ex4.chat.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface that creates a repository function
 */
public interface ChatMessagesRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findFirst3ByOrderByIdDesc();
    List<ChatMessage> findByIdGreaterThanEqualOrderByIdAsc(long id);
    List<ChatMessage> findByUserNameOrderByIdAsc(String userName);
    List<ChatMessage> findByMessageContainingIgnoreCaseOrderByIdAsc(String message);
}
