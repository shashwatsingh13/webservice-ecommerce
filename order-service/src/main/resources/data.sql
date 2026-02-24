INSERT INTO order_mst (order_status, total_price) VALUES
('CANCELLED', 2499.99),
('CONFIRMED', 4999.50),
('PENDING', 1599.00),
('CANCELLED', 8999.99),
('CONFIRMED', 1200.00);



INSERT INTO order_item (product_id, quantity, order_id) VALUES
(1, 2, 1),
(3, 1, 1),

(2, 5, 2),
(4, 1, 2),

(5, 3, 3),

(6, 1, 4),
(7, 2, 4),
(8, 1, 4),

(9, 1, 5);
