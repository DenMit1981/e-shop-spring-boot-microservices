package com.denmit.eshop.orderservice.dto.request;

import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
public class ProductRequestDto {

    private String title;

    private BigDecimal price;
}
