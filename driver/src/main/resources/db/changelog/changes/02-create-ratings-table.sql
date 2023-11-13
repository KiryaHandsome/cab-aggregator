CREATE TABLE IF NOT EXISTS ratings
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    total_ratings  INTEGER       NOT NULL,
    average_rating DECIMAL(3, 2) NOT NULL,
    driver_id      INTEGER REFERENCES drivers (id) ON DELETE CASCADE
);