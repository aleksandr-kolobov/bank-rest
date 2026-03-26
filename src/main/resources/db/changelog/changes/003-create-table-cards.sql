--liquibase formatted sql

--changeset alexkolo:003-create-table-cards runAlways=false

CREATE TABLE IF NOT EXISTS cards
(
    id                      UUID           PRIMARY KEY,
    user_id                 UUID           NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    encrypted_card_number   TEXT           NOT NULL UNIQUE,
    masked_card_number      VARCHAR(19)    NOT NULL,
    cardholder_name         VARCHAR(100)   NOT NULL,
    expiry_date             DATE           NOT NULL,
    status                  VARCHAR(20)    NOT NULL,
    balance                 DECIMAL(19,2)  NOT NULL DEFAULT 0,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE INDEX idx_cards_user_id ON cards(user_id);
CREATE INDEX idx_cards_status ON cards(status);
CREATE INDEX idx_cards_masked_number ON cards(masked_card_number);
