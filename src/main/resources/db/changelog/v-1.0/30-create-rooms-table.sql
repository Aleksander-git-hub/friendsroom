CREATE TABLE rooms(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated TIMESTAMP DEFAULT current_timestamp NOT NULL,
    owner_id BIGINT REFERENCES users(id) ON DELETE RESTRICT
);

OK