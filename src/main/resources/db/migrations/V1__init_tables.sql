CREATE TABLE IF NOT EXISTS people
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name  VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS groups
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(20),
    speciality VARCHAR(50),
    year       INT
);

CREATE TABLE IF NOT EXISTS students
(
    id       SERIAL PRIMARY KEY REFERENCES people,
    group_id INT,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS teachers
(
    id     SERIAL PRIMARY KEY REFERENCES people,
    degree VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS courses
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS lessons
(
    id          SERIAL PRIMARY KEY,
    place       VARCHAR(120),
    course_id   INT,
    teacher_id  INT,
    schedule_id INT,
    number      INT
);

CREATE TABLE IF NOT EXISTS schedules_for_days
(
    id  SERIAL PRIMARY KEY,
    day DATE
);

CREATE TABLE IF NOT EXISTS groups_courses
(
    id         SERIAL PRIMARY KEY,
    group_id INT,
    course_id  INT,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teachers_courses
(
    id         SERIAL PRIMARY KEY,
    teacher_id INT,
    course_id  INT,
    FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS groups_lessons
(
    id        SERIAL PRIMARY KEY,
    group_id  INT,
    lesson_id INT,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON UPDATE CASCADE ON DELETE CASCADE
);

