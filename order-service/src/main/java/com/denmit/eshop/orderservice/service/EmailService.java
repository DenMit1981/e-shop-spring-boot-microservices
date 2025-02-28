package com.denmit.eshop.orderservice.service;

public interface EmailService {

    void sendOrderDetailsMessage(Long orderId, String order, Long userId);

    void sendFeedbackMessage(Long orderId, Long feedbackId, Long userId);
}
