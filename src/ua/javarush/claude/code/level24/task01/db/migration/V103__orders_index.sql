-- Міграція: індекс за замовленнями для прискорення вибірки
CREATE INDEX idx_orders_created_at ON orders (created_at);
