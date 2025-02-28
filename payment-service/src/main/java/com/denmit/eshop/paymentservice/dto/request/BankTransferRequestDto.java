package com.denmit.eshop.paymentservice.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
public class BankTransferRequestDto extends PaymentRequestDto {

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Incorrect account number entered")
    private String senderBankAccount;
}
