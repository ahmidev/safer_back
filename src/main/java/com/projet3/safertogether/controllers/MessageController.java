package com.projet3.safertogether.controllers;



import com.projet3.safertogether.dto.MessageDto;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.models.Message;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.MessageRepository;
import com.projet3.safertogether.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {


    private final MessageService messageService;


    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageRepository messageRepository;

//    @MessageMapping("/sendMessage")
//    public void sendMessage(Message message) {
//        // Enregistrez le message dans la base de données
//        messageRepository.save(message);
//
//        // Renvoyer le message au destinataire
//        String destination = "/topic/messages/" + message.getReceiver().getId();
//        template.convertAndSend(destination, message);
//    }

    @PostMapping("/create")
    public Message create(@RequestBody Message messages){
        return messageService.creer(messages);
    }

    @GetMapping("/read")
    public List<Message> read(){
        return messageService.lire();

    }



    @GetMapping("/by-sender/{senderId}")
    public ResponseEntity<List<MessageDto>> getMessagesBySenderId(@PathVariable Integer senderId) {
        List<MessageDto> messages = messageService.getMessagesBySenderId(senderId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/all-receivers-by-sender/{senderId}")
    public ResponseEntity<List<User>> getAllReceiversBySenderId(@PathVariable Integer senderId) {
        List<User> receivers = messageService.getAllReceiversBySenderId(senderId);
        return ResponseEntity.ok(receivers);
    }
    @PutMapping("/update/{id}") //modifier
    public Message update(@PathVariable Integer id,@RequestBody Message message){
        return messageService.modifier(id, message);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        return messageService.supprimer(id);
    }
}
