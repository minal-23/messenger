package com.fullstack.messenger.service;

import com.fullstack.messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;
    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }
    public void setUserOnlineStatus(String username,boolean isOnline){
        userRepository.userOnlineStatus(username,isOnline);
    }
}
