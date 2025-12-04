DROP DATABASE IF EXISTS food_ordering_system;
CREATE DATABASE IF NOT EXISTS food_ordering_system;
USE food_ordering_system;

CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(400) NOT NULL,
    user_type ENUM('customer', 'manager') NOT NULL
);

CREATE TABLE Address (
	address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    zip VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE Phone (
    user_id INT NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    PRIMARY KEY (user_id, phone_number),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE Restaurant (
    restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
    manager_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    cuisine_type VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE CASCADE
);


CREATE TABLE RestaurantKeyword (
    restaurant_keyword_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    keyword VARCHAR(50) NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);


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

CREATE TABLE MenuItem (
    menu_item_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE Order (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    order_status ENUM('pending', 'preparing', 'sent', 'delivered') NOT NULL default 'preparing',
    order_date DATETIME NOT NULL,
    delivery_address_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES Restaurant(restaurant_id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_address_id) REFERENCES Address(address_id) ON DELETE CASCADE
);

CREATE TABLE CartItem (
    order_id INT,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, menu_item_id),
    FOREIGN KEY (order_id) REFERENCES Order(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id) ON DELETE CASCADE
);

CREATE TABLE OrderItem (
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, menu_item_id),
    FOREIGN KEY (order_id) REFERENCES Order(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES MenuItem(menu_item_id) ON DELETE CASCADE
);

CREATE TABLE Rating (
    order_id INT NOT NULL,
    rating_value INT NOT NULL CHECK (rating_value BETWEEN 1 AND 5),
    review_text TEXT,
    PRIMARY KEY (order_id),
    FOREIGN KEY (order_id) REFERENCES Order(order_id) ON DELETE CASCADE
);

CREATE TABLE Card (
    customer_id INT,
    card_no CHAR(16) PRIMARY KEY,
    card_holder_name VARCHAR(100) NOT NULL,
    expiry_date DATE NOT NULL,
    cvv CHAR(3) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES User(user_id) ON DELETE CASCADE
);