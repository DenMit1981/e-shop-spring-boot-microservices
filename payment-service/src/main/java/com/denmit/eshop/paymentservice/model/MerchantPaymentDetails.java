package com.denmit.eshop.paymentservice.model;

import com.denmit.eshop.paymentservice.model.enums.CardBrand;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receiver")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class MerchantPaymentDetails {

    @Id
    @SequenceGenerator(name = "receiverIdSeq", sequenceName = "receiver_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiverIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_brand")
    @Enumerated(EnumType.STRING)
    private CardBrand cardBrand;

    @Column(name = "bank_account")
    private String bankAccount;
}
