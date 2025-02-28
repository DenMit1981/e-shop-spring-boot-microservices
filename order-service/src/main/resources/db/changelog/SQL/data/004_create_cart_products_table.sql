CREATE TABLE cart_products (
    cart_id BIGINT REFERENCES carts(id),
    product_id BIGINT,
    PRIMARY KEY (cart_id, product_id)
);