package com.denmit.eshop.goodsservice.controller;

import com.denmit.eshop.goodsservice.dto.request.ProductCreationRequestDto;
import com.denmit.eshop.goodsservice.dto.response.*;
import com.denmit.eshop.goodsservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/goods")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Product controller")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product by admin")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductUnitResponseDto create(@RequestBody @Valid ProductCreationRequestDto productCreationRequestDto) {
        return productService.create(productCreationRequestDto);
    }

    @GetMapping("/units")
    @Operation(summary = "Get all product units for admin")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductUnitResponseDto> getAllUnitsForAdmin(@RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                                            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                                            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize,
                                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        return productService.getAllUnitsForAdmin(sortField, sortDirection, pageSize, pageNumber);
    }

    @GetMapping
    @Operation(summary = "Get all goods for admin")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductAdminResponseDto> getAllForAdmin(@RequestParam(value = "searchField", defaultValue = "default") String searchField,
                                                        @RequestParam(value = "parameter", defaultValue = "") String parameter,
                                                        @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                                        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                                        @RequestParam(value = "pageSize", defaultValue = "25") int pageSize,
                                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        return productService.getAllForAdmin(searchField, parameter, sortField, sortDirection, pageSize, pageNumber);
    }

    @GetMapping("/for-buyer")
    @Operation(summary = "Get all goods for buyer")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductBuyerResponseDto> getAllForBuyer() {
        return productService.getAllForBuyer();
    }

    @PostMapping("/chosen/in-cart")
    @Operation(summary = "Get product categories from chosen products in cart for buyer")
    @ResponseStatus(HttpStatus.OK)
    public List<CartProductResponseDto> getProductCategoriesFromUnits(@RequestBody List<ProductUnitResponseDto> goods) {
        return productService.getProductCategoriesFromUnits(goods);
    }

    @GetMapping("/units/{productId}")
    @Operation(summary = "Get product unit by ID")
    @ResponseStatus(HttpStatus.OK)
    public ProductUnitResponseDto getUnitById(@PathVariable("productId") Long productId) {
        return productService.getUnitById(productId);
    }

    @GetMapping("/units/{productId}/history")
    @Operation(summary = "Get product unit for history by ID")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getUnitForHistoryById(@PathVariable("productId") Long productId) {
        return productService.getUnitForHistoryById(productId);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get product by category ID for admin")
    @ResponseStatus(HttpStatus.OK)
    public ProductAdminResponseDto getById(@PathVariable("categoryId") Long categoryId) {
        return productService.getById(categoryId);
    }

    @GetMapping("/chosen")
    @Operation(summary = "Get product by title and price for buyer")
    @ResponseStatus(HttpStatus.OK)
    public ChosenProductResponseDto getByTitleAndPrice(@RequestParam(value = "title") String title,
                                                       @RequestParam(value = "price") String price) {
        return productService.getByTitleAndPrice(title, price);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product by admin")
    @ResponseStatus(HttpStatus.OK)
    public ProductUnitResponseDto update(@PathVariable("productId") Long productId,
                                         @RequestBody @Valid ProductCreationRequestDto productDto) {
        return productService.update(productId, productDto);
    }

    @PutMapping("/for-buyer/{productId}/change")
    @Operation(summary = "Change product quantity and total price by buyer after chosen product")
    @ResponseStatus(HttpStatus.OK)
    public void changeProductQuantityAndTotalPrice(@PathVariable("productId") Long productId,
                                                   @RequestParam Long quantity,
                                                   @RequestParam BigDecimal price) {
        productService.changeProductQuantityAndTotalPrice(productId, quantity, price);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product by admin")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("productId") Long productId) {
        productService.deleteById(productId);
    }

    @DeleteMapping("/for-buyer/{productId}")
    @Operation(summary = "Delete product after committed order")
    @ResponseStatus(HttpStatus.OK)
    public void removeOrderedProduct(@PathVariable("productId") Long productId) {
        productService.removeOrderedProduct(productId);
    }

    @GetMapping("/total")
    @Operation(summary = "Get total amount of goods")
    @ResponseStatus(HttpStatus.OK)
    public Long getTotalAmount() {
        return productService.getTotalAmount();
    }
}
