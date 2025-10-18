package com.fullstack.messenger.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String content;
    String sender;
    String recipient;
    @Column(nullable=false)
    LocalDateTime timestamp;
    String color;
    @Enumerated(EnumType.STRING)
    MessageType type;
    public enum MessageType{
            CHAT,JOIN,LEAVE,PRIVATE_MESSAGE,TYPING
    }
}
