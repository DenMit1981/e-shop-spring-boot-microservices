package com.denmit.eshop.feedbackservice.security.provider.impl;

import com.denmit.eshop.feedbackservice.security.model.CustomUserDetails;
import com.denmit.eshop.feedbackservice.security.provider.UserProvider;
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
