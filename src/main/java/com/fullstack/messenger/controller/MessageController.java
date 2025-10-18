package com.fullstack.messenger.controller;

import com.fullstack.messenger.model.ChatMessage;
import com.fullstack.messenger.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



//.to get the private message from the repository
@RestController
@RequestMapping("/api/message")
public class MessageController
{
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @GetMapping("/private")
    public ResponseEntity<List<ChatMessage>> getPrivateMessages(@RequestParam String user1, @RequestParam String user2) {

        List<ChatMessage> messages = chatMessageRepository.findPrivateMessagesBetweenTwoUsers(user1, user2);
        return ResponseEntity.ok(messages);
    }
}
