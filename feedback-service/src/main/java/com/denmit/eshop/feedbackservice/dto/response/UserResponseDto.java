package com.denmit.eshop.feedbackservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String name;

    private String email;
}
