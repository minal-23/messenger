package com.fullstack.messenger.listener;

import com.fullstack.messenger.model.ChatMessage;
import com.fullstack.messenger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

//public class WebSocketListener {
//    @Autowired
//    UserService userService;
//    //connection handling,disoonecction handling someone added or lft thr group
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;
//    private static final Logger logger= LoggerFactory.getLogger(WebSocketListener.class);
//@EventListener
//    public void handleWebSocketConnectionListener(SessionConnectedEvent event){
//    logger.info("Coneected");
//}
//public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
//    StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
//    String username=headerAccessor.getSessionAttributes().get("username").toString();
//    userService.setUserOnlineStatus(username,false);
//    System.out.println("User discconnected");
//    ChatMessage chatMessage=new ChatMessage();
//    chatMessage.setType(ChatMessage.MessageType.LEAVE);
//    chatMessage.setSender(username);
//    messagingTemplate.convertAndSend("/topic/public",chatMessage);
//}
//}
@Component
public class WebSocketListener {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

    @EventListener
    public void handleWebsocketConnectListener(SessionConnectedEvent event){
        logger.info("Connected to websocket");
    }

    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        userService.setUserOnlineStatus(username, false);

        System.out.println("User disconnected from websocket");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        chatMessage.setSender(username);
        messagingTemplate.convertAndSend("/topic/public", chatMessage);

    }
}