--liquibase formatted sql

--changeset alexkolo:001-create-table-cards runAlways=false

CREATE TABLE IF NOT EXISTS cards
(
    id                UUID           PRIMARY KEY,
    number            VARCHAR(255)    NOT NULL,
    owner_id          UUID           NOT NULL,
    validity_period   DATE           NOT NULL,
    status            VARCHAR(20)    NOT NULL,
    balance           NUMERIC(12, 2) NOT NULL,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);