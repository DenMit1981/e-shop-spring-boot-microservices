package com.denmit.eshop.feedbackservice.service.impl;

import com.denmit.eshop.feedbackservice.client.UserClient;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackResponseDto;
import com.denmit.eshop.feedbackservice.mapper.FeedbackMapper;
import com.denmit.eshop.feedbackservice.model.Feedback;
import com.denmit.eshop.feedbackservice.service.KafkaService;
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

    private static final String TOPIC = "order.feedback";
    private static final String GROUP_ID = "order";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson = new GsonBuilder().create();
    private final FeedbackMapper feedbackMapper;
    private final UserClient userClient;

    @Override
    public void sendMessage(Feedback feedback) {
        FeedbackResponseDto feedbackDto = feedbackMapper.toDto(feedback, userClient.getById(feedback.getUserId()).getName());
        String json = gson.toJson(feedbackDto);

        Message<String> message = MessageBuilder.withPayload(json)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();

        kafkaTemplate.send(message);
        log.info("Feedback has been sent: {}", message);
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void receiveFeedback(String message) {
        FeedbackResponseDto feedback = gson.fromJson(message, FeedbackResponseDto.class);
        log.info("Feedback has been received (" + feedback + ")");
    }
}
