create table tbl_attempt_details
(
    attempt_id         int auto_increment
        primary key,
    session_id         int               not null,
    problem_id         int               not null,
    selected_choice_id int               null,
    is_correct         tinyint default 0 not null
);

create table tbl_attempt_sessions
(
    session_id  int auto_increment
        primary key,
    user_id     int                                 not null,
    started_at  timestamp default CURRENT_TIMESTAMP null,
    finished_at timestamp                           null,
    total_score int                                 null,
    difficulty  varchar(10)                         not null,
    times       int                                 null
);

create table tbl_class_settings
(
    id        int auto_increment
        primary key,
    class_num int not null
);

create table tbl_comments
(
    comment_id int auto_increment
        primary key,
    post_id    int                                 not null,
    user_id    int                                 not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP null
);

create table tbl_users
(
    user_id    int auto_increment
        primary key,
    username   varchar(50)                           not null,
    name       varchar(50)                           not null,
    password   varchar(255)                          not null,
    email      varchar(100)                          not null,
    role       varchar(50) default 'ROLE_STUDENT'    null,
    created_at timestamp   default CURRENT_TIMESTAMP null,
    constraint UKc190nfu2w5xwvexf9dv08grsq
        unique (username),
    constraint UKj562wwmipqt96rkoqbo0jc34
        unique (email)
);


INSERT INTO sql_bank.tbl_users (user_id, username, name, password, email, role, created_at, class_num) VALUES (1, '1', '관리자', '$2a$10$aGoWkK3b40tmVIgNQqbX6.i5X6NyJVw45FiEsxyjmzHBzLJ8qXPvS', '1', 'ROLE_ADMIN', '2025-02-26 16:20:00', null);
INSERT INTO sql_bank.tbl_users (user_id, username, name, password, email, role, created_at, class_num) VALUES (2, '2', '안혜빈', '$2a$10$aGoWkK3b40tmVIgNQqbX6.i5X6NyJVw45FiEsxyjmzHBzLJ8qXPvS', '2', 'ROLE_ADMIN', '1998-01-15 23:59:59', null);
INSERT INTO sql_bank.tbl_users (user_id, username, name, password, email, role, created_at, class_num) VALUES (3, '3', '한상인', '$2a$10$aGoWkK3b40tmVIgNQqbX6.i5X6NyJVw45FiEsxyjmzHBzLJ8qXPvS', '3', 'ROLE_ADMIN', '1999-04-01 00:00:00', null);

create table tbl_posts
(
    post_id    int auto_increment
        primary key,
    user_id    int                                 not null,
    title      varchar(255)                        not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    category   varchar(1)                          null
);

create table tbl_problem_choices
(
    choice_id   int auto_increment
        primary key,
    problem_id  int     not null,
    choice_text text    not null,
    is_correct  tinyint not null
);

create table tbl_problem_discussions
(
    discussion_id int auto_increment
        primary key,
    problem_id    int                                 not null,
    user_id       int                                 not null,
    content       text                                not null,
    created_at    timestamp default CURRENT_TIMESTAMP null
);

create table tbl_problems
(
    problem_id  int auto_increment
        primary key,
    question    varchar(255)                        not null,
    description text                                not null,
    difficulty  varchar(50)                         not null,
    created_by  int                                 null,
    created_at  timestamp default CURRENT_TIMESTAMP null,
    permit_yn   char      default 'Y'               null
);