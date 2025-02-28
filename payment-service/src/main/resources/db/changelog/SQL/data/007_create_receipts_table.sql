create TABLE IF NOT EXISTS receipts (
    id BIGSERIAL   PRIMARY KEY NOT NULL,
    order_id       BIGINT,
    user_id        BIGINT,
    payment_id     BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payments(id)
);

