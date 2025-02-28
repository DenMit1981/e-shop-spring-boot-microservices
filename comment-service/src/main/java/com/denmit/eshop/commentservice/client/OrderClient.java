package com.denmit.eshop.commentservice.client;

import com.denmit.eshop.commentservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.commentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/api/v1/orders")
@SecuredFeignClient
public interface OrderClient {

    @GetMapping("/{orderId}/comment-feedback")
    OrderUserResponseDto getOrderUserById(@PathVariable("orderId") Long orderId);
}
