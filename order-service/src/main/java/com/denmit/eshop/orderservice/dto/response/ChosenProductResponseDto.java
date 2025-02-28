package com.denmit.eshop.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChosenProductResponseDto {

    private BigDecimal price;

    private Long quantity;

    private List<Long> unitIds;
}
