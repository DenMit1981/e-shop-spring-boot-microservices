package com.denmit.eshop.attachmentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentNameResponseDto {

    private String fileName;
}
