CREATE TABLE user_rooms(
    user_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    room_id BIGINT REFERENCES rooms(id) ON DELETE RESTRICT
);

OK