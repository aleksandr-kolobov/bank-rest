--liquibase formatted sql

--changeset alexkolo:002-create-table-users runAlways=false

CREATE TABLE IF NOT EXISTS users
(
    id               UUID            PRIMARY KEY,
    email            VARCHAR(255)    NOT NULL,
    firstname        VARCHAR(255)    NOT NULL,
    lastname         VARCHAR(255)    NOT NULL,
    middlename       VARCHAR(255),
    password_hash    VARCHAR(255)    NOT NULL,

    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
);