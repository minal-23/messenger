package com.fullstack.messenger.service;

import com.fullstack.messenger.dto.LoginRequestDTO;
import com.fullstack.messenger.dto.LoginResponseDTO;
import com.fullstack.messenger.dto.RegisterRequestDTO;
import com.fullstack.messenger.dto.UserDTO;
import com.fullstack.messenger.jwt.JWTService;
import com.fullstack.messenger.model.User;
import com.fullstack.messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    public UserDTO signup(RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUserName(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }
        User user=new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode((registerRequestDTO.getPassword())));
        user.setEmail(registerRequestDTO.getEmail());
        User savedUser=userRepository.save(user);
        return convertToUserDTO(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user=userRepository.findByUserName(loginRequestDTO.getUsername())
                .orElseThrow(()-> new RuntimeException("not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()));
        String jwtToken=jwtService.generateToken(user);
        return LoginResponseDTO.builder()
                .token(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }
    public String logout(){

    }
    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

}
