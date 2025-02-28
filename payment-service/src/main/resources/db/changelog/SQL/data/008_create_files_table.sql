create TABLE IF NOT EXISTS files (
    id BIGSERIAL   PRIMARY KEY NOT NULL,
    name           VARCHAR(255) NOT NULL,
    path_file      VARCHAR(255) NOT NULL,
    receipt_id     BIGINT NOT NULL,
    FOREIGN KEY (receipt_id) REFERENCES receipts(id)
);
