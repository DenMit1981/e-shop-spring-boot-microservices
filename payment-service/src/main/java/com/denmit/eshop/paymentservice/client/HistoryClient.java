package com.denmit.eshop.paymentservice.client;

import com.denmit.eshop.paymentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "history-service", path = "/api/v1/history")
@SecuredFeignClient
public interface HistoryClient {

    @PostMapping("/order/{orderId}/payment/{paymentNumber}")
    void saveHistoryForOrderPayment(@PathVariable Long orderId, @PathVariable String paymentNumber);
}

