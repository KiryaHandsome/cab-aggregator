CREATE TABLE IF NOT EXISTS driver_ratings
(
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    driver_id INTEGER      NOT NULL,
    score     INTEGER      NOT NULL CHECK ( score >= 0 AND score <= 5 ),
    comment   VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS passenger_ratings
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    passenger_id INTEGER      NOT NULL,
    score        INTEGER      NOT NULL CHECK ( score >= 0 AND score <= 5 ),
    comment      VARCHAR(255) NULL
);

INSERT INTO driver_ratings(driver_id, score, comment)
VALUES (1, 5, 'Good driver'),
       (1, 1, 'Smokes in car'),
       (2, 5, null);

INSERT INTO passenger_ratings(passenger_id, score, comment)
VALUES (1, 5, 'Good passenger'),
       (1, 1, 'Smokes in car'),
       (2, 5, null);