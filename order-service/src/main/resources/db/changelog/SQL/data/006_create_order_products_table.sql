CREATE TABLE order_products (
    order_id BIGINT REFERENCES orders(id),
    product_id BIGINT,
    PRIMARY KEY (order_id, product_id)
);