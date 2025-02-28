package com.denmit.eshop.commentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String name;
}
