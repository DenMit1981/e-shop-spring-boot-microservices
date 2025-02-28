package com.denmit.eshop.authenticationservice.service;

import com.denmit.eshop.authenticationservice.dto.request.UserLoginRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserRegisterRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserUpdateRequestDto;
import com.denmit.eshop.authenticationservice.dto.response.UserLoginResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserRegisterResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserRegisterResponseDto registration(UserRegisterRequestDto userRegisterRequestDto, String checkBoxValue);

    UserLoginResponseDto authentication(UserLoginRequestDto userDto);

    UserResponseDto getById(Long userid);

    List<UserResponseDto> getAllAdmins();

    UserRegisterResponseDto changePassword(UserUpdateRequestDto userDto, String login);
}
