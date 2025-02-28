package com.denmit.eshop.historyservice.service;

import com.denmit.eshop.historyservice.dto.HistoryResponseDto;

import java.util.List;

public interface HistoryService {

    List<HistoryResponseDto> getAllByOrderId(Long orderId, String buttonValue);

    void saveHistoryForCreatedOrder(Long orderId);

    void saveHistoryForOrderPayment(Long orderId, String paymentNumber);

    void saveHistoryForCanceledOrder();

    void saveHistoryForAddedProduct(Long productId);

    void saveHistoryForRemovedProduct(Long productId);

    void saveHistoryForAttachedFile(Long attachmentId, Long orderId);

    void saveHistoryForReplacedFile(Long attachmentId, Long orderId, String oldFileName);

    void saveHistoryForRemovedFile(Long attachmentId, Long orderId);

    void saveHistoryForRemovedFile(String fileName, Long orderId);

    void saveHistoryForAddedComment(Long commentId, Long orderId);

    void saveHistoryForRemovedComment(Long commentId, Long orderId);

    void saveHistoryForLeftFeedback(Long commentId, Long orderId);

    void saveHistoryForRemovedFeedback(Long feedbackId, Long orderId);
}
