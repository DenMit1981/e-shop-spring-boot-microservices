package com.denmit.eshop.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receipts")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Receipt {

    @Id
    @SequenceGenerator(name = "receiptsIdSeq", sequenceName = "receipt_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiptsIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;

    @OneToOne(mappedBy = "receipt")
    private File file;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
