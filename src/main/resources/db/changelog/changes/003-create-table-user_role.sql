--liquibase formatted sql

--changeset alexkolo:003-create-table-user_role runAlways=false

CREATE TABLE IF NOT EXISTS user_role
(
    user_id    UUID          NOT NULL,
    role       VARCHAR(20)   NOT NULL,

    CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role),
    CONSTRAINT fkj345gk1bovqvfame88rcx7yyx FOREIGN KEY (user_id) REFERENCES users (id)
        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);