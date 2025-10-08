package com.fullstack.messenger.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class ChatMessage {
    int id;
    String content;
    String sender;
    String recipient;
    @Column(nullable=false)
    LocalDateTime timeStamp;
    String color;
    @Enumerated(EnumType.STRING)
    MessageType type;
    public enum MessageType{
            CHAT,JOIN,LEAVE,PRIVATE_MESSAGE,TYPING
    }
}
