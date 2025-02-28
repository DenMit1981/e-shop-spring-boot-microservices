package com.denmit.eshop.attachmentservice.service;

import com.denmit.eshop.attachmentservice.dto.AttachmentNameResponseDto;
import com.denmit.eshop.attachmentservice.dto.AttachmentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AttachmentService {

    AttachmentResponseDto upload(MultipartFile file, Long orderId, Long userId);

    List<AttachmentResponseDto> getAllByOrderId(Long orderId);

    AttachmentNameResponseDto getByIdForHistory(Long fileId);

    AttachmentResponseDto getInfo(Long fileId);

    InputStream download(Long fileId);

    AttachmentResponseDto replace(Long attachmentId, MultipartFile file, Long userId);

    void deleteById(Long fileId, Long userId);

    void deleteByName(String fileName, Long orderId, Long userId);
}
