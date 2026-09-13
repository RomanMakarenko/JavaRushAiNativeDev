-- Базова таблиця підписок для сервісу CashFlow MRR
CREATE TABLE subscriptions (
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT      NOT NULL,
    price_cents    BIGINT      NOT NULL,
    billing_period VARCHAR(16) NOT NULL,
    status         VARCHAR(16) NOT NULL,
    started_on     DATE        NOT NULL
);

CREATE INDEX idx_subscriptions_status ON subscriptions (status);