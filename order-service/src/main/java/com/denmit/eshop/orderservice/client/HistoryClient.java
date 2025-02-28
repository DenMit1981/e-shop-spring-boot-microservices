package com.denmit.eshop.orderservice.client;

import com.denmit.eshop.orderservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "history-service", path = "/api/v1/history")
@SecuredFeignClient
public interface HistoryClient {

    @PostMapping("/add-product/{productId}")
    void saveHistoryForAddedProduct(@PathVariable("productId") Long productId);

    @PostMapping("/remove-product/{productId}")
    void saveHistoryForRemovedProduct(@PathVariable("productId") Long productId);

    @PostMapping("/order/{orderId}")
    void saveHistoryForCreatedOrder(@PathVariable("orderId") Long orderId);

    @PostMapping("/cancel-order")
    void saveHistoryForCanceledOrder();
}
