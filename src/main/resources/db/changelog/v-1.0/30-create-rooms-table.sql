CREATE TABLE rooms(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated TIMESTAMP DEFAULT current_timestamp NOT NULL,
    status VARCHAR(20) DEFAULT 'NOT_CONFIRMED' NOT NULL,
    activation_code VARCHAR(255),
    owner_id BIGINT REFERENCES users(id) ON DELETE RESTRICT
);

OK