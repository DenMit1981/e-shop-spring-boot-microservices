package com.denmit.eshop.orderservice.client;

import com.denmit.eshop.orderservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.orderservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feedback-service", path = "/api/v1/feedbacks")
@SecuredFeignClient
public interface FeedbackClient {

    @GetMapping("/{feedbackId}/mail")
    FeedbackMessageResponseDto getByIdForEmailMessage(@PathVariable Long feedbackId);
}
