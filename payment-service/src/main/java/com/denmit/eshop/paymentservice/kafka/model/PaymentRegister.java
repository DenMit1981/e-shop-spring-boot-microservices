package com.denmit.eshop.paymentservice.kafka.model;

import com.denmit.eshop.paymentservice.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRegister {

    private String paymentNumber;

    private Long orderId;

    private String payer;

    private PaymentStatus status;

    private BigDecimal amount;
}
