package com.denmit.eshop.historyservice.client;

import com.denmit.eshop.historyservice.dto.FeedbackResponseDto;
import com.denmit.eshop.historyservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feedback-service", path = "/api/v1/feedbacks")
@SecuredFeignClient
public interface FeedbackClient {

    @GetMapping("/{feedbackId}")
    FeedbackResponseDto getById(@PathVariable Long feedbackId);
}
