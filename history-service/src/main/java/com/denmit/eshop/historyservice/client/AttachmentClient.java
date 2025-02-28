package com.denmit.eshop.historyservice.client;

import com.denmit.eshop.historyservice.dto.AttachmentNameResponseDto;
import com.denmit.eshop.historyservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "attachment-service", path = "/api/v1/attachments")
@SecuredFeignClient
public interface AttachmentClient {

    @GetMapping("/{attachmentId}/history")
    AttachmentNameResponseDto getByIdForHistory(@PathVariable Long attachmentId);
}
