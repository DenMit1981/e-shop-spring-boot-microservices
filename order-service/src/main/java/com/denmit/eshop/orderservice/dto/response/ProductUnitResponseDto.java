package com.denmit.eshop.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductUnitResponseDto {

    private Long id;

    private String title;

    private BigDecimal price;

    private String description;
}
