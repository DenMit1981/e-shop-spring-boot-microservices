package com.denmit.eshop.authenticationservice.service.impl;

import com.denmit.eshop.authenticationservice.config.security.jwt.JwtTokenProvider;
import com.denmit.eshop.authenticationservice.dto.request.UserLoginRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserRegisterRequestDto;
import com.denmit.eshop.authenticationservice.dto.request.UserUpdateRequestDto;
import com.denmit.eshop.authenticationservice.dto.response.UserLoginResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserRegisterResponseDto;
import com.denmit.eshop.authenticationservice.dto.response.UserResponseDto;
import com.denmit.eshop.authenticationservice.exception.*;
import com.denmit.eshop.authenticationservice.kafka.model.EmailRegister;
import com.denmit.eshop.authenticationservice.kafka.service.EmailProducerService;
import com.denmit.eshop.authenticationservice.mapper.UserMapper;
import com.denmit.eshop.authenticationservice.model.User;
import com.denmit.eshop.authenticationservice.model.enums.Role;
import com.denmit.eshop.authenticationservice.repository.UserRepository;
import com.denmit.eshop.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String USER_IS_PRESENT = "User with login %s is already present";
    private static final String USER_NOT_FOUND = "User with %s %s not found";
    private static final String USER_HAS_ANOTHER_PASSWORD = "User with login %s has another password. " +
            "Go to register or enter valid credentials";
    private static final String CHECKBOX_IS_NOT_CLICKED = "You should not be here.\n" +
            "Please, agree with the terms of service first\n";
    private static final String ADMIN = "admin";
    private static final String SUCCESSFUL_REGISTRATION = "User registered successfully";
    private static final String INCORRECT_PASSWORD = "The current password is entered incorrectly";
    private static final String PASSWORDS_DO_NOT_MATCH = "Passwords don't match";
    private static final String SUCCESSFUL_CHANGE_PASSWORD = "Password has been changed successfully";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailProducerService emailProducerService;

    @Value("${topic.register}")
    private String topicRegistration;

    @Override
    @Transactional
    public UserRegisterResponseDto registration(UserRegisterRequestDto userRegisterRequestDto, String checkBoxValue) {
        validateUserBeforeSave(userRegisterRequestDto, checkBoxValue);

        String password = passwordEncoder.encode(userRegisterRequestDto.getPassword());
        Role role = getUserRoleByLogin(userRegisterRequestDto.getEmail());
        User user = userMapper.toUser(userRegisterRequestDto, password, role);

        userRepository.save(user);

        log.info("New user : {}", user);

        sendRegisterMessage(user);

        return userMapper.toUserRegisterResponseDto(user, SUCCESSFUL_REGISTRATION);
    }

    @Override
    @Transactional
    public UserLoginResponseDto authentication(UserLoginRequestDto userLoginRequestDto) {
        User user = findByLoginAndPassword(userLoginRequestDto.getLogin(), userLoginRequestDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole(), String.valueOf(user.getId()));

        return userMapper.toUserLoginResponseDto(user, token);
    }

    @Override
    public UserResponseDto getById(Long userId) {
        return userMapper.toUserResponseDto(findById(userId));
    }

    @Override
    public List<UserResponseDto> getAllAdmins() {
        return userMapper.toDtos(userRepository.findByRole(Role.ROLE_ADMIN));
    }

    @Override
    @Transactional
    public UserRegisterResponseDto changePassword(UserUpdateRequestDto userDto, String login) {
        User user = findByLogin(login);

        if (!passwordEncoder.matches(userDto.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException(INCORRECT_PASSWORD);
        }

        if (!userDto.getNewPassword().equals(userDto.getConfirmPassword())) {
            throw new PasswordMismatchException(PASSWORDS_DO_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        userRepository.save(user);

        return userMapper.toUserRegisterResponseDto(user, SUCCESSFUL_CHANGE_PASSWORD);
    }

    private User findById(Long userid) {
        return userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, "id", userid)));
    }

    private User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException(String.format(USER_HAS_ANOTHER_PASSWORD, login));
        }

        return user;
    }

    private User findByLogin(String login) {
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, "login", login)));
    }

    private void validateUserBeforeSave(UserRegisterRequestDto userDto, String checkBoxValue) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserIsPresentException(String.format(USER_IS_PRESENT, userDto.getEmail()));
        }
        if (!"yes".equals(checkBoxValue)) {
            throw new CheckBoxException(CHECKBOX_IS_NOT_CLICKED);
        }
    }

    private Role getUserRoleByLogin(String login) {
        return login.contains(ADMIN) ? Role.ROLE_ADMIN : Role.ROLE_BUYER;
    }

    private void sendRegisterMessage(User user) {
        emailProducerService.sendMessage(EmailRegister.builder()
                .password(user.getPassword())
                .username(user.getName()), topicRegistration);
    }
}
