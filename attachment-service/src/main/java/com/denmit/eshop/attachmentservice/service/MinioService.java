package com.denmit.eshop.attachmentservice.service;

import java.io.InputStream;

public interface MinioService {

    void upload(String fileName, InputStream inputStream);

    InputStream downloadFile(String fileName);

    void removeFile(String fileName);
}
