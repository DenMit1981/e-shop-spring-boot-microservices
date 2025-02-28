package com.denmit.eshop.authenticationservice.kafka.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    public void sendMessage(Object message, String topic) {
        kafkaTemplate.send(topic, gson.toJson(message));
        log.info("New user registered successfully");
    }
}
