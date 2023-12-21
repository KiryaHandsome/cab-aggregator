CREATE TABLE IF NOT EXISTS drivers
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    status       VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ratings
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    total_ratings  INTEGER       NOT NULL,
    average_rating DECIMAL(3, 2) NOT NULL,
    driver_id      INTEGER REFERENCES drivers (id) ON DELETE CASCADE
);

INSERT INTO drivers (name, surname, email, phone_number, status)
VALUES ('John', 'Doe', 'john.doe@example.com', '+375447775566', '0'),
       ('Jane', 'Smith', 'jane.smith@example.com', '+375297775566', '1'),
       ('Alice', 'Johnson', 'alice.johnson@example.com', '+375441235566', '2');

INSERT INTO ratings(total_ratings, average_rating, driver_id)
VALUES (5, 4.0, 1),
       (1, 1.2, 2),
       (6, 2.3, 3);