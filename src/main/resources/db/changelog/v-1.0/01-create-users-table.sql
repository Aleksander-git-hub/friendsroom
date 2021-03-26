CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    second_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    activation_code VARCHAR(255),
    created TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated TIMESTAMP DEFAULT current_timestamp NOT NULL,
    status VARCHAR(20) DEFAULT 'NOT_CONFIRMED' NOT NULL
);

OK