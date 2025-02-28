package com.denmit.eshop.paymentservice.dto.response;

import com.denmit.eshop.paymentservice.model.enums.CardBrand;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantCardPaymentDetailsResponseDto extends ReceiverResponseDto {

    private String cardNumber;

    private CardBrand cardBrand;
}
