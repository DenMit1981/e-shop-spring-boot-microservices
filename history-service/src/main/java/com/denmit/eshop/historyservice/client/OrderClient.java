package com.denmit.eshop.historyservice.client;

import com.denmit.eshop.historyservice.dto.OrderHistoryResponseDto;
import com.denmit.eshop.historyservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", path = "/api/v1/orders")
@SecuredFeignClient
public interface OrderClient {

    @GetMapping("/{orderId}/history")
    OrderHistoryResponseDto getByIdForHistory(@PathVariable("orderId") Long orderId);
}
