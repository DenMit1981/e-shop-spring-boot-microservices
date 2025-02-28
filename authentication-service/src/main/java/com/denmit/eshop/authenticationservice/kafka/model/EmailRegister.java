package com.denmit.eshop.authenticationservice.kafka.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRegister {

    private String username;

    private String password;
}
