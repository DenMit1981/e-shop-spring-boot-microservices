package com.denmit.eshop.goodsservice.model;

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
@Table(name = "goods_categories")
public class ProductCategory {

    @Id
    @SequenceGenerator(name = "goodsCategoriesIdSeq", sequenceName = "goods_category_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goodsCategoriesIdSeq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "remaining_quantity")
    private Long remainingQuantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "remaining_price")
    private BigDecimal remainingPrice;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> goods = new ArrayList<>();

    public void addProduct(Product product) {
        if (this.goods == null) {
            goods = new ArrayList<>();
        }
        this.goods.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product) {
        this.goods.remove(product);
        product.setCategory(null);
    }
}
