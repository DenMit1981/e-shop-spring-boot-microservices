package com.denmit.eshop.paymentservice.kafka.service;

import com.denmit.eshop.paymentservice.model.Payment;

public interface KafkaService {

    void sendMessage(Payment payment);
}
