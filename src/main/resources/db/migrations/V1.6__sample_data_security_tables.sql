INSERT INTO usr (username, password, enabled)
VALUES ('user1', '$2a$12$7eO87w.rFOeFRH73dgwBO.8vEL4vY7Mxwv4Ba.IcB.sgNKTJQroRW', TRUE),
       ('user2', '$2a$12$D38zBSFM9iApv3VzD7tul.cgH4uxfDw7SAHuwrNaHy9xIHbVLU4Au', TRUE),
       ('user3', '$2a$12$B1cupOr7FtcHnvPsFVhZYOf6sNHazXKQh7X1GRLn.jpYlXoBd.Gim', TRUE),
       ('user4', '$2a$12$e5854CM310OYZClxzEtBR.qVHSizgX/v1jEyk0HrBgpgafGGkICpC', TRUE);

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ROLE_STUDENT'),
       (2, 'ROLE_TEACHER'),
       (3, 'ROLE_ADMIN'),
       (4, 'ROLE_STUFF');