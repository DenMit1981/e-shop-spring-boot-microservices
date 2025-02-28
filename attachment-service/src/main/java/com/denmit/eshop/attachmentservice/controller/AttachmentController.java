package com.denmit.eshop.attachmentservice.controller;

import com.denmit.eshop.attachmentservice.dto.AttachmentNameResponseDto;
import com.denmit.eshop.attachmentservice.dto.AttachmentResponseDto;
import com.denmit.eshop.attachmentservice.security.provider.UserProvider;
import com.denmit.eshop.attachmentservice.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attachments")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Attachment controller")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final UserProvider userProvider;

    @PostMapping(value = "/order/{orderId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Upload new file")
    @ResponseStatus(HttpStatus.CREATED)
    public AttachmentResponseDto uploadFile(@RequestParam("file") MultipartFile file,
                                            @PathVariable("orderId") Long orderId) {
        return attachmentService.upload(file, orderId, Long.valueOf(userProvider.getUserId()));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all attachments by order ID")
    @ResponseStatus(HttpStatus.OK)
    public List<AttachmentResponseDto> getAttachmentsByOrderId(@PathVariable Long orderId) {
        return attachmentService.getAllByOrderId(orderId);
    }

    @GetMapping("/{attachmentId}")
    @Operation(summary = "Download file by ID")
    @ResponseStatus(HttpStatus.OK)
    public InputStreamResource download(@PathVariable Long attachmentId) {
        return new InputStreamResource(attachmentService.download(attachmentId));
    }

    @GetMapping("/{attachmentId}/info")
    @Operation(summary = "Get file info by ID")
    @ResponseStatus(HttpStatus.OK)
    public AttachmentResponseDto getInfo(@PathVariable Long attachmentId) {
        return attachmentService.getInfo(attachmentId);
    }

    @GetMapping("/{attachmentId}/history")
    @Operation(summary = "Get file info by ID for history")
    @ResponseStatus(HttpStatus.OK)
    public AttachmentNameResponseDto getByIdForHistory(@PathVariable Long attachmentId) {
        return attachmentService.getByIdForHistory(attachmentId);
    }

    @PutMapping(value = "/{attachmentId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Replace one file with another one")
    @ResponseStatus(HttpStatus.OK)
    public AttachmentResponseDto replace(@PathVariable Long attachmentId,
                                         @RequestParam("file") MultipartFile file) {
        return attachmentService.replace(attachmentId, file, Long.valueOf(userProvider.getUserId()));
    }

    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete file by ID")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long attachmentId) {
        attachmentService.deleteById(attachmentId, Long.valueOf(userProvider.getUserId()));
    }

    @DeleteMapping("/{fileName}/order/{orderId}")
    @Operation(summary = "Delete file by name and order ID")
    @ResponseStatus(HttpStatus.OK)
    public List<AttachmentResponseDto> deleteByName(@PathVariable String fileName,
                                                    @PathVariable Long orderId) {
        attachmentService.deleteByName(fileName, orderId, Long.valueOf(userProvider.getUserId()));

        return attachmentService.getAllByOrderId(orderId);
    }
}
