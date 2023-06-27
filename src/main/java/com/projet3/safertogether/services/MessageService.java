package com.projet3.safertogether.services;


import com.projet3.safertogether.dto.MessageDto;
import com.projet3.safertogether.models.Message;
import com.projet3.safertogether.models.User;

import java.util.List;

public interface MessageService {

    Message creer(Message message);

    List<Message> lire();
    List<MessageDto> getMessagesBySenderId(Integer sendeId);
    List<User> getAllReceiversBySenderId(Integer senderId);

    Message modifier(Integer id, Message message);

    String supprimer(Integer id);
}
