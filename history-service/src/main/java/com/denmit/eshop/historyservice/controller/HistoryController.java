package com.denmit.eshop.historyservice.controller;

import com.denmit.eshop.historyservice.dto.HistoryResponseDto;
import com.denmit.eshop.historyservice.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "History controller")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/add-product/{productId}")
    @Operation(summary = "Save history for added product")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForAddedProduct(@PathVariable Long productId) {
        historyService.saveHistoryForAddedProduct(productId);
    }

    @PostMapping("/remove-product/{productId}")
    @Operation(summary = "Save history for removed product")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForRemovedProduct(@PathVariable Long productId) {
        historyService.saveHistoryForRemovedProduct(productId);
    }

    @PostMapping("/order/{orderId}")
    @Operation(summary = "Save history for created order")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForCreatedOrder(@PathVariable Long orderId) {
        historyService.saveHistoryForCreatedOrder(orderId);
    }

    @PostMapping("/order/{orderId}/payment/{paymentNumber}")
    @Operation(summary = "Save history for order payment")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForOrderPayment(@PathVariable Long orderId, @PathVariable String paymentNumber) {
        historyService.saveHistoryForOrderPayment(orderId, paymentNumber);
    }

    @PostMapping("/cancel-order")
    @Operation(summary = "Save history for canceled order")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForCanceledOrder() {
        historyService.saveHistoryForCanceledOrder();
    }

    @PostMapping("/order/{orderId}/attach-file/{fileId}")
    @Operation(summary = "Save history for attached file")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForAttachedFile(@PathVariable Long fileId, @PathVariable Long orderId) {
        historyService.saveHistoryForAttachedFile(fileId, orderId);
    }

    @PostMapping("/order/{orderId}/replace-file/{fileId}")
    @Operation(summary = "Save history for replaced file")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForReplacedFile(@PathVariable Long fileId, @PathVariable Long orderId,
                                           @RequestParam String oldFileName) {
        historyService.saveHistoryForReplacedFile(fileId, orderId, oldFileName);
    }

    @PostMapping("/order/{orderId}/remove-file/{fileId}")
    @Operation(summary = "Save history for removed file by file ID")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForRemovedFile(@PathVariable Long fileId, @PathVariable Long orderId) {
        historyService.saveHistoryForRemovedFile(fileId, orderId);
    }

    @PostMapping("/order/{orderId}/remove-file-by-name/{fileName}")
    @Operation(summary = "Save history for removed file by file name")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForRemovedFileByName(@PathVariable String fileName, @PathVariable Long orderId) {
        historyService.saveHistoryForRemovedFile(fileName, orderId);
    }

    @PostMapping("/order/{orderId}/add-comment/{commentId}")
    @Operation(summary = "Save history for added comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForAddedComment(@PathVariable Long commentId, @PathVariable Long orderId) {
        historyService.saveHistoryForAddedComment(commentId, orderId);
    }

    @PostMapping("/order/{orderId}/remove-comment/{commentId}")
    @Operation(summary = "Save history for removed comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForRemovedComment(@PathVariable Long commentId, @PathVariable Long orderId) {
        historyService.saveHistoryForRemovedComment(commentId, orderId);
    }

    @PostMapping("/order/{orderId}/leave-feedback/{feedbackId}")
    @Operation(summary = "Save history for left feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForLeftFeedback(@PathVariable Long feedbackId, @PathVariable Long orderId) {
        historyService.saveHistoryForLeftFeedback(feedbackId, orderId);
    }

    @PostMapping("/order/{orderId}/remove-feedback/{feedbackId}")
    @Operation(summary = "Save history for removed feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHistoryForRemovedFeedback(@PathVariable Long feedbackId, @PathVariable Long orderId) {
        historyService.saveHistoryForRemovedFeedback(feedbackId, orderId);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all order history")
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryResponseDto> getAllHistoryByOrderId(@PathVariable Long orderId,
                                                           @RequestParam(defaultValue = "default") String buttonValue) {
        return historyService.getAllByOrderId(orderId, buttonValue);
    }
}
