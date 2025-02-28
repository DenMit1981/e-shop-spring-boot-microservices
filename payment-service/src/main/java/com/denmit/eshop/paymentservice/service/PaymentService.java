package com.denmit.eshop.paymentservice.service;

import com.denmit.eshop.paymentservice.dto.request.PaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.response.PaymentResponseDto;
import com.denmit.eshop.paymentservice.model.enums.PaymentMethod;

public interface PaymentService {

    PaymentResponseDto create(PaymentRequestDto paymentRequestDto, PaymentMethod paymentMethod,
                              Long userId, Long orderId);

    PaymentResponseDto getById(Long paymentId);

    PaymentResponseDto getByPaymentNumber(String paymentNumber);

    PaymentResponseDto getByOrderId(Long orderId);
}
