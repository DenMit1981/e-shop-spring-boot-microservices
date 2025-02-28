package com.denmit.eshop.feedbackservice.service.impl;

import com.denmit.eshop.feedbackservice.client.HistoryClient;
import com.denmit.eshop.feedbackservice.client.OrderClient;
import com.denmit.eshop.feedbackservice.client.UserClient;
import com.denmit.eshop.feedbackservice.dto.request.FeedbackRequestDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackInfoResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.feedbackservice.exception.FeedbackAccessException;
import com.denmit.eshop.feedbackservice.exception.FeedbackNotFoundException;
import com.denmit.eshop.feedbackservice.mapper.FeedbackMapper;
import com.denmit.eshop.feedbackservice.model.Feedback;
import com.denmit.eshop.feedbackservice.repository.FeedbackRepository;
import com.denmit.eshop.feedbackservice.service.FeedbackService;
import com.denmit.eshop.feedbackservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final HistoryClient historyClient;
    private final KafkaService kafkaService;

    @Override
    public FeedbackInfoResponseDto add(FeedbackRequestDto feedbackDto, Long orderId, Long userId) {
        OrderUserResponseDto order = checkAccess(orderId, userId);

        Feedback feedback = saveFeedback(feedbackDto, order, orderId);

        historyClient.saveHistoryForLeftFeedback(feedback.getId(), orderId);
        orderClient.sendFeedbackMessageToOrder(orderId, feedback.getId(), userId);
        kafkaService.sendMessage(feedback);

        return feedbackMapper.toInfoDto(feedback, userClient.getById(order.getUserId()).getName());
    }

    @Override
    public List<FeedbackInfoResponseDto> getAllByOrderId(Long orderId, String buttonValue) {

        OrderUserResponseDto order = orderClient.getOrderUserById(orderId);

        List<Feedback> feedbacks = buttonValue.equals("Show All")
                ? feedbackRepository.findByOrderIdOrderByDate(orderId)
                : feedbackRepository.findByOrderId(orderId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "date"))).toList();

        log.info("{} feedbacks for order {}: {}", buttonValue.equals("Show All") ? "All" : "Last 5", orderId, feedbacks);

        return feedbackMapper.toDtos(feedbacks, userClient.getById(order.getUserId()).getName());
    }

    @Override
    public FeedbackResponseDto getById(Long feedbackId) {
        Feedback feedback = findById(feedbackId);

        return feedbackMapper.toDto(feedback, userClient.getById(feedback.getUserId()).getName());
    }

    @Override
    public FeedbackMessageResponseDto getByIdForEmailMessage(Long feedbackId) {
        return feedbackMapper.toMessageDto(findById(feedbackId));
    }

    @Override
    public void deleteById(Long feedbackId, Long userId) {
        Feedback feedback = findById(feedbackId);

        checkAccess(feedback.getOrderId(), userId);
        historyClient.saveHistoryForRemovedFeedback(feedbackId, feedback.getOrderId());

        removeFeedback(feedback);
    }

    @Transactional
    private Feedback saveFeedback(FeedbackRequestDto feedbackDto, OrderUserResponseDto order, Long orderId) {
        Feedback feedback = feedbackMapper.toEntity(feedbackDto);

        feedback.setOrderId(orderId);
        feedback.setUserId(order.getUserId());
        feedback.setDate(LocalDateTime.now());

        log.info("New feedback has just been added to order {}: {}", orderId, feedback.getRate());

        return feedbackRepository.save(feedback);
    }

    @Transactional
    private void removeFeedback(Feedback feedback) {
        feedbackRepository.deleteById(feedback.getId());

        log.info("Feedback {} has just been deleted from order {}", feedback.getId(), feedback.getOrderId());
    }

    private Feedback findById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(FeedbackNotFoundException::new);
    }

    private OrderUserResponseDto checkAccess(Long orderId, Long userId) {
        OrderUserResponseDto order = orderClient.getOrderUserById(orderId);

        if (!order.getUserId().equals(userId)) {
            throw new FeedbackAccessException();
        }

        return order;
    }
}
