package com.fullstack.messenger.controller;

import com.fullstack.messenger.model.ChatMessage;
import com.fullstack.messenger.model.User;
import com.fullstack.messenger.repository.ChatMessageRepository;
import com.fullstack.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

@Controller
public class ChatController
{
    @Autowired
    private UserService userService;//dependency injection
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat.addUser")//WEB SOCKET DESTINATION
    @SendTo("/topic/public")//
    //after use logs in this method is hit
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor)
    {
        //to add the user to the chat area
        if(userService.userExists(chatMessage.getSender())){//checkimg whether user is available in DB  or not
            //store username in session
            headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());//say in a dicord community
            //a thousand members are present and the server wants to know which user ischatting hence storing it in the session
            userService.setUserOnlineStatus(chatMessage.getSender(),true);//to show the user as online
            System.out.println("User Added Successfully"+chatMessage.getSender()+"with SeesionId"+headerAccessor.getSessionId());
            chatMessage.setTimeStamp(LocalDateTime.now());//time when user came online
            if(chatMessage.getContent()==null)chatMessage.setContent("");
            return (ChatMessage) chatMessageRepository.save(chatMessage);

        }
        return null;

    }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage)
    {
        if(userService.userExists(chatMessage.getSender())){
            if(chatMessage.getTimeStamp()==null){
                chatMessage.setTimeStamp(LocalDateTime.now());
            }
            if(chatMessage.getContent()==null){
                chatMessage.setContent("");
            }
            return (ChatMessage) chatMessageRepository.save(chatMessage);
        }
        return null;

    }
    @MessageMapping("/chat.sendPrivateMessage")//dynamic subscription
    public void sendPrivateMessage(@Payload ChatMessage chatMessage,SimpMessageHeaderAccessor headerAccessor)
    {
        //to set the reciient destnation4//unique user name
         if(userService.userExists(chatMessage.getSender()) && userService.userExists(chatMessage.getRecipient())) {
             if (chatMessage.getTimeStamp() == null) {
                 chatMessage.setTimeStamp(LocalDateTime.now());
             }
             if (chatMessage.getContent() == null) {
                 chatMessage.setContent("");
             }
             chatMessage.setType(ChatMessage.MessageType.PRIVATE_MESSAGE);//settin
             ChatMessage savedMessage = (ChatMessage) chatMessageRepository.save(chatMessage);
             System.out.println("hey get chat message id" + chatMessage.getId());
             //to set the recipent destn and sender
             try{
                 //setting the path
                 String recipientDestn = "/user/" + chatMessage.getRecipient() + "/queue/private";
                 System.out.println("sent message to recipent" + recipientDestn);
                 messagingTemplate.convertAndSend(recipientDestn,savedMessage);
                 //sender should also sees the message on the right side of the chat
                 String senderDestn="/user/" + chatMessage.getSender() + "/queue/private";
                 System.out.println("sent message which i sent" + senderDestn);
                 messagingTemplate.convertAndSend(senderDestn,savedMessage);
             }
             catch(Exception e){
                 System.out.println("Error"+e.getMessage());
                 e.printStackTrace();
             }



         }
         else{
             System.out.println("Error sender or recipient not found "+chatMessage.getSender()+chatMessage.getRecipient());
         }

    }

}
