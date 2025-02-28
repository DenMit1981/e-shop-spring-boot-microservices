package com.denmit.eshop.authenticationservice.controller;

import com.denmit.eshop.authenticationservice.dto.request.UserLoginRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserRegisterRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserUpdateRequestDto;
import com.denmit.eshop.authenticationservice.dto.response.UserLoginResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserRegisterResponseDto;
import com.denmit.eshop.authenticationservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication controller")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Registration a new user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegisterResponseDto registration(@RequestBody @Valid UserRegisterRequestDto userDto,
                                                @RequestParam(value = "isUserCheck", defaultValue = "default") String checkBoxValue) {
        return userService.registration(userDto, checkBoxValue);
    }

    @PostMapping("/signin")
    @Operation(summary = "Authentication and generation JWT token")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto authentication(@RequestBody @Valid UserLoginRequestDto userDto) {
        return userService.authentication(userDto);
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Change user password")
    @ResponseStatus(HttpStatus.OK)
    public UserRegisterResponseDto changePassword(@RequestBody @Valid UserUpdateRequestDto userDto, Principal principal) {
        return userService.changePassword(userDto, principal.getName());
    }
}
