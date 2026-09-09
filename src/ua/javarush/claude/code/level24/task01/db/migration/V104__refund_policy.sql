-- Міграція: таблиця аудиту політики повернень
CREATE TABLE refund_policy_audit (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT      NOT NULL,
    amount      NUMERIC(12, 2) NOT NULL,
    approved_by VARCHAR(64),
    created_at  TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE INDEX idx_refund_policy_audit_order ON refund_policy_audit (order_id);
