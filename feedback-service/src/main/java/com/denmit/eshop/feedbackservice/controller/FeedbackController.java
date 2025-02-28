package com.denmit.eshop.feedbackservice.controller;

import com.denmit.eshop.feedbackservice.dto.request.FeedbackRequestDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackInfoResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackResponseDto;
import com.denmit.eshop.feedbackservice.security.provider.UserProvider;
import com.denmit.eshop.feedbackservice.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedbacks")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Feedback controller")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserProvider userProvider;

    @PostMapping("/order/{orderId}")
    @Operation(summary = "Add new feedback to order by buyer")
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackInfoResponseDto add(@RequestBody @Valid FeedbackRequestDto feedbackRequestDto,
                                       @PathVariable Long orderId) {
        return feedbackService.add(feedbackRequestDto, orderId, Long.valueOf(userProvider.getUserId()));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all order feedbacks")
    @ResponseStatus(HttpStatus.OK)
    public List<FeedbackInfoResponseDto> getAllByOrderId(@PathVariable Long orderId,
                                                         @RequestParam(defaultValue = "default") String buttonValue) {
        return feedbackService.getAllByOrderId(orderId, buttonValue);
    }

    @GetMapping("/{feedbackId}")
    @Operation(summary = "Get feedback by ID")
    @ResponseStatus(HttpStatus.OK)
    public FeedbackResponseDto getById(@PathVariable Long feedbackId) {
        return feedbackService.getById(feedbackId);
    }

    @GetMapping("/{feedbackId}/mail")
    @Operation(summary = "Get feedback info by ID for email message")
    @ResponseStatus(HttpStatus.OK)
    public FeedbackMessageResponseDto getByIdForEmailMessage(@PathVariable Long feedbackId) {
        return feedbackService.getByIdForEmailMessage(feedbackId);
    }

    @DeleteMapping("/{feedbackId}")
    @Operation(summary = "Delete feedback by ID")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long feedbackId) {
        feedbackService.deleteById(feedbackId, Long.valueOf(userProvider.getUserId()));
    }
}
