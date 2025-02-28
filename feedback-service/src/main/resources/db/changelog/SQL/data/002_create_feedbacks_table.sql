create TABLE IF NOT EXISTS feedbacks (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    rate VARCHAR(255),
    date TIMESTAMP,
    text VARCHAR(255),
    order_id BIGINT,
    user_id BIGINT
);
