create TABLE IF NOT EXISTS goods_categories (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    total_price         DECIMAL,
    remaining_price     DECIMAL,
    quantity            BIGINT,
    remaining_quantity  BIGINT
);