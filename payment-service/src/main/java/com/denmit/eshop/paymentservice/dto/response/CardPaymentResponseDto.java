package com.denmit.eshop.paymentservice.dto.response;

import com.denmit.eshop.paymentservice.model.enums.CardBrand;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardPaymentResponseDto extends PaymentResponseDto {

    private MerchantCardPaymentDetailsResponseDto receiver;

    private String cardNumber;

    private CardBrand cardBrand;

    private String cardHolderName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    private LocalDate expiryDate;

    private String cvc;
}
