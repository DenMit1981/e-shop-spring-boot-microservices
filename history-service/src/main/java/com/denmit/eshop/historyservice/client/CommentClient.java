package com.denmit.eshop.historyservice.client;

import com.denmit.eshop.historyservice.dto.CommentUserResponseDto;
import com.denmit.eshop.historyservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-service", path = "/api/v1/comments")
@SecuredFeignClient
public interface CommentClient {

    @GetMapping("/{commentId}")
    CommentUserResponseDto getById(@PathVariable Long commentId);
}
