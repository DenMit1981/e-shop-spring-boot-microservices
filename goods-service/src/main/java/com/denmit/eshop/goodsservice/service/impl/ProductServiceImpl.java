package com.denmit.eshop.goodsservice.service.impl;

import com.denmit.eshop.goodsservice.dto.request.ProductCreationRequestDto;
import com.denmit.eshop.goodsservice.dto.response.*;
import com.denmit.eshop.goodsservice.exception.ProductNotFoundException;
import com.denmit.eshop.goodsservice.mapper.ProductMapper;
import com.denmit.eshop.goodsservice.model.Product;
import com.denmit.eshop.goodsservice.model.ProductCategory;
import com.denmit.eshop.goodsservice.repository.ProductCategoryRepository;
import com.denmit.eshop.goodsservice.repository.ProductRepository;
import com.denmit.eshop.goodsservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_NOT_FOUND_BY_ID = "Product with id %s not found";
    private static final String PRODUCT_NOT_FOUND_BY_TITLE_AND_PRICE = "Product with title %s and price %s $ not found";
    private static final String PRODUCT_IS_OVER = "Product with category id %s is out of stock";

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public ProductUnitResponseDto create(ProductCreationRequestDto productCreationRequestDto) {
        Product product = productMapper.toEntity(productCreationRequestDto);

        return productMapper.toProductUnitDto(productRepository.save(product));
    }

    @Override
    public List<ProductBuyerResponseDto> getAllForBuyer() {
        createProductCategories(productRepository.findAll());

        return productMapper.toProductBuyerDtos(productCategoryRepository.findAllWithGoodsInStock());
    }

    @Override
    public List<ProductUnitResponseDto> getAllUnitsForAdmin(String sortField, String sortDirection, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        List<Product> goods = productRepository.findAll(pageable).stream().toList();

        return productMapper.toProductUnitDtos(goods);
    }

    @Override
    public List<ProductAdminResponseDto> getAllForAdmin(String searchField, String parameter, String sortField,
                                                        String sortDirection, int pageSize, int pageNumber) {
        createProductCategories(productRepository.findAll());

        List<ProductCategory> categories = getAllFilteredCategories(searchField, parameter)
                .stream()
                .filter(product -> product.getRemainingQuantity() > 0)
                .toList();

        List<ProductAdminResponseDto> goods = productMapper.toProductAdminDtos(categories);

        return paginateGoods(sortGoods(goods, sortField, sortDirection), pageSize, pageNumber);
    }

    @Override
    public ProductUnitResponseDto getUnitById(Long productId) {
        return productMapper.toProductUnitDto(findById(productId));
    }

    @Override
    public ProductResponseDto getUnitForHistoryById(Long productId) {
        return productMapper.toProductDto(findById(productId));
    }

    @Override
    public ProductAdminResponseDto getById(Long productCategoryId) {
        createProductCategories(productRepository.findAll());

        return productMapper.toProductAdminDto(findCategoryById(productCategoryId));
    }

    @Override
    public ChosenProductResponseDto getByTitleAndPrice(String title, String price) {
        createProductCategories(productRepository.findAll());

        return productMapper.toChosenProductDto(findCategoryByTitleAndPrice(title, price));
    }

    @Override
    @Transactional
    public ProductUnitResponseDto update(Long productId, ProductCreationRequestDto productDto) {
        Product existingProduct = findById(productId);

        String oldTitle = existingProduct.getTitle();
        BigDecimal oldPrice = existingProduct.getPrice();

        Product updatedProduct = productMapper.toEntity(productDto);
        mapper.map(updatedProduct, existingProduct);

        productRepository.save(existingProduct);

        Optional.ofNullable(existingProduct.getCategory()).ifPresent(category -> {
            String newKey = createKey(existingProduct);
            String oldKey = createKey(oldTitle, oldPrice);

            if (!newKey.equals(oldKey)) {
                updateOldCategory(category, existingProduct, oldPrice);
                ProductCategory newCategory = getOrCreateCategory(existingProduct);
                updateNewCategory(newCategory, existingProduct);
            }
        });

        return productMapper.toProductUnitDto(existingProduct);
    }

    @Override
    @Transactional
    public void changeProductQuantityAndTotalPrice(Long productId, Long quantity, BigDecimal price) {
        createProductCategories(productRepository.findAll());

        Product product = findById(productId);
        ProductCategory productCategory = product.getCategory();

        productCategory.setRemainingQuantity(productCategory.getRemainingQuantity() + quantity);
        productCategory.setRemainingPrice(productCategory.getRemainingPrice().add(price));

        productCategoryRepository.save(productCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long productId) {
        Product product = findById(productId);

        ProductCategory category = product.getCategory();

        if (category != null) {
            updateOldCategory(category, product, product.getPrice());
        }

        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public void removeOrderedProduct(Long productId) {
        ProductCategory category = findCategoryByProductId(productId);

        category.setQuantity(category.getQuantity() - 1);
        category.setTotalPrice(category.getTotalPrice().subtract(findById(productId).getPrice()));

        productCategoryRepository.save(category);
    }

    @Override
    public Long getTotalAmount() {
        return productRepository.count();
    }

    @Override
    @Transactional
    public List<CartProductResponseDto> getProductCategoriesFromUnits(List<ProductUnitResponseDto> goods) {
        Set<ProductCategory> categories = new HashSet<>();

        goods.forEach(productUnit -> productCategoryRepository.findByGoodsTitleAndGoodsPrice(productUnit.getTitle(), productUnit.getPrice()).ifPresent(categories::add));

        return categories.stream()
                .map(productMapper::toCartProductDto)
                .collect(Collectors.toList());
    }

    @Transactional
    private void createProductCategories(List<Product> products) {
        Map<String, ProductCategory> categoryMap = new HashMap<>();

        for (Product product : products) {
            String key = createKey(product);

            ProductCategory category = categoryMap.computeIfAbsent(key, k -> getOrCreateCategory(product));

            if (!category.getGoods().contains(product)) {
                updateNewCategory(category, product);
            }
        }
    }

    private Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_ID, productId)));
    }

    private ProductCategory findCategoryById(Long productCategoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_ID, productCategoryId)));

        if (productCategory.getRemainingQuantity() <= 0) {
            throw new ProductNotFoundException(String.format(PRODUCT_IS_OVER, productCategoryId));
        }

        return productCategory;
    }

    private ProductCategory findCategoryByProductId(Long productId) {
        return productCategoryRepository.findByGoodsId(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_ID, productId)));
    }

    private ProductCategory findCategoryByTitleAndPrice(String title, String price) {
        BigDecimal parsedPrice = new BigDecimal(price);

        BigDecimal minPrice = parsedPrice.setScale(2, RoundingMode.DOWN);
        BigDecimal maxPrice = parsedPrice.setScale(2, RoundingMode.UP);

        return productCategoryRepository.findByGoodsTitleAndGoodsPriceRange(title, minPrice, maxPrice)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_TITLE_AND_PRICE, title, price)));
    }

    private List<ProductCategory> getAllFilteredCategories(String searchField, String parameter) {
        return switch (searchField) {
            case "id" -> productCategoryRepository.findAllByIdContaining(parameter);
            case "title" -> productCategoryRepository.findAllByGoodsTitleContainingIgnoreCase(parameter);
            case "price" -> productCategoryRepository.findAllByGoodsPriceContaining(parameter);
            case "description" -> productCategoryRepository.findAllByGoodsDescriptionContainingIgnoreCase(parameter);
            default -> productCategoryRepository.findAll();
        };
    }

    private List<ProductAdminResponseDto> sortGoods(List<ProductAdminResponseDto> goods, String sortField,
                                                    String sortDirection) {
        Comparator<ProductAdminResponseDto> comparator = switch (sortField) {
            case "title" -> Comparator.comparing(ProductAdminResponseDto::getTitle);
            case "price" -> Comparator.comparing(ProductAdminResponseDto::getPrice);
            case "description" -> Comparator.comparing(ProductAdminResponseDto::getDescription);
            default -> Comparator.comparing(ProductAdminResponseDto::getId);
        };

        return goods.stream()
                .sorted(sortDirection.equalsIgnoreCase("desc") ? comparator.reversed() : comparator)
                .toList();
    }

    private List<ProductAdminResponseDto> paginateGoods(List<ProductAdminResponseDto> goods, int pageSize, int pageNumber) {
        int totalItems = goods.size();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > totalItems) {
            return List.of();
        }

        return goods.subList(fromIndex, toIndex);
    }

    private String createKey(Product product) {
        return product.getTitle() + ":" + product.getPrice().stripTrailingZeros().toPlainString();
    }

    private String createKey(String title, BigDecimal price) {
        return title + ":" + price.stripTrailingZeros().toPlainString();
    }

    private void updateOldCategory(ProductCategory category, Product product, BigDecimal oldPrice) {
        category.removeProduct(product);
        category.setQuantity(category.getQuantity() - 1);
        category.setRemainingQuantity(category.getRemainingQuantity() - 1);
        category.setTotalPrice(category.getTotalPrice().subtract(oldPrice));
        category.setRemainingPrice(category.getRemainingPrice().subtract(oldPrice));

        if (category.getRemainingQuantity() > 0) {
            productCategoryRepository.save(category);
        } else {
            productCategoryRepository.deleteById(category.getId());
        }
    }

    private ProductCategory getOrCreateCategory(Product product) {
        return productCategoryRepository.findByGoodsTitleAndGoodsPrice(product.getTitle(), product.getPrice())
                .orElseGet(() -> {
                    ProductCategory categoryEntity = new ProductCategory();
                    categoryEntity.setQuantity(0L);
                    categoryEntity.setRemainingQuantity(0L);
                    categoryEntity.setTotalPrice(BigDecimal.ZERO);
                    categoryEntity.setRemainingPrice(BigDecimal.ZERO);
                    return categoryEntity;
                });
    }

    private void updateNewCategory(ProductCategory category, Product product) {
        category.setQuantity(category.getQuantity() + 1);
        category.setRemainingQuantity(category.getRemainingQuantity() + 1);
        category.setTotalPrice(category.getTotalPrice().add(product.getPrice()));
        category.setRemainingPrice(category.getRemainingPrice().add(product.getPrice()));
        category.addProduct(product);
        productCategoryRepository.save(category);
    }
}
