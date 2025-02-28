package com.denmit.eshop.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "carts")
public class Cart {

    @Id
    @SequenceGenerator(name = "cartsIdSeq", sequenceName = "cart_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartsIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "cart_id"))
    @ElementCollection
    private List<Long> productsIds = new ArrayList<>();

    public Cart(Long userId) {
        this.userId = userId;
    }

    public void addProductId(Long productId) {
        this.productsIds.add(productId);
    }

    public void removeProductId(Long productId) {
        this.productsIds.remove(productId);
    }
}
