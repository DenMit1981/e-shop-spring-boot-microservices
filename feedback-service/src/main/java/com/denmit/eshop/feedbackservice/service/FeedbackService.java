package com.denmit.eshop.feedbackservice.service;

import com.denmit.eshop.feedbackservice.dto.request.FeedbackRequestDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackInfoResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {

    FeedbackInfoResponseDto add(FeedbackRequestDto feedbackDto, Long orderId, Long userId);

    List<FeedbackInfoResponseDto> getAllByOrderId(Long orderId, String buttonValue);

    FeedbackResponseDto getById(Long feedbackId);

    FeedbackMessageResponseDto getByIdForEmailMessage(Long feedbackId);

    void deleteById(Long feedbackId, Long userId);
}
