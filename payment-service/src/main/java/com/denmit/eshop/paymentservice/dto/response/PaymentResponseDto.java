package com.denmit.eshop.paymentservice.dto.response;

import com.denmit.eshop.paymentservice.model.enums.PaymentMethod;
import com.denmit.eshop.paymentservice.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDto {

    private String paymentNumber;

    private Long orderId;

    private String payer;

    private PaymentStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;
}
