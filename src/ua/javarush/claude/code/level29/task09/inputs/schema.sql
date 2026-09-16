-- Поточна схема таблиці платежів за підписками.
-- Час зберігається в локальній часовій зоні без явного UTC — це і потрібно нормалізувати.
CREATE TABLE subscription_payments (
    id              BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT       NOT NULL,
    amount_cents    INTEGER      NOT NULL,
    currency        VARCHAR(3)   NOT NULL,
    -- legacy-поле: локальний час сервера, без часової зони
    charged_at      TIMESTAMP    NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'pending',
    created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_subscription_payments_charged_at ON subscription_payments (charged_at);