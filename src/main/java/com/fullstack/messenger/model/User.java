package com.fullstack.messenger.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="User")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false,unique = true)
    String username;
    @Column(unique = true,nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @Column(name="is_online",nullable = false)
    Boolean isOnline;

}
