package com.denmit.eshop.paymentservice.dto.request;

import com.denmit.eshop.paymentservice.model.enums.CardBrand;
import com.denmit.eshop.paymentservice.utils.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
public class CardPaymentRequestDto extends PaymentRequestDto {

    @Pattern(regexp = "^\\d{16}$", message = "Incorrect card number entered")
    private String cardNumber;

    private CardBrand cardBrand;

    private String cardHolderName;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate expiryDate;

    @Pattern(regexp = "^[0-9]{3}$", message = "Incorrect cvc entered")
    private String cvc;
}
