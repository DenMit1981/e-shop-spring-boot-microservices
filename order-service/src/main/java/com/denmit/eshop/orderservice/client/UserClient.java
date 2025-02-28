package com.denmit.eshop.orderservice.client;

import com.denmit.eshop.orderservice.dto.response.UserResponseDto;
import com.denmit.eshop.orderservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "authentication-service", path = "/api/v1/users")
@SecuredFeignClient
public interface UserClient {

    @GetMapping("/{userId}")
    UserResponseDto getById(@PathVariable Long userId);

    @GetMapping("/role-admin")
    List<UserResponseDto> getAllAdmins();
}
