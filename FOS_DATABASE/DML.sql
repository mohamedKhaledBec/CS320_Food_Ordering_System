USE food_ordering_system;

-- USERS (unchanged)
INSERT INTO User (email, password, user_type) VALUES
('manager.1@gmail.com', '$2a$10$7tPkODJIS/0IENdIh1fCOusytjGgcjep/U2sPphzDgoEagTvLG0Ra', 'manager'),
('manager.2@gmail.com', '$2a$10$iPSOSNT0LRdxLPTtpZZGPexnUS3Fs9lNbyxOrbcLwMSsjzHEfVPQa', 'manager'),
('manager.3@gmail.com', '$2a$10$Ud7q.ZXvKF1p0C4w6Pp12OO6xaDz7/iJbeZbDjdo0zAu2b6903MRu', 'manager'),
('customer.1@gmail.com', '$2a$10$QYjRugyWC3sHYEqJCljG8OsPiCEMFNb4IPdtj/o9MPyW6zFbWrAP6', 'customer'),
('customer.2@gmail.com', '$2a$10$4KqLI5nNNpPlP4fdOSVljeKCVtsxOZyjtb/kAKCw0MnrTFYuoqYVa', 'customer'),
('customer.3@gmail.com', '$2a$10$q/wuVA4yA5uz18aqRoTc5edVffPSiAOLbraJAkLB4liyu42dFkkiW', 'customer'),
('customer.4@gmail.com', '$2a$10$K8Isy/sdmnCMoOyvBy3KA.IWswaLD7GWWywbG.HOD2fpzOYch3lqG', 'customer'),
('customer.5@gmail.com', '$2a$10$JljDtMj78ZS8LsGo.EP0buhDVri0uwQS7lpvsSUOKcvfn3k8GKVve', 'customer');

-- ADDRESSES
INSERT INTO Address (customer_id, address_line, city) VALUES
(4, '123 Manager St', 'İstanbul'),
(4, '456 Manager Ave', 'İstanbul'),
(5, '789 Manager Rd', 'Ankara'),
(5, '101 Customer Ln', 'İstanbul'),
(6, '202 Customer Dr', 'İstanbul'),
(7, '303 Customer Blvd', 'Ankara'),
(8, '404 Customer Cir', 'İstanbul'),
(8, '505 Customer Way', 'Ankara');

-- PHONES
INSERT INTO Phone (customer_id, phone_number) VALUES
(4, '555-0101'),
(4, '555-0102'),
(5, '555-0103'),
(5, '555-0104'),
(6, '555-0105'),
(6, '555-0106'),
(7, '555-0107'),
(8, '555-0108');

-- RESTAURANTS
INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES
(1, 'Pizza Palace', 'Italian', 'İstanbul'),
(1, 'Sushi Stop', 'Japanese', 'İstanbul'),
(2, 'Burger Bonanza', 'American', 'İstanbul'),
(3, 'Kebab King', 'Turkish', 'Ankara'),
(3, 'Taco Town', 'Mexican', 'Ankara');

-- KEYWORDS
INSERT INTO RestaurantKeyword (restaurant_id, keyword) VALUES
(1, 'pizza'), (1, 'italian'),
(2, 'sushi'), (2, 'japanese'),
(3, 'burger'), (3, 'american'),
(4, 'kebab'), (4, 'turkish'),
(5, 'taco'), (5, 'mexican');

-- MENU ITEMS (needed for Discount FK)
INSERT INTO MenuItem (restaurant_id, item_name, description, price) VALUES
(1, 'Margherita Pizza', 'Classic tomato and mozzarella', 12.99),
(1, 'Pepperoni Pizza', 'Pepperoni and cheese', 14.99),
(1, 'Pasta Carbonara', 'Creamy pasta with bacon', 11.99),
(2, 'California Roll', 'Crab and avocado roll', 8.99),
(2, 'Spicy Tuna Roll', 'Spicy tuna and cucumber', 9.99),
(2, 'Miso Soup', 'Traditional miso soup', 3.99),
(3, 'Cheeseburger', 'Beef patty with cheese', 7.99),
(3, 'French Fries', 'Crispy fries', 3.99),
(3, 'Milkshake', 'Vanilla milkshake', 4.99),
(4, 'Doner Kebab', 'Grilled lamb wrap', 6.99),
(4, 'Lahmacun', 'Turkish pizza', 5.99),
(4, 'Ayran', 'Yogurt drink', 1.99),
(5, 'Beef Taco', 'Beef and salsa taco', 4.99),
(5, 'Chicken Quesadilla', 'Chicken and cheese', 6.99),
(5, 'Guacamole', 'Fresh avocado dip', 3.99);

