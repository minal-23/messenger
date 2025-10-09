package com.fullstack.messenger.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;//coz in response we get JWT token
    private UserDTO userDTO;


}
