package com.denmit.eshop.orderservice.kafka.service;

import com.denmit.eshop.orderservice.model.Order;

public interface KafkaService {

    void sendMessage(Order order);
}
