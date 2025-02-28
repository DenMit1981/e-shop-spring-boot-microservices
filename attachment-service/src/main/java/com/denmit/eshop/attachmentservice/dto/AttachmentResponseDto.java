package com.denmit.eshop.attachmentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentResponseDto extends AttachmentNameResponseDto {

    private Long id;

    private String fileSize;

    private Long orderId;
}
