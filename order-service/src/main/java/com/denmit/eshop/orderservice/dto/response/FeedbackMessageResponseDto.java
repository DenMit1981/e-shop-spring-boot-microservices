package com.denmit.eshop.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackMessageResponseDto {

    private String rate;

    private String text;
}
