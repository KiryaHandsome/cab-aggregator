CREATE TABLE IF NOT EXISTS passenger_ratings
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    passenger_id INTEGER      NOT NULL,
    score        INTEGER      NOT NULL CHECK ( score >= 0 AND score <= 5 ),
    comment      VARCHAR(255) NULL
);