package com.fullstack.messenger.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;//coz in response we get JWT token
    private UserDTO userDTO;


}
