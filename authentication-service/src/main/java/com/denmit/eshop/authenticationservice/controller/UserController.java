package com.denmit.eshop.authenticationservice.controller;

import com.denmit.eshop.authenticationservice.dto.response.UserResponseDto;
import com.denmit.eshop.authenticationservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User controller")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @GetMapping("/role-admin")
    @Operation(summary = "Get all admins")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllAdmins() {
        return userService.getAllAdmins();
    }
}
