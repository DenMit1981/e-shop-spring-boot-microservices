package com.denmit.eshop.paymentservice.kafka.service.impl;

import com.denmit.eshop.paymentservice.client.UserClient;
import com.denmit.eshop.paymentservice.kafka.model.PaymentRegister;
import com.denmit.eshop.paymentservice.kafka.service.KafkaService;
import com.denmit.eshop.paymentservice.mapper.PaymentMapper;
import com.denmit.eshop.paymentservice.model.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private static final String TOPIC = "order.pay";
    private static final String GROUP_ID = "order";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson = new GsonBuilder().create();
    private final PaymentMapper paymentMapper;
    private final UserClient userClient;

    @Override
    public void sendMessage(Payment payment) {
        PaymentRegister paymentDto = paymentMapper.toPaymentRegister(payment, userClient.getById(payment.getUserId()).getName());
        String json = gson.toJson(paymentDto);

        Message<String> message = MessageBuilder.withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();

        kafkaTemplate.send(message);
        log.info("Payment has been sent: {}", message);
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void receivePayment(String message) {
        PaymentRegister payment = gson.fromJson(message, PaymentRegister.class);
        log.info("Payment № {} has been received (" + payment + ")", payment.getPaymentNumber());
    }
}

