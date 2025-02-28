package com.denmit.eshop.feedbackservice.client;

import com.denmit.eshop.feedbackservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "history-service", path = "/api/v1/history")
@SecuredFeignClient
public interface HistoryClient {

    @PostMapping("/order/{orderId}/leave-feedback/{feedbackId}")
    void saveHistoryForLeftFeedback(@PathVariable Long feedbackId, @PathVariable Long orderId);

    @PostMapping("/order/{orderId}/remove-feedback/{feedbackId}")
    void saveHistoryForRemovedFeedback(@PathVariable Long feedbackId, @PathVariable Long orderId);
}
