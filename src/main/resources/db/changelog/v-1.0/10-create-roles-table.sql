CREATE TABLE roles(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    created TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated TIMESTAMP DEFAULT current_timestamp NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL
);

OK