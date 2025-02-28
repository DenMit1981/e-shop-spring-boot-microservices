package com.denmit.eshop.historyservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {

    private String title;

    private BigDecimal price;
}
