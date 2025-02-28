package com.denmit.eshop.authenticationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//    @Mock
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private AuthenticationService authenticationService;

    @Test
    public void testAuthentication() {
//        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
//        User userMock = mock(User.class);
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
//        UserDetails userDetailsMock = mock(UserDetails.class);
//        Mockito.when(customUserDetailsService.loadUserByUsername(loginRequestDto.getUsername())).thenReturn(userDetailsMock);
//
//        String tokenMock = "sdlfkjsdlkfjsdflsdlfksdjfsdfdslfsdjfljsdlfsdjfjsjdf";
//        Mockito.when(jwtTokenProvider.generateToken(userDetailsMock, userMock.getId())).thenReturn(tokenMock);
//
//        Mockito.when(userService.getByUsernameOrThrow(loginRequestDto.getUsername())).thenReturn(userMock);
//
//        TokenResponseDto result = authenticationService.authentication(loginRequestDto);
//
//        verify(authenticationManager, times(1)).authenticate(authenticationToken);
//        verify(customUserDetailsService, times(1)).loadUserByUsername("username");
//        assertEquals(tokenMock, result.token());
    }

    @Test
    public void testRegistration() {
//        String expectedMessage="Пользователь успешно зарегистрирован";
//        RegistrationRequestDto user  = mock(RegistrationRequestDto.class);
//
//        RegistrationResponseDto result = authenticationService.registration(user,"password");
//
//        verify(userService, times(1)).create(user, "password");
//        assertEquals(expectedMessage,result.getMessage());

    }
}