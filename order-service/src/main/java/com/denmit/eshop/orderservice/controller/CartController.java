package com.denmit.eshop.orderservice.controller;

import com.denmit.eshop.orderservice.dto.request.ProductRequestDto;
import com.denmit.eshop.orderservice.dto.response.CartResponseDto;
import com.denmit.eshop.orderservice.security.provider.UserProvider;
import com.denmit.eshop.orderservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Cart controller")
public class CartController {

    private final CartService cartService;
    private final UserProvider userProvider;

    @PutMapping("/add-product")
    @Operation(summary = "Add product to cart")
    @ResponseStatus(HttpStatus.OK)
    public CartResponseDto addProductToCart(@RequestBody ProductRequestDto productDto) {
        return cartService.addProductToCart(productDto, Long.valueOf(userProvider.getUserId()));
    }

    @PutMapping("/remove-product")
    @Operation(summary = "Remove product from cart")
    @ResponseStatus(HttpStatus.OK)
    public CartResponseDto removeProductFromCart(@RequestBody ProductRequestDto productDto) {
        return cartService.removeProductFromCart(productDto, Long.valueOf(userProvider.getUserId()));
    }

    @PutMapping("/clear")
    @Operation(summary = "Clear cart for cancelled order")
    @ResponseStatus(HttpStatus.OK)
    public void clearCartForCancelledOrder() {
        cartService.clearCartForCancelledOrder(Long.valueOf(userProvider.getUserId()));
    }
}
