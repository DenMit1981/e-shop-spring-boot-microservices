package com.denmit.eshop.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "ordersIdSeq", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordersIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "product_id")
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @ElementCollection
    @ToString.Exclude
    private List<Long> productsIds = new ArrayList<>();
}
