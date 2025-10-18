package com.fullstack.messenger.controller;

import com.fullstack.messenger.dto.LoginRequestDTO;
import com.fullstack.messenger.dto.LoginResponseDTO;
import com.fullstack.messenger.dto.RegisterRequestDTO;
import com.fullstack.messenger.dto.UserDTO;
import com.fullstack.messenger.model.User;
import com.fullstack.messenger.repository.UserRepository;
import com.fullstack.messenger.service.AuthenticationService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    public AuthenticationService authenticationService;
    @Autowired
    public UserRepository userRepository;
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(authenticationService.signup(registerRequestDTO));
    }
    @PostMapping("/login")
        public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO=authenticationService.login(loginRequestDTO);
        ResponseCookie responseCookie=ResponseCookie.from("JWT",loginResponseDTO.getToken())
                //fetch cookiee from cached cookie as JWT
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1*60*60)
                .sameSite("strict")
                .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(loginResponseDTO.getUserDTO());
        }
        @PostMapping("/logout")
        public ResponseEntity<String> logout(){
        return authenticationService.logout();
        }
//        @GetMapping("/getonlineusers")
//        public ResponseEntity<Map<String,Object>> getOnlineUsers(){
//            return ResponseEntity.ok(authenticationService.getOnlineUsers());
//            }
@GetMapping("/getonlineusers")
public ResponseEntity<Map<String, Object>> getOnlineUsers(){
    return ResponseEntity.ok(authenticationService.getOnlineUsers());
}

        @GetMapping("/getcurrentuser")
        public ResponseEntity<?> getCurrentUser(Authentication authentication){
            if(authentication==null)return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER UNAI=UTHOIZED");
            String username=authentication.name();
            User user = userRepository.findByUsername(username).orElseThrow(()->
                    new RuntimeException("User not found"));
            return ResponseEntity.ok(convertToUserDTO(user));//returning the userDtO , not gonnal return the user directly,
            //first will convert user to userDTO
        }
        public UserDTO convertToUserDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        return userDTO;
        }



}
