package com.denmit.eshop.commentservice.client;

import com.denmit.eshop.commentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "history-service", path = "/api/v1/history")
@SecuredFeignClient
public interface HistoryClient {

    @PostMapping("/order/{orderId}/add-comment/{commentId}")
    void saveHistoryForAddedComment(@PathVariable Long commentId, @PathVariable Long orderId);

    @PostMapping("/order/{orderId}/remove-comment/{commentId}")
    void saveHistoryForRemovedComment(@PathVariable Long commentId, @PathVariable Long orderId);
}
