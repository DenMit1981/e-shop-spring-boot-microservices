package com.denmit.eshop.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderHistoryResponseDto {

    private Long id;

    private Long userId;

    private BigDecimal totalPrice;
}
