create TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    role    VARCHAR(255) NOT NULL
);
