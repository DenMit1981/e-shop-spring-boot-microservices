package com.denmit.eshop.orderservice.kafka.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRegister {

    private Long id;

    String user;

    private String description;

    private BigDecimal totalPrice;
}
