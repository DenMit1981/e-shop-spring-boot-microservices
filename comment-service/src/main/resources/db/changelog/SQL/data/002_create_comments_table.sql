create TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    date TIMESTAMP,
    text VARCHAR(255),
    order_id BIGINT,
    user_id BIGINT
);
