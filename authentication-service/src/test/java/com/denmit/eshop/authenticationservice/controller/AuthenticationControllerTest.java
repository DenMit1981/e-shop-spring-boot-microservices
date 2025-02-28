package com.denmit.eshop.authenticationservice.controller;

import com.denmit.eshop.authenticationservice.dto.request.UserLoginRequestDto;
import com.denmit.eshop.authenticationservice.config.security.jwt.JwtTokenProvider;
import com.denmit.eshop.authenticationservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.denmit.eshop.authenticationservice.kafka.service.EmailProducerService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailProducerService emailProducerService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void login() throws Exception {
        UserLoginRequestDto user = new UserLoginRequestDto();
        user.setLogin("den@mail.ru");
        user.setPassword("pass");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        verify(userService, times(1)).authentication(user);
    }

    @Test
    void registration() {
    }

    @Test
    void valid() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void updatePassword() {
    }
}