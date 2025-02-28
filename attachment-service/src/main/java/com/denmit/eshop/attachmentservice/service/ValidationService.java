package com.denmit.eshop.attachmentservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ValidationService {

    void validateUploadFile(MultipartFile file);
}
