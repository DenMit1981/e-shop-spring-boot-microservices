package com.denmit.eshop.authenticationservice.mapper;

import com.denmit.eshop.authenticationservice.dto.request.UserRegisterRequestDto;
import com.denmit.eshop.authenticationservice.dto.response.UserLoginResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserRegisterResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserResponseDto;
import com.denmit.eshop.authenticationservice.model.User;

import com.denmit.eshop.authenticationservice.model.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", expression = "java(password)")
    User toUser(UserRegisterRequestDto userRegisterRequestDto, String password, Role role);

    UserRegisterResponseDto toUserRegisterResponseDto(User user, String message);

    @Mapping(source = "user.name", target = "username")
    UserLoginResponseDto toUserLoginResponseDto(User user, String token);

    UserResponseDto toUserResponseDto(User user);

    default List<UserResponseDto> toDtos(List<User> users) {
        return users.stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }
}
