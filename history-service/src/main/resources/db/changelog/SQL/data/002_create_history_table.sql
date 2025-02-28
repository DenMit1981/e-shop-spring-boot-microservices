create TABLE IF NOT EXISTS history (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    date TIMESTAMP,
    action VARCHAR(255),
    description VARCHAR(255),
    order_id BIGINT,
    user_id BIGINT,
    status VARCHAR(255)
);
