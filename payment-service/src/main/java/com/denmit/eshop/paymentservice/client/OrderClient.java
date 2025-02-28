package com.denmit.eshop.paymentservice.client;

import com.denmit.eshop.paymentservice.dto.response.OrderHistoryResponseDto;
import com.denmit.eshop.paymentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/api/v1/orders")
@SecuredFeignClient
public interface OrderClient {

    @GetMapping("/{orderId}/history")
    OrderHistoryResponseDto getByIdForHistory(@PathVariable("orderId") Long orderId);
}
