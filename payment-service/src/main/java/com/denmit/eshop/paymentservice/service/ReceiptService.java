package com.denmit.eshop.paymentservice.service;

import com.denmit.eshop.paymentservice.dto.response.ReceiptResponseDto;
import org.springframework.core.io.ByteArrayResource;

public interface ReceiptService {

    ReceiptResponseDto generate(Long userId, Long orderId);

    ByteArrayResource downloadReceiptFile(Long orderId);

    ReceiptResponseDto getById(Long receiptId);
}
