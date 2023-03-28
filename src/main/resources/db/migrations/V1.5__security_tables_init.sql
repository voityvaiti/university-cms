CREATE TABLE usr
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE users_roles
(
    id      SERIAL PRIMARY KEY,
    user_id INT,
    role    VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES usr (id)
)