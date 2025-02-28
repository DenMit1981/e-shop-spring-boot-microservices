create TABLE IF NOT EXISTS carts (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    user_id    BIGINT NOT NULL,
    total_price    DECIMAL,
    quantity    BIGINT
);
