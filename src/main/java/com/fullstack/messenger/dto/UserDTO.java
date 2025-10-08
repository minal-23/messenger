package com.fullstack.messenger.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false,unique = true)
    String username;
    @Column(unique = true,nullable = false)
    String email;
    @Column(name="is_online",nullable = false)
    Boolean isOnline;
}
