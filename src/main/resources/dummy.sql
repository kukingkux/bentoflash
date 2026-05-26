-- Dafa - Modul 4: Karma Tracking)
INSERT INTO users (name, email, karma_score) VALUES 
('Budi Utomo', 'budi.utomo@student.ac.id', 100),       -- User normal
('Siti Rahma', 'siti.rahma@student.ac.id', 130),       -- User dengan reliabilitas tinggi
('Andi Wijaya', 'andi.wijaya@student.ac.id', 45);       -- User yang sering ghosting, karma score rendah


-- Elfan Modul 2: Flash Pricing)
INSERT INTO catalog_items (item_type, name, base_price, current_price) VALUES 
('BENTO', 'Rendang Padang Premium Bento', 35000.00, 35000.00), -- Eligible for discount, dapet diskon
('BENTO', 'Ayam Penyet Pedas Flash', 25000.00, 25000.00),      -- Eligible for discount, dapet diskon
('BEVERAGE', 'Es Teh Manis Segar', 5000.00, 5000.00);          -- Not Perishable (Exempt), tidak dapat diskon


--Hafidh Modul 5: Macro Calculator)
INSERT INTO ingredients (name, calories, protein, carbs, fat) VALUES 
('Nasi Putih (1 Portion)', 200, 4.00, 45.00, 0.50),
('Daging Rendang Sapi', 300, 25.00, 3.00, 21.00),
('Ayam Goreng Penyet', 240, 20.00, 0.00, 17.00),
('Sambal ijo & Lalapan', 45, 1.20, 6.00, 2.50);


-- Rendang Bento (ID: 1) contains Nasi Putih (1), Daging Rendang (2), and Sambal (4)
INSERT INTO bento_ingredients (bento_id, ingredient_id) VALUES 
(1, 1),
(1, 2),
(1, 4);

-- Ayam Penyet Bento (ID: 2) contains Nasi Putih (1), Ayam Goreng (3), and Sambal (4)
INSERT INTO bento_ingredients (bento_id, ingredient_id) VALUES 
(2, 1),
(2, 3),
(2, 4);

-- Rafael Modul 3: FIFO Queue)
INSERT INTO orders (user_id, item_id, pickup_code, status, order_time) VALUES 
(1, 1, 'BENTO-7X8', 'PENDING', '2026-05-26 11:30:00'), -- First in queue
(2, 2, 'BENTO-2M4', 'PENDING', '2026-05-26 11:35:00'), -- Second in queue
(3, 1, 'BENTO-990', 'GHOSTED', '2026-05-25 13:00:00'); -- Past order for testing Dafa's system