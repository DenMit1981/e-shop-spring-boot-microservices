package com.denmit.eshop.attachmentservice.client;

import com.denmit.eshop.attachmentservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "history-service", path = "/api/v1/history")
@SecuredFeignClient
public interface HistoryClient {

    @PostMapping("/order/{orderId}/attach-file/{attachmentId}")
    void saveHistoryForAttachedFile(@PathVariable("attachmentId") Long attachmentId,
                                    @PathVariable("orderId") Long orderId);

    @PostMapping("/order/{orderId}/replace-file/{fileId}")
    void saveHistoryForReplacedFile(@PathVariable("fileId") Long fileId,
                                    @PathVariable("orderId") Long orderId,
                                    @RequestParam("oldFileName") String oldFileName);

    @PostMapping("/order/{orderId}/remove-file/{fileId}")
    void saveHistoryForRemovedFile(@PathVariable("fileId") Long fileId,
                                   @PathVariable("orderId") Long orderId);

    @PostMapping("/order/{orderId}/remove-file-by-name/{fileName}")
    void saveHistoryForRemovedFileByName(@PathVariable("fileName") String fileName,
                                         @PathVariable("orderId") Long orderId);
}
