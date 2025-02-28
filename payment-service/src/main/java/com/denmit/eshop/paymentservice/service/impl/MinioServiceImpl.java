package com.denmit.eshop.paymentservice.service.impl;

import com.denmit.eshop.paymentservice.config.MinioProperties;
import com.denmit.eshop.paymentservice.service.MinioService;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    @PostConstruct
    private void init() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
            }
        } catch (IOException | MinioException | GeneralSecurityException e) {
            log.error("Error uploading Minio: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void upload(String fileName, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (IOException | MinioException | GeneralSecurityException e) {
            log.error("Error uploading file to Minio", e);
            throw new RuntimeException("Error uploading file to Minio", e);
        }
    }

    @Override
    public InputStream download(String fileName) {
        InputStream inputStream;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        } catch (IOException | MinioException | GeneralSecurityException e) {
            log.error("Error downloading file");
            return null;
        }
        return inputStream;
    }
}
