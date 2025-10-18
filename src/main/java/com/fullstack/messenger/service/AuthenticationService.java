package com.fullstack.messenger.service;

import com.fullstack.messenger.dto.LoginRequestDTO;
import com.fullstack.messenger.dto.LoginResponseDTO;
import com.fullstack.messenger.dto.RegisterRequestDTO;
import com.fullstack.messenger.dto.UserDTO;
import com.fullstack.messenger.jwt.JWTService;
import com.fullstack.messenger.model.User;
import com.fullstack.messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }
        User user=new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode((registerRequestDTO.getPassword())));
        user.setEmail(registerRequestDTO.getEmail());
        user.setIsOnline(false);
        User savedUser=userRepository.save(user);

        return convertToUserDTO(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace(); // temporarily log real reason
            throw new RuntimeException("Invalid credentials: " + e.getMessage());
        }
        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }


    public ResponseEntity<String> logout(){
        ResponseCookie responseCookie= ResponseCookie.from("JWT","")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                .body("logged out successfully");

    }
    public Map<String , Object> getOnlineUsers() {
        List<User> usersList = userRepository.findByIsOnlineTrue();
        Map<String, Object> onlineUsers = usersList.stream().collect(Collectors.toMap(User::getUsername, user -> user));
        return onlineUsers;
    }

        public UserDTO convertToUserDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

}
