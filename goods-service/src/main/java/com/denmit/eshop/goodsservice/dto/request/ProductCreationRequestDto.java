package com.denmit.eshop.goodsservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
public class ProductCreationRequestDto {

    private static final String TITLE_FIELD_IS_EMPTY = "Title field shouldn't be empty";
    private static final String PRICE_FIELD_IS_EMPTY = "Price field shouldn't be empty";
    private static final String QUANTITY_FIELD_IS_EMPTY = "Quantity field shouldn't be empty";
    private static final String WRONG_SIZE_OF_TITLE = "Title shouldn't be more than 15 symbols";
    private static final String WRONG_SIZE_OF_DESCRIPTION = "Description shouldn't be more than 100 symbols";
    private static final String WRONG_TITLE = "Title should be in latin letters";
    private static final String WRONG_PRICE_VALUE = "The value of price should be at least 1";
    private static final String WRONG_QUANTITY_VALUE = "The value of quantity should be at least 1";

    @NotEmpty(message = TITLE_FIELD_IS_EMPTY)
    @Size(max = 15, message = WRONG_SIZE_OF_TITLE)
    @Pattern(regexp = "^[A-za-z ]*$", message = WRONG_TITLE)
    private String title;

    @NotNull(message = PRICE_FIELD_IS_EMPTY)
    @DecimalMin(value = "1.00", message = WRONG_PRICE_VALUE)
    private BigDecimal price;

    @Size(max = 100, message = WRONG_SIZE_OF_DESCRIPTION)
    private String description;
}
