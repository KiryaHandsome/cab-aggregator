CREATE TABLE IF NOT EXISTS ratings
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    total_ratings  INTEGER,
    average_rating DECIMAL(3, 2),
    passenger_id   INTEGER REFERENCES passengers (id) ON DELETE CASCADE
);