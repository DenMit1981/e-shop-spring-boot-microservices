package com.denmit.eshop.historyservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackResponseDto {

    private String user;

    private String rate;
}
