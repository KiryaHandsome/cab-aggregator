CREATE TABLE IF NOT EXISTS passengers
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name"       VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(30)  NOT NULL UNIQUE,
    CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\.[A-Za-z]{2,}$')
);