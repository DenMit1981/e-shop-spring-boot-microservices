package com.denmit.eshop.paymentservice.mapper;

import com.denmit.eshop.paymentservice.dto.response.MerchantBankTransferDetailsResponseDto;
import com.denmit.eshop.paymentservice.dto.response.MerchantCardPaymentDetailsResponseDto;
import com.denmit.eshop.paymentservice.model.MerchantPaymentDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiverMapper {

    MerchantCardPaymentDetailsResponseDto toCardPaymentDto(MerchantPaymentDetails entity);

    MerchantBankTransferDetailsResponseDto toBankTransferDto(MerchantPaymentDetails entity);
}
