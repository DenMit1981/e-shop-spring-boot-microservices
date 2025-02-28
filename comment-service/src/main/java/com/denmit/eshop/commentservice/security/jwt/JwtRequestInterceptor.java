package com.denmit.eshop.commentservice.security.jwt;

import com.denmit.eshop.commentservice.security.SecuredFeignClient;
import com.denmit.eshop.commentservice.security.model.CustomUserDetails;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (requestTemplate.feignTarget().type().isAnnotationPresent(SecuredFeignClient.class)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            requestTemplate.header("Authorization", "Bearer " + user.getJwt());
        }
    }
}
