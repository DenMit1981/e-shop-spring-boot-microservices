package com.denmit.eshop.feedbackservice.client;

import com.denmit.eshop.feedbackservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.feedbackservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", path = "/api/v1/orders")
@SecuredFeignClient
public interface OrderClient {

    @GetMapping("/{orderId}/comment-feedback")
    OrderUserResponseDto getOrderUserById(@PathVariable("orderId") Long orderId);

    @PostMapping("/{orderId}/feedback-message")
    void sendFeedbackMessageToOrder(@PathVariable Long orderId, @RequestParam Long feedbackId, @RequestParam Long userId);
}
