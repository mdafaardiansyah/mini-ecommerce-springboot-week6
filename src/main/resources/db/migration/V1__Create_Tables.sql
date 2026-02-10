-- ========================================
-- Categories Table
-- ========================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_category_name (name, is_deleted)
);

-- ========================================
-- Products Table
-- ========================================
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(19, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    sku VARCHAR(50),
    category_id BIGINT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_product_sku (sku),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    KEY idx_category_id (category_id),
    KEY idx_product_name (name),
    KEY idx_product_price (price),
    CONSTRAINT chk_price_non_negative CHECK (price >= 0),
    CONSTRAINT chk_stock_non_negative CHECK (stock >= 0)
);

-- ========================================
-- Indexes for Performance
-- ========================================
CREATE INDEX idx_categories_created_at ON categories(created_at);
CREATE INDEX idx_products_created_at ON products(created_at);
CREATE INDEX idx_products_is_deleted ON products(is_deleted);
CREATE INDEX idx_categories_is_deleted ON categories(is_deleted);
