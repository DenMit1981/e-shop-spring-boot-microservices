package com.denmit.eshop.commentservice.security.provider.impl;

import com.denmit.eshop.commentservice.security.model.CustomUserDetails;
import com.denmit.eshop.commentservice.security.provider.UserProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityUserProvider implements UserProvider {

    @Override
    public String getUserId() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetails.getUserId();
    }
}
