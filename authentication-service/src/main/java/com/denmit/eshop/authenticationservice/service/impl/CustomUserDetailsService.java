package com.denmit.eshop.authenticationservice.service.impl;

import com.denmit.eshop.authenticationservice.exception.UserNotFoundException;
import com.denmit.eshop.authenticationservice.model.CustomUserDetails;
import com.denmit.eshop.authenticationservice.model.User;
import com.denmit.eshop.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User with login %s not found";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        return new CustomUserDetails(getByLogin(login));
    }

    private User getByLogin(String login) {
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, login)));
    }
}
