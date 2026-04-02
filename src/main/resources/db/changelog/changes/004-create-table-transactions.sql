--liquibase formatted sql

--changeset alexkolo:004-create-table-transactions runAlways=false

CREATE TABLE IF NOT EXISTS transactions (
    id                UUID           PRIMARY KEY,
    user_id           UUID           NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    from_card_id      UUID           NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    to_card_id        UUID           NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    amount            DECIMAL(19,2)  NOT NULL,
    transaction_date  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description       VARCHAR(200)
);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_from_card ON transactions(from_card_id);
CREATE INDEX idx_transactions_to_card ON transactions(to_card_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
