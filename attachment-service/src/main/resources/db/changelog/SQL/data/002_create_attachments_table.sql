create TABLE IF NOT EXISTS attachments (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    order_id BIGINT
);
