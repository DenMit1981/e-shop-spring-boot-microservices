package com.denmit.eshop.paymentservice.mapper;

import com.denmit.eshop.paymentservice.dto.response.PaymentResponseDto;
import com.denmit.eshop.paymentservice.dto.response.ReceiptResponseDto;
import com.denmit.eshop.paymentservice.model.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptResponseDto toDto(Receipt receipt, PaymentResponseDto paymentInfo, String filePath);
}
