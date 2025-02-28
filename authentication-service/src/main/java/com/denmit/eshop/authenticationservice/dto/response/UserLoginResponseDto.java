package com.denmit.eshop.authenticationservice.dto.response;

import com.denmit.eshop.authenticationservice.model.enums.Role;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class UserLoginResponseDto {

    private String username;

    private String email;

    private Role role;

    private String token;
}
