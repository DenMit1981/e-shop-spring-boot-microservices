package com.denmit.eshop.feedbackservice.service;

import com.denmit.eshop.feedbackservice.model.Feedback;

public interface KafkaService {

    void sendMessage(Feedback feedback);
}
