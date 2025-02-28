package com.denmit.eshop.goodsservice.mapper;

import com.denmit.eshop.goodsservice.dto.request.ProductCreationRequestDto;
import com.denmit.eshop.goodsservice.dto.response.*;
import com.denmit.eshop.goodsservice.model.Product;
import com.denmit.eshop.goodsservice.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductCreationRequestDto productDto);

    ProductResponseDto toProductDto(Product product);

    ProductUnitResponseDto toProductUnitDto(Product product);

    List<ProductUnitResponseDto> toProductUnitDtos(List<Product> products);

    @Mapping(target = "title", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getTitle() : null)")
    @Mapping(target = "price", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getPrice() : null)")
    @Mapping(target = "quantity", source = "entity.remainingQuantity")
    ProductBuyerResponseDto toProductBuyerDto(ProductCategory entity);

    List<ProductBuyerResponseDto> toProductBuyerDtos(List<ProductCategory> products);

    @Mapping(target = "title", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getTitle() : null)")
    @Mapping(target = "price", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getPrice() : null)")
    @Mapping(target = "description", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getDescription() : null)")
    @Mapping(target = "quantity", source = "entity.remainingQuantity")
    @Mapping(target = "totalPrice", source = "entity.remainingPrice")
    ProductAdminResponseDto toProductAdminDto(ProductCategory entity);

    List<ProductAdminResponseDto> toProductAdminDtos(List<ProductCategory> entities);

    @Mapping(target = "price", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getPrice() : null)")
    @Mapping(target = "unitIds", source = "entity", qualifiedByName = "mapUnitIds")
    @Mapping(target = "quantity", source = "entity.remainingQuantity")
    ChosenProductResponseDto toChosenProductDto(ProductCategory entity);

    @Mapping(target = "title", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getTitle() : null)")
    @Mapping(target = "price", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getGoods().get(0).getPrice() : null)")
    @Mapping(target = "quantity", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? (entity.getQuantity() - entity.getRemainingQuantity()) : null)")
    @Mapping(target = "totalPrice", expression = "java(entity.getGoods() != null && !entity.getGoods().isEmpty() ? entity.getTotalPrice().subtract(entity.getRemainingPrice()) : null)")
    CartProductResponseDto toCartProductDto(ProductCategory entity);

    @Named("mapUnitIds")
    default List<Long> mapUnitIds(ProductCategory entity) {
        return entity.getGoods().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}
