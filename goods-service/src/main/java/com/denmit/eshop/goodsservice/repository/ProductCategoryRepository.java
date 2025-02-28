package com.denmit.eshop.goodsservice.repository;

import com.denmit.eshop.goodsservice.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByGoodsId(Long id);

    Optional<ProductCategory> findByGoodsTitleAndGoodsPrice(String title, BigDecimal price);

    @Query("SELECT DISTINCT pc FROM ProductCategory pc JOIN pc.goods g WHERE g.title = :title AND g.price >= :minPrice AND g.price <= :maxPrice")
    Optional<ProductCategory> findByGoodsTitleAndGoodsPriceRange(@Param("title") String title, @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT pc FROM ProductCategory pc WHERE str(pc.id) LIKE CONCAT('%', :id, '%')")
    List<ProductCategory> findAllByIdContaining(String id);

    List<ProductCategory> findAllByGoodsTitleContainingIgnoreCase(String title);

    @Query("SELECT DISTINCT pc FROM ProductCategory pc JOIN pc.goods g WHERE CAST(g.price AS string) LIKE CONCAT(:price, '%')")
    List<ProductCategory> findAllByGoodsPriceContaining(@Param("price") String price);

    List<ProductCategory> findAllByGoodsDescriptionContainingIgnoreCase(String description);

    @Query("SELECT pc FROM ProductCategory pc JOIN pc.goods g WHERE pc.remainingQuantity > 0 ORDER BY g.title ASC")
    List<ProductCategory> findAllWithGoodsInStock();
}
