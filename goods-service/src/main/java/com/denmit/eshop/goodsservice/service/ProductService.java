package com.denmit.eshop.goodsservice.service;

import com.denmit.eshop.goodsservice.dto.request.ProductCreationRequestDto;
import com.denmit.eshop.goodsservice.dto.response.*;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductUnitResponseDto create(ProductCreationRequestDto productCreationRequestDto);

    List<ProductBuyerResponseDto> getAllForBuyer();

    List<ProductUnitResponseDto> getAllUnitsForAdmin(String sortField, String sortDirection, int pageSize, int pageNumber);

    List<ProductAdminResponseDto> getAllForAdmin(String searchField, String parameter, String sortField,
                                                 String sortDirection, int pageSize, int pageNumber);

    ProductResponseDto getUnitForHistoryById(Long productId);

    ProductAdminResponseDto getById(Long productId);

    ProductUnitResponseDto getUnitById(Long productId);

    ChosenProductResponseDto getByTitleAndPrice(String title, String price);

    ProductUnitResponseDto update(Long id, ProductCreationRequestDto productDto);

    void changeProductQuantityAndTotalPrice(Long productId, Long quantity, BigDecimal price);

    void deleteById(Long productId);

    void removeOrderedProduct(Long productId);

    Long getTotalAmount();

    List<CartProductResponseDto> getProductCategoriesFromUnits(List<ProductUnitResponseDto> goods);
}
