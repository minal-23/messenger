package com.fullstack.messenger.controller;

import com.fullstack.messenger.dto.LoginRequestDTO;
import com.fullstack.messenger.dto.LoginResponseDTO;
import com.fullstack.messenger.dto.RegisterRequestDTO;
import com.fullstack.messenger.dto.UserDTO;
import com.fullstack.messenger.service.AuthenticationService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    public AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(authenticationService.signup(registerRequestDTO));
    }
    @PostMapping("/login")
        public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO=authenticationService.login(loginRequestDTO);
        ResponseCookie responseCookie=ResponseCookie.from("JWT",loginResponseDTO.getToken())
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
        @GetMapping("/getcurrentuser")
        public ResponseEntity<?> getCurrentUser(Authentication authentication){
            if(authentication==null)return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER UNAI=UTHOIZED");
        }

}
