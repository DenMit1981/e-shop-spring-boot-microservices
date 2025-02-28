package com.denmit.eshop.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderAdminResponseDto extends OrderResponseDto {

    private Long id;

    private String user;
}
