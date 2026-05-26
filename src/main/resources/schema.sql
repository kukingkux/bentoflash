DROP TABLE IF EXISTS bento_ingredients;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS catalog_items;
DROP TABLE IF EXISTS users;

-- 1. Users Table (Modul 4: Dafa)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    karma_score INT DEFAULT 100
);

-- 2. Catalog Items Table (Modul 2: Elfan)
CREATE TABLE catalog_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_type VARCHAR(20) NOT NULL, -- Handles Single-Table Inheritance ('BENTO')
    name VARCHAR(100) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    current_price DECIMAL(10, 2) NOT NULL
    sku_code VARCHAR(50)
);

-- 3. Ingredients Table (Modul 5: Hafidh)
CREATE TABLE ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories INT NOT NULL,
    protein DECIMAL(5,2) NOT NULL,
    carbs DECIMAL(5,2) NOT NULL,
    fat DECIMAL(5,2) NOT NULL
);

-- 4. Many-to-Many Bridge (LocalCultureBento <-> Ingredient)
CREATE TABLE bento_ingredients (
    bento_id BIGINT,
    ingredient_id BIGINT,
    PRIMARY KEY (bento_id, ingredient_id),
    FOREIGN KEY (bento_id) REFERENCES catalog_items(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

-- 5. Orders Table (Modul 3: Rafael)
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    item_id BIGINT,
    pickup_code VARCHAR(20),
    is_picked_up BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'PENDING',
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES catalog_items(id)
);