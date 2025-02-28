package com.denmit.eshop.orderservice.client;

import com.denmit.eshop.orderservice.dto.response.CartProductResponseDto;
import com.denmit.eshop.orderservice.dto.response.ChosenProductResponseDto;
import com.denmit.eshop.orderservice.dto.response.ProductUnitResponseDto;
import com.denmit.eshop.orderservice.security.SecuredFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "goods-service", path = "/api/v1/goods")
@SecuredFeignClient
public interface ProductClient {

    @GetMapping("/units/{productId}")
    ProductUnitResponseDto getUnitById(@PathVariable("productId") Long productId);

    @GetMapping("/chosen")
    ChosenProductResponseDto getByTitleAndPrice(@RequestParam(value = "title") String title,
                                                @RequestParam(value = "price") String price);

    @PostMapping("/chosen/in-cart")
    List<CartProductResponseDto> getProductCategoriesFromUnits(@RequestBody List<ProductUnitResponseDto> goods);

    @PutMapping("/for-buyer/{productId}/change")
    void changeProductQuantityAndTotalPrice(@PathVariable("productId") Long productId, @RequestParam Long quantity,
                                            @RequestParam BigDecimal price);

    @DeleteMapping("/for-buyer/{productId}")
    void removeOrderedProduct(@PathVariable("productId") Long productId);
}
