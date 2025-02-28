package com.denmit.eshop.attachmentservice.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ACCESS_DENIED_FOR_ADMIN = "Access is allowed only for buyer";

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        res.setStatus(403);
        res.getWriter().write(ACCESS_DENIED_FOR_ADMIN);
    }
}

