package com.denmit.eshop.goodsservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartProductResponseDto extends ProductResponseDto {

    private Long quantity;

    private BigDecimal totalPrice;
}
