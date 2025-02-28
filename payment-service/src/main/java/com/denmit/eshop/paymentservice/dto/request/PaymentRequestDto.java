package com.denmit.eshop.paymentservice.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class PaymentRequestDto {

    @Digits(integer = 10, fraction = 2, message = "Incorrect amount entered")
    @NotNull(message = "Amount field shouldn't be empty")
    private BigDecimal amount;
}
