create TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    total_price    DECIMAL NOT NULL,
    description    VARCHAR(255),
    user_id    BIGINT NOT NULL
);

