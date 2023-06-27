package com.projet3.safertogether.services.impl;



import com.projet3.safertogether.dto.MessageDto;
import com.projet3.safertogether.models.Message;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.MessageRepository;
import com.projet3.safertogether.services.MessageService;
import com.projet3.safertogether.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository; // injection pour avoir accés au méthode du repository ( CRUD )

    private final UserService userService;


    @Override
    public Message creer(Message message) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'creer'");
        return messageRepository.save(message);

    }

    @Override
    public List<Message> lire() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'lire'");
        return messageRepository.findAll();
    }

    public List<MessageDto> getMessagesBySenderId(Integer senderId) {
        List<Message> messages = messageRepository.findBySenderId(senderId);
        return messages.stream().map(MessageDto::formEntity).collect(Collectors.toList());
    }

    public List<User> getAllReceiversBySenderId(Integer senderId) {
        List<Message> messages = messageRepository.findBySenderId(senderId);
        List<Integer> receiverIds = messages.stream()
                .map(message -> message.getReceiver().getId())
                .distinct()
                .collect(Collectors.toList());
        return userService.getUsersByIds(receiverIds);
    }

    @Override
    public Message modifier(Integer id, Message message) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'modifier'");
        return messageRepository.findById(id)
                .map(m-> {

                    m.setReceiver(message.getReceiver());
                    m.setCreatedDate(message.getCreatedDate());
                    m.setSender(message.getSender());
                    m.setLecture(message.isLecture());
                    m.setMessage(message.getMessage());
                    return messageRepository.save(m);

                }).orElseThrow(() -> new RuntimeException(" echec et mat"));
    }

    @Override
    public String supprimer(Integer id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
        messageRepository.deleteById(id);
        return " message supprimé";
    }
}
