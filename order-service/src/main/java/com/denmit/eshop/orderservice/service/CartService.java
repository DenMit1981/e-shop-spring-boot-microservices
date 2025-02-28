package com.denmit.eshop.orderservice.service;

import com.denmit.eshop.orderservice.dto.request.ProductRequestDto;
import com.denmit.eshop.orderservice.dto.response.CartResponseDto;

public interface CartService {

    CartResponseDto addProductToCart(ProductRequestDto productDto, Long userId);

    CartResponseDto removeProductFromCart(ProductRequestDto productDto, Long userId);

    void clearCartForCancelledOrder(Long userId);
}
