package com.denmit.eshop.historyservice.service.impl;

import com.denmit.eshop.historyservice.client.*;
import com.denmit.eshop.historyservice.dto.*;
import com.denmit.eshop.historyservice.mapper.HistoryMapper;
import com.denmit.eshop.historyservice.model.History;
import com.denmit.eshop.historyservice.model.enums.Status;
import com.denmit.eshop.historyservice.repository.HistoryRepository;
import com.denmit.eshop.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {

    private static final Map<String, String> ACTIONS = Map.ofEntries(
            Map.entry("add", "Product was added"),
            Map.entry("remove", "Product was removed"),
            Map.entry("create", "Order was created"),
            Map.entry("pay", "Order was paid"),
            Map.entry("attach", "File was attached"),
            Map.entry("replace", "File was replaced"),
            Map.entry("removeFile", "File was removed"),
            Map.entry("addComment", "Comment was added"),
            Map.entry("removeComment", "Comment was removed"),
            Map.entry("addFeedback", "Feedback was left"),
            Map.entry("removeFeedback", "Feedback was removed")
    );

    private static final Map<String, String> DESCRIPTIONS = Map.ofEntries(
            Map.entry("add", "%s (%s $) was added to cart"),
            Map.entry("remove", "%s (%s $) was removed from cart"),
            Map.entry("create", "Order %s (%s $) was created"),
            Map.entry("pay", "Order %s (%s $) was paid (Payment number: %s)"),
            Map.entry("attach", "File %s was attached to order %s"),
            Map.entry("replace", "File %s was replaced with file %s to order %s"),
            Map.entry("removeFile", "File %s was removed from order %s"),
            Map.entry("addComment", "Comment was added by %s to order %s"),
            Map.entry("removeComment", "Comment was removed by %s from order %s"),
            Map.entry("addFeedback", "Feedback with rate %s was left by %s to order %s"),
            Map.entry("removeFeedback", "Feedback with rate %s was removed by %s from order %s")
    );

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final AttachmentClient attachmentClient;
    private final UserClient userClient;
    private final CommentClient commentClient;
    private final FeedbackClient feedbackClient;

    @Override
    public List<HistoryResponseDto> getAllByOrderId(Long orderId, String buttonValue) {
        OrderHistoryResponseDto order = orderClient.getByIdForHistory(orderId);

        List<History> history = buttonValue.equals("Show All")
                ? historyRepository.findByOrderIdOrderByDate(orderId)
                : historyRepository.findByOrderId(orderId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "date"))).toList();

        log.info("{} history for order {}: {}", buttonValue.equals("Show All") ? "All" : "Last 5", orderId, history);

        return historyMapper.toDtos(history, userClient.getById(order.getUserId()).getName());
    }

    @Override
    @Transactional
    public void saveHistoryForAddedProduct(Long productId) {
        saveHistoryForProduct(productId, "add");
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedProduct(Long productId) {
        saveHistoryForProduct(productId, "remove");
    }

    @Override
    @Transactional
    public void saveHistoryForCreatedOrder(Long orderId) {
        saveHistoryForOrder(orderId, "create", null);
    }

    @Override
    @Transactional
    public void saveHistoryForCanceledOrder() {
        historyRepository.findAll().stream()
                .filter(history -> history.getStatus().equals(Status.IN_PROGRESS))
                .forEach(history -> history.setStatus(Status.CANCELED));
    }

    @Override
    @Transactional
    public void saveHistoryForOrderPayment(Long orderId, String paymentNumber) {
        saveHistoryForOrder(orderId, "pay", paymentNumber);
    }

    @Override
    @Transactional
    public void saveHistoryForAttachedFile(Long attachmentId, Long orderId) {
        saveHistoryForAttachment("attach", attachmentId, orderId, null);
    }

    @Override
    @Transactional
    public void saveHistoryForReplacedFile(Long attachmentId, Long orderId, String oldFileName) {
        saveHistoryForAttachment("replace", attachmentId, orderId, oldFileName);
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedFile(Long attachmentId, Long orderId) {
        saveHistoryForAttachment("removeFile", attachmentId, orderId, null);
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedFile(String fileName, Long orderId) {
        saveHistoryForAttachment("removeFile", null, orderId, fileName);
    }

    @Override
    @Transactional
    public void saveHistoryForAddedComment(Long commentId, Long orderId) {
        saveHistoryForComment(commentId, orderId, "addComment");
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedComment(Long commentId, Long orderId) {
        saveHistoryForComment(commentId, orderId, "removeComment");
    }

    @Override
    @Transactional
    public void saveHistoryForLeftFeedback(Long commentId, Long orderId) {
        saveHistoryForFeedback(commentId, orderId, "addFeedback");
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedFeedback(Long feedbackId, Long orderId) {
        saveHistoryForFeedback(feedbackId, orderId, "removeFeedback");
    }

    private void saveHistoryForProduct(Long productId, String actionKey) {
        ProductResponseDto product = productClient.getUnitForHistoryById(productId);
        String description = String.format(DESCRIPTIONS.get(actionKey), product.getTitle(), product.getPrice());

        saveHistoryParameters(ACTIONS.get(actionKey), description, null, null, Status.IN_PROGRESS);
    }

    private void saveHistoryForOrder(Long orderId, String actionKey, String paymentNumber) {
        OrderHistoryResponseDto order = orderClient.getByIdForHistory(orderId);
        String description = paymentNumber == null
                ? String.format(DESCRIPTIONS.get(actionKey), orderId, order.getTotalPrice())
                : String.format(DESCRIPTIONS.get(actionKey), orderId, order.getTotalPrice(), paymentNumber);

        saveHistoryParameters(ACTIONS.get(actionKey), description, order.getUserId(), orderId, Status.IN_PROGRESS);
        setHistoryParametersIfOrderIsCreated(order);
    }

    private void saveHistoryForAttachment(String action, Long attachmentId, Long orderId, String oldFileName) {
        AttachmentNameResponseDto attachment = attachmentId != null ? attachmentClient.getByIdForHistory(attachmentId) : null;
        String fileName = attachment != null ? attachment.getFileName() : oldFileName;

        String description = "replace".equals(action)
                ? String.format(DESCRIPTIONS.get("replace"), oldFileName, fileName, orderId)
                : String.format(DESCRIPTIONS.get(action), fileName, orderId);

        saveHistoryParameters(ACTIONS.get(action), description, orderClient.getByIdForHistory(orderId).getUserId(), orderId, Status.READY);
    }

    private void saveHistoryForComment(Long commentId, Long orderId, String actionKey) {
        CommentUserResponseDto comment = commentClient.getById(commentId);
        String description = String.format(DESCRIPTIONS.get(actionKey), comment.getUser(), orderId);

        saveHistoryParameters(ACTIONS.get(actionKey), description, orderClient.getByIdForHistory(orderId).getUserId(), orderId, Status.READY);
    }

    private void saveHistoryForFeedback(Long feedbackId, Long orderId, String actionKey) {
        FeedbackResponseDto feedback = feedbackClient.getById(feedbackId);
        String description = String.format(DESCRIPTIONS.get(actionKey), feedback.getRate(), feedback.getUser(), orderId);

        saveHistoryParameters(ACTIONS.get(actionKey), description, orderClient.getByIdForHistory(orderId).getUserId(), orderId, Status.READY);
    }

    private void saveHistoryParameters(String action, String description, Long userId, Long orderId, Status status) {
        History history = new History();

        history.setDate(LocalDateTime.now());
        history.setAction(action);
        history.setDescription(description);
        history.setOrderId(orderId);
        history.setStatus(status);
        history.setUserId(userId);

        historyRepository.save(history);
    }

    private void setHistoryParametersIfOrderIsCreated(OrderHistoryResponseDto order) {
        historyRepository.findAll().stream()
                .filter(history -> history.getStatus().equals(Status.IN_PROGRESS))
                .forEach(history -> {
                    history.setOrderId(order.getId());
                    history.setStatus(Status.READY);
                    history.setUserId(order.getUserId());
                });
    }
}
