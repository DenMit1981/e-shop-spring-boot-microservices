package com.denmit.eshop.paymentservice.service;

public interface EmailService {

    void sendReceiptMessage(Long orderId, Long receiptId, Long userId);
}
