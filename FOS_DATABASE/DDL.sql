-- DDL.sql
DROP DATABASE IF EXISTS food_ordering_system;
CREATE DATABASE IF NOT EXISTS food_ordering_system;
USE food_ordering_system;

-- User (Strong Entity)
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(400) NOT NULL,
    user_type ENUM('customer', 'manager') NOT NULL
);

-- Address (Weak Entity)
CREATE TABLE Address (
    user_id INT NOT NULL,
    address_line1 VARCHAR(100) NOT NULL,
    address_line2 VARCHAR(100),
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    zip VARCHAR(10),
    PRIMARY KEY (user_id, address_line1),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- Phone (Weak Entity)
CREATE TABLE Phone (
    user_id INT NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    PRIMARY KEY (user_id, phone_number),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- Restaurant (Strong Entity)
CREATE TABLE Restaurant (
    restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
    manager_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    cuisine_type VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- RestaurantKeyword (Strong Entity, could be weak but kept simple)
CREATE TABLE RestaurantKeyword (
    restaurant_keyword_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    keyword VARCHAR(50) NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);

-- Discount (Strong Entity)
CREATE TABLE Discount (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    discount_name VARCHAR(50) NOT NULL,
    discount_description TEXT,
    discount_value DECIMAL(5,2) NOT NULL CHECK (discount_value >= 0 AND discount_value <= 100),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);

-- MenuItem (Strong Entity)
CREATE TABLE MenuItem (
    menu_item_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    image VARCHAR(255),
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);

-- Cart (Strong Entity)
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- CartItem (Weak Entity)
CREATE TABLE CartItem (
    cart_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, menu_item_id),
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id) ON DELETE CASCADE
);

-- Order (Strong Entity)
CREATE TABLE `Order` (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    order_status ENUM('pending', 'preparing', 'sent', 'accepted') NOT NULL,
    order_date DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);

-- OrderItem (Weak Entity)
CREATE TABLE OrderItem (
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, menu_item_id),
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id) ON DELETE CASCADE
);

-- Rating (Weak Entity)
CREATE TABLE Rating (
    customer_id INT NOT NULL,
    order_id INT NOT NULL,
    rating_value INT NOT NULL CHECK (rating_value BETWEEN 1 AND 5),
    review_text TEXT,
    PRIMARY KEY (customer_id, order_id),
    FOREIGN KEY (customer_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id) ON DELETE CASCADE
);