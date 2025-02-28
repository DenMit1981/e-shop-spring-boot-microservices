package com.denmit.eshop.orderservice.kafka.service.impl;

import com.denmit.eshop.orderservice.client.UserClient;
import com.denmit.eshop.orderservice.dto.response.OrderAdminResponseDto;
import com.denmit.eshop.orderservice.kafka.model.OrderRegister;
import com.denmit.eshop.orderservice.kafka.service.KafkaService;
import com.denmit.eshop.orderservice.model.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    private static final String TOPIC = "order.create";
    private static final String GROUP_ID = "order";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson = new GsonBuilder().create();
    private final ModelMapper mapper;
    private final UserClient userClient;

    @Override
    public void sendMessage(Order order) {
        OrderRegister orderDto = mapper.map(order, OrderRegister.class);
        orderDto.setUser(userClient.getById(order.getUserId()).getName());

        String json = gson.toJson(orderDto);

        Message<String> message = MessageBuilder.withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();

        kafkaTemplate.send(message);
        log.info("Order has been sent: {}", message);
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void receiveOrder(String message) {
        OrderRegister orderDto = gson.fromJson(message, OrderRegister.class);
        OrderAdminResponseDto order = mapper.map(orderDto, OrderAdminResponseDto.class);
        log.info("Order № {} has been received (user: {}, total price: {})", order.getId(), order.getUser(), order.getTotalPrice());
    }
}

