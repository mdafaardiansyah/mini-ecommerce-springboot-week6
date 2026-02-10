-- ========================================
-- Sample Categories
-- ========================================
INSERT INTO categories (name, description, is_deleted) VALUES
('Electronics', 'Electronic devices and accessories', FALSE),
('Clothing', 'Fashion and apparel items', FALSE),
('Books', 'Books and educational materials', FALSE),
('Home & Garden', 'Home improvement and garden supplies', FALSE),
('Sports', 'Sports equipment and accessories', FALSE)
ON DUPLICATE KEY UPDATE name=name;

-- ========================================
-- Sample Products
-- ========================================
INSERT INTO products (name, description, price, stock, sku, category_id, is_deleted) VALUES
-- Electronics
('Wireless Bluetooth Headphones', 'High-quality wireless headphones with noise cancellation', 89.99, 50, 'ELEC-001', 1, FALSE),
('USB-C Charging Cable', 'Fast charging USB-C cable, 6 feet long', 12.99, 200, 'ELEC-002', 1, FALSE),
('Laptop Stand', 'Adjustable aluminum laptop stand for better ergonomics', 34.99, 75, 'ELEC-003', 1, FALSE),

-- Clothing
('Men\'s Cotton T-Shirt', '100% cotton comfortable t-shirt', 19.99, 150, 'CLTH-001', 2, FALSE),
('Women\'s Running Shoes', 'Lightweight and breathable running shoes', 59.99, 80, 'CLTH-002', 2, FALSE),
('Denim Jacket', 'Classic blue denim jacket', 79.99, 40, 'CLTH-003', 2, FALSE),

-- Books
('Java Programming for Beginners', 'Complete guide to Java programming', 29.99, 100, 'BOOK-001', 3, FALSE),
('The Art of Clean Code', 'Best practices for writing maintainable code', 24.99, 120, 'BOOK-002', 3, FALSE),
('Data Structures Handbook', 'Comprehensive data structures reference', 34.99, 60, 'BOOK-003', 3, FALSE),

-- Home & Garden
('LED Desk Lamp', 'Adjustable brightness LED desk lamp', 29.99, 90, 'HOME-001', 4, FALSE),
('Garden Tool Set', 'Essential garden tools set', 44.99, 55, 'HOME-002', 4, FALSE),
('Plant Pot Set', 'Set of 3 ceramic plant pots', 19.99, 110, 'HOME-003', 4, FALSE),

-- Sports
('Yoga Mat Premium', 'Non-slip yoga mat with carrying strap', 24.99, 130, 'SPRT-001', 5, FALSE),
('Resistance Bands Set', 'Set of 5 resistance bands with different strengths', 15.99, 180, 'SPRT-002', 5, FALSE),
('Water Bottle Insulated', 'Double-wall insulated stainless steel water bottle', 21.99, 140, 'SPRT-003', 5, FALSE)
ON DUPLICATE KEY UPDATE name=name;
