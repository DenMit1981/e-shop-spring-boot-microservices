package com.denmit.eshop.orderservice.controller;

import com.denmit.eshop.orderservice.dto.response.OrderAdminResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderHistoryResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.orderservice.security.provider.UserProvider;
import com.denmit.eshop.orderservice.service.EmailService;
import com.denmit.eshop.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Order controller")
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;
    private final UserProvider userProvider;

    @PostMapping
    @Operation(summary = "Create a new order by buyer")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto create() {
        return orderService.save(Long.valueOf(userProvider.getUserId()));
    }

    @GetMapping("/for-admin")
    @Operation(summary = "Get all orders for admin")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderAdminResponseDto> getAll(@RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                              @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                              @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        return orderService.getAllForAdmin(sortField, sortDirection, pageSize, pageNumber);
    }

    @GetMapping("/{orderId}/for-buyer")
    @Operation(summary = "Get order by ID for buyer")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getByIdForBuyer(@PathVariable("orderId") Long orderId) {
        return orderService.getByIdForBuyer(orderId);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID for admin")
    @ResponseStatus(HttpStatus.OK)
    public OrderAdminResponseDto getByIdForAdmin(@PathVariable("orderId") Long orderId) {
        return orderService.getByIdForAdmin(orderId);
    }

    @GetMapping("/{orderId}/history")
    @Operation(summary = "Get order dto for history by ID")
    @ResponseStatus(HttpStatus.OK)
    public OrderHistoryResponseDto getByIdForHistory(@PathVariable("orderId") Long orderId) {
        return orderService.getByIdForHistory(orderId);
    }

    @GetMapping("/{orderId}/comment-feedback")
    @Operation(summary = "Get order dto for comments and feedbacks by ID")
    @ResponseStatus(HttpStatus.OK)
    public OrderUserResponseDto getOrderUserById(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderUserById(orderId);
    }

    @GetMapping("/{orderId}/existence")
    @Operation(summary = "Check order existence by ID")
    @ResponseStatus(HttpStatus.OK)
    public void checkOrderExistenceById(@PathVariable("orderId") Long orderId) {
        orderService.checkOrderExistenceById(orderId);
    }

    @PostMapping("/{orderId}/feedback-message")
    @Operation(summary = "Send feedback message to order")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendFeedbackMessageToOrder(@PathVariable Long orderId,
                                           @RequestParam Long feedbackId, @RequestParam Long userId) {
        emailService.sendFeedbackMessage(orderId, feedbackId, userId);
    }
}
