-- Час переведення підписки в паузу.
-- Поле додано, але в розрахунку MRR поки не використовується.
ALTER TABLE subscriptions ADD COLUMN paused_at TIMESTAMP NULL;