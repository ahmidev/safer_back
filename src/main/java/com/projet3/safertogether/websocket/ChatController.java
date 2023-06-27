package com.projet3.safertogether.websocket;

import com.projet3.safertogether.dto.MessageDto;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.models.Message;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.MessageRepository;
import com.projet3.safertogether.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatController {



    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserServiceImpl userService;



    @MessageMapping("/sendMessage")
    public void handleMessage( MessageDto messageDto) {

        // Récupérez les instances User pour les identifiants sender et receiver

        User sender = userService.findById2(messageDto.getSender());
        User receiver = userService.findById2(messageDto.getReceiver());
        // Enregistrez le message dans la base de données

        Message msg = MessageDto.toEntity(messageDto, sender, receiver);
        messageRepository.save(msg);

        // Renvoyer le message au destinataire
//        String destination = "/topic/messages/" + message.getReceiver().getId();
//        template.convertAndSend(destination, message);

        // je le renvoi au receveur et à l'envoyeur

        template.convertAndSend("/topic/messages/" + messageDto.getReceiver() + "/" + messageDto.getSender(), messageDto);
        template.convertAndSend("/topic/messages/" + messageDto.getSender() + "/" + messageDto.getReceiver(), messageDto);

    }
}

