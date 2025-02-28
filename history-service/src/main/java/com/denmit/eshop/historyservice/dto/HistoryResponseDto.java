package com.denmit.eshop.historyservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryResponseDto {

    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String user;

    private String action;

    private String description;
}
