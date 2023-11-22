CREATE TABLE IF NOT EXISTS passengers
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name"       VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(30)  NOT NULL UNIQUE,
    CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\.[A-Za-z]{2,}$')
);

CREATE TABLE IF NOT EXISTS ratings
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    total_ratings  INTEGER,
    average_rating DECIMAL(3, 2),
    passenger_id   INTEGER REFERENCES passengers (id) ON DELETE CASCADE
);

INSERT INTO passengers ("name", surname, email, phone_number)
VALUES ('John', 'Doe', 'john.doe@example.com', '+123456789');
INSERT INTO passengers ("name", surname, email, phone_number)
VALUES ('Alice', 'Smith', 'alice.smith@example.com', '+987654321');
INSERT INTO passengers ("name", surname, email, phone_number)
VALUES ('Bob', 'Johnson', 'bob.johnson@example.com', '+111223344');
