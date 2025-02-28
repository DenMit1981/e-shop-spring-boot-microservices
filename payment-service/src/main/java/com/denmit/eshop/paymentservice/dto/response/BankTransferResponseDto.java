package com.denmit.eshop.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTransferResponseDto extends PaymentResponseDto {

    private MerchantBankTransferDetailsResponseDto receiver;

    private String senderBankAccount;
}
