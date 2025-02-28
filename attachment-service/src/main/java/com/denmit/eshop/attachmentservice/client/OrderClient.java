package com.denmit.eshop.attachmentservice.client;

import com.denmit.eshop.attachmentservice.dto.OrderUserResponseDto;
import com.denmit.eshop.attachmentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/api/v1/orders")
@SecuredFeignClient
public interface OrderClient {

    @GetMapping("/{orderId}/comment-feedback")
    OrderUserResponseDto getOrderUserById(@PathVariable("orderId") Long orderId);

    @GetMapping("/{orderId}/existence")
    void checkOrderExistenceById(@PathVariable("orderId") Long orderId);
}
