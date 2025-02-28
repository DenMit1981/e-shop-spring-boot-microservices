create TABLE IF NOT EXISTS receiver (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name         VARCHAR(255),
    card_number  VARCHAR(255),
    card_brand   VARCHAR(255),
    bank_account VARCHAR(255)
);
