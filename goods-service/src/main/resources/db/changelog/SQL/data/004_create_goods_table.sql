create TABLE IF NOT EXISTS goods (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    title    VARCHAR(255) NOT NULL,
    price    DECIMAL NOT NULL,
    description    VARCHAR(255),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES goods_categories(id)
);

ALTER TABLE goods ADD CONSTRAINT category_id_fkey FOREIGN KEY (category_id) REFERENCES goods_categories(id) ON DELETE SET NULL;
