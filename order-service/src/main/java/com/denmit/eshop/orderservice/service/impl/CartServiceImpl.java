package com.denmit.eshop.orderservice.service.impl;

import com.denmit.eshop.orderservice.client.HistoryClient;
import com.denmit.eshop.orderservice.client.ProductClient;
import com.denmit.eshop.orderservice.dto.request.ProductRequestDto;
import com.denmit.eshop.orderservice.dto.response.CartResponseDto;
import com.denmit.eshop.orderservice.dto.response.ChosenProductResponseDto;
import com.denmit.eshop.orderservice.dto.response.ProductUnitResponseDto;
import com.denmit.eshop.orderservice.exception.ProductNotFoundException;
import com.denmit.eshop.orderservice.exception.ProductNotSelectedException;
import com.denmit.eshop.orderservice.model.Cart;
import com.denmit.eshop.orderservice.repository.CartRepository;
import com.denmit.eshop.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private static final String PRODUCT_NOT_SELECTED = "You should select the product first";
    private static final String PRODUCT_NOT_FOUND = "Product with title %s and price %s $ is not in the cart";
    private static final String PRODUCT_IS_OVER = "Product with title %s and price %s $ out of stock";

    private final ProductClient productClient;
    private final HistoryClient historyClient;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CartResponseDto addProductToCart(ProductRequestDto productDto, Long userId) {
        return modifyCart(productDto, userId, true);
    }

    @Override
    @Transactional
    public CartResponseDto removeProductFromCart(ProductRequestDto productDto, Long userId) {
        return modifyCart(productDto, userId, false);
    }

    @Override
    @Transactional
    public void clearCartForCancelledOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> new Cart(userId));

        getCartGoods(cart).forEach(product ->
                productClient.changeProductQuantityAndTotalPrice(product.getId(), 1L, product.getPrice()));

        cart.setProductsIds(new ArrayList<>());

        historyClient.saveHistoryForCanceledOrder();

        cartRepository.save(cart);
    }

    private CartResponseDto modifyCart(ProductRequestDto productDto, Long userId, boolean isAdding) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> new Cart(userId));
        String title = productDto.getTitle();
        BigDecimal price = productDto.getPrice();

        if (title == null || price == null || title.isBlank()) {
            throw new ProductNotSelectedException(PRODUCT_NOT_SELECTED);
        }

        ChosenProductResponseDto product = productClient.getByTitleAndPrice(title, String.valueOf(price));

        if (isAdding) {
            if (product.getQuantity() < 1) {
                throw new ProductNotFoundException(String.format(PRODUCT_IS_OVER, title, price));
            }
        } else {
            if (!isProductPresent(cart, title, price)) {
                throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, title, price));
            }
        }

        Long productId = product.getUnitIds().get(Math.toIntExact(isAdding ? product.getQuantity() - 1 : product.getQuantity()));

        if (isAdding) {
            cart.addProductId(productId);
            productClient.changeProductQuantityAndTotalPrice(productId, -1L, product.getPrice().multiply(BigDecimal.valueOf(-1)));
            historyClient.saveHistoryForAddedProduct(productId);
        } else {
            cart.removeProductId(productId);
            productClient.changeProductQuantityAndTotalPrice(productId, 1L, product.getPrice());
            historyClient.saveHistoryForRemovedProduct(productId);
        }

        return getCartResponseDtoFromEntity(cartRepository.save(cart));
    }

    private CartResponseDto getCartResponseDtoFromEntity(Cart cart) {
        CartResponseDto cartDto = new CartResponseDto();

        List<ProductUnitResponseDto> goods = cart.getProductsIds().stream()
                .map(productClient::getUnitById)
                .toList();

        cartDto.setGoods(productClient.getProductCategoriesFromUnits(goods));

        return cartDto;
    }

    private List<ProductUnitResponseDto> getCartGoods(Cart cart) {
        return cart.getProductsIds()
                .stream()
                .map(productClient::getUnitById)
                .toList();
    }

    private boolean isProductPresent(Cart cart, String title, BigDecimal price) {
        BigDecimal roundedPrice = price.setScale(2, RoundingMode.HALF_UP);

        return cart.getProductsIds().stream()
                .map(productClient::getUnitById)
                .anyMatch(product -> title.equals(product.getTitle()) && product.getPrice().compareTo(roundedPrice) == 0);
    }
}
