create TABLE IF NOT EXISTS payments (
    id BIGSERIAL   PRIMARY KEY NOT NULL,
    payment_status VARCHAR(255),
    payment_number VARCHAR(255),
    date           TIMESTAMP,
    amount         DECIMAL,
    payment_method VARCHAR(255),
    fields         JSONB,
    all_fields_filled BOOLEAN NOT NULL,
    order_id       BIGINT,
    user_id        BIGINT,
    receiver_id    BIGINT,
    FOREIGN KEY (receiver_id) REFERENCES receiver(id)
);