package com.denmit.eshop.commentservice.client;

import com.denmit.eshop.commentservice.dto.response.UserResponseDto;
import com.denmit.eshop.commentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentication-service", path = "/api/v1/users")
@SecuredFeignClient
public interface UserClient {

    @GetMapping("/{userId}")
    UserResponseDto getById(@PathVariable Long userId);
}
