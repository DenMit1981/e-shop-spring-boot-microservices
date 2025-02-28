package com.denmit.eshop.paymentservice.service;

import java.io.InputStream;

public interface MinioService {

    void upload(String fileName, InputStream inputStream);

    InputStream download(String fileName);
}
