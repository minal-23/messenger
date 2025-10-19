package com.fullstack.messenger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//This WebSocketConfig class configures your WebSocket setup and the
//        built-in message broker that Spring provides for real-time messaging.
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        //etting up internal broker
        config.enableSimpleBroker("/topic","/queue","/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){//registering a stomp endpoint on which i will be doing the socket
        //communicatuon
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173","http://localhost:3000")
                .withSockJS();//sockJS fallback support //as few browsers do not suppot

    }

}
