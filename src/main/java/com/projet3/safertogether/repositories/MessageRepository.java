package com.projet3.safertogether.repositories;


import com.projet3.safertogether.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderId(Integer senderId);
}
