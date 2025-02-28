package com.denmit.eshop.paymentservice.controller;

import com.denmit.eshop.paymentservice.dto.request.BankTransferRequestDto;
import com.denmit.eshop.paymentservice.dto.request.CardPaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.response.BankTransferResponseDto;
import com.denmit.eshop.paymentservice.dto.response.CardPaymentResponseDto;
import com.denmit.eshop.paymentservice.dto.response.PaymentResponseDto;
import com.denmit.eshop.paymentservice.model.enums.PaymentMethod;
import com.denmit.eshop.paymentservice.security.provider.UserProvider;
import com.denmit.eshop.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Payment controller")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserProvider userProvider;

    @PostMapping("/order/{orderId}/card-payment")
    @Operation(summary = "Create card payment for order by buyer")
    @ApiResponse(responseCode = "201", description = "Paid by card",
            content = @Content(schema = @Schema(implementation = CardPaymentResponseDto.class)))
    public PaymentResponseDto createCardPayment(@RequestBody @Valid CardPaymentRequestDto cardPaymentRequestDto,
                                                @PathVariable Long orderId) {
        return paymentService.create(cardPaymentRequestDto, PaymentMethod.CARD_PAYMENT, Long.valueOf(userProvider.getUserId()), orderId);
    }

    @PostMapping("/order/{orderId}/bank-transfer")
    @Operation(summary = "Create bank transfer payment for order by buyer")
    @ApiResponse(responseCode = "201", description = "Paid by bank transfer",
            content = @Content(schema = @Schema(implementation = BankTransferResponseDto.class)))
    public PaymentResponseDto createBankTransfer(@RequestBody @Valid BankTransferRequestDto bankTransferRequestDto,
                                                 @PathVariable Long orderId) {
        return paymentService.create(bankTransferRequestDto, PaymentMethod.BANK_TRANSFER, Long.valueOf(userProvider.getUserId()), orderId);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getByOrderId(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getById(@PathVariable Long paymentId) {
        return paymentService.getById(paymentId);
    }

    @GetMapping
    @Operation(summary = "Get payment by number")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getByPaymentNumber(@RequestParam String number) {
        return paymentService.getByPaymentNumber(number);
    }
}
