CREATE TABLE debts(
    id BIGSERIAL PRIMARY KEY,
    sum DOUBLE PRECISION,
    created TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated TIMESTAMP DEFAULT current_timestamp NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    room_id BIGINT REFERENCES rooms(id) ON DELETE RESTRICT,
    who_owes_money_id BIGINT REFERENCES users(id) ON DELETE RESTRICT
);

OK