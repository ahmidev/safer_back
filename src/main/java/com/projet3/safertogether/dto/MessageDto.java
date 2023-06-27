package com.projet3.safertogether.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projet3.safertogether.models.Message;
import com.projet3.safertogether.models.User;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer sender;

    private Integer receiver;

    private String message;
    private boolean lecture ;
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    public  static MessageDto formEntity(Message message){
        return MessageDto.builder()
                .id(message.getId())
                .message(message.getMessage())
                .receiver(message.getReceiver().getId())
                .sender(message.getSender().getId())
                .createdDate(message.getCreatedDate())
                .lastModifiedDate(message.getLastModifiedDate())
                .build();
    }

    public  static Message toEntity(MessageDto message,User sender, User receiver){
        return Message.builder()
                .id(message.getId())
                .message(message.getMessage())
                .receiver(receiver)
                .sender(sender)
                .createdDate(message.getCreatedDate())
                .lastModifiedDate(message.getLastModifiedDate())
                .build();
    }
}