-- DISCOUNTS (rewritten based on real DDL)
INSERT INTO Discount (menu_item_id, discount_name, discount_description, discount_percentage, start_date, end_date)
VALUES
(1, 'Pizza Deal', '10% off pizzas', 10.00, '2025-04-01 00:00:00', '2025-12-30 23:59:59'),
(4, 'Sushi Special', '15% off rolls', 15.00, '2025-04-01 00:00:00', '2025-12-30 23:59:59');

-- CARDS
INSERT INTO Card (customer_id, card_no, card_holder_name, expiry_date, cvv) VALUES
(4, '1111222233334444', 'Customer One', '2027-12-31', '123'),
(5, '5555666677778888', 'Customer Two', '2026-06-30', '456');

-- ORDERS
INSERT INTO `Order`
(customer_id, restaurant_id, phone_number, order_status, order_date, delivery_address_id, card_no)
VALUES
(4, 1, '555-0101', 'delivered', '2025-04-10 12:00:00', 4, '1111222233334444'),
(4, 1, '555-0101', 'sent', '2025-12-11 12:00:00', 4, '1111222233334444'),
(4, 1, '555-0101', 'preparing', '2025-12-11 12:00:00', 4, '1111222233334444'),
(4, 1, '555-0101', 'pending', '2025-12-11 18:00:00', 4, '1111222233334444'),
(4, 2, '555-0102', 'delivered', '2025-04-10 13:00:00', 4, '1111222233334444'),

(5, 3, '555-0103', 'delivered', '2025-04-10 14:00:00', 5, '5555666677778888'),
(5, 4, '555-0104', 'delivered', '2025-04-10 15:00:00', 5, '5555666677778888'),

(6, 5, '555-0105', 'delivered', '2025-04-10 16:00:00', 6, NULL),
(6, 1, '555-0106', 'delivered', '2025-04-11 12:00:00', 6, NULL),

(7, 2, '555-0107', 'delivered', '2025-04-11 13:00:00', 7, NULL),
(7, 3, '555-0107', 'delivered', '2025-04-11 14:00:00', 7, NULL),

(8, 4, '555-0108', 'delivered', '2025-04-11 15:00:00', 8, NULL),
(8, 5, '555-0108', 'delivered', '2025-04-11 16:00:00', 8, NULL);


-- ORDER ITEMS
INSERT INTO CartItem (order_id, menu_item_id, price, quantity) VALUES
(1, 1, 10.00, 2), (1, 2, 12.50, 1),
(2, 4, 15.00, 3),
(3, 7, 20.00, 2), (3, 8, 18.00, 2),
(4, 10, 22.00, 1),
(5, 13, 14.00, 2),
(6, 1, 10.00, 1), (6, 3, 11.00, 1),
(7, 4, 15.00, 2),
(8, 7, 20.00, 1), (8, 9, 17.00, 1),
(9, 10, 22.00, 2),
(10, 13, 14.00, 1), (10, 15, 19.00, 2);


-- RATINGS (fixed column names)
INSERT INTO Rating (order_id, rating_value, rating_comment) VALUES
(1, 5, 'Great pizza!'),
(6, 4, 'Good but slow'),
(7, 4, 'Nice rolls'),
(3, 5, 'Best burgers'),
(8, 4, 'Nice fries'),
(4, 5, 'Amazing kebab'),
(9, 4, 'Nice lahmacun'),
(10, 4, 'Nice guacamole'),
(5, 4, 'Good service');

