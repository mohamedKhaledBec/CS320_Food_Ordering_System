-- DML.sql
USE food_ordering_system;

-- ===========
-- Users (unchanged)
-- ===========
INSERT INTO User (email, password, user_type) VALUES
('manager.1@gmail.com', '$2a$10$7tPkODJIS/0IENdIh1fCOusytjGgcjep/U2sPphzDgoEagTvLG0Ra', 'manager'),
('manager.2@gmail.com', '$2a$10$iPSOSNT0LRdxLPTtpZZGPexnUS3Fs9lNbyxOrbcLwMSsjzHEfVPQa', 'manager'),
('manager.3@gmail.com', '$2a$10$Ud7q.ZXvKF1p0C4w6Pp12OO6xaDz7/iJbeZbDjdo0zAu2b6903MRu', 'manager'),
('customer.1@gmail.com', '$2a$10$QYjRugyWC3sHYEqJCljG8OsPiCEMFNb4IPdtj/o9MPyW6zFbWrAP6', 'customer'),
('customer.2@gmail.com', '$2a$10$4KqLI5nNNpPlP4fdOSVljeKCVtsxOZyjtb/kAKCw0MnrTFYuoqYVa', 'customer'),
('customer.3@gmail.com', '$2a$10$q/wuVA4yA5uz18aqRoTc5edVffPSiAOLbraJAkLB4liyu42dFkkiW', 'customer'),
('customer.4@gmail.com', '$2a$10$K8Isy/sdmnCMoOyvBy3KA.IWswaLD7GWWywbG.HOD2fpzOYch3lqG', 'customer'),
('customer.5@gmail.com', '$2a$10$JljDtMj78ZS8LsGo.EP0buhDVri0uwQS7lpvsSUOKcvfn3k8GKVve', 'customer');

-- ===========
-- Addresses
-- ===========
INSERT INTO Address (user_id, address_line, city) VALUES
(1, '123 Manager St', 'Istanbul'),
(2, '456 Manager Ave', 'Istanbul'),
(3, '789 Manager Rd', 'Ankara'),
(4, '101 Customer Ln', 'Istanbul'),
(5, '202 Customer Dr', 'Istanbul'),
(6, '303 Customer Blvd', 'Ankara'),
(7, '404 Customer Cir', 'Istanbul'),
(8, '505 Customer Way', 'Ankara');

-- ===========
-- Phones
-- ===========
INSERT INTO Phone (user_id, phone_number) VALUES
(1, '555-0101'),
(2, '555-0102'),
(3, '555-0103'),
(4, '555-0104'),
(5, '555-0105'),
(6, '555-0106'),
(7, '555-0107'),
(8, '555-0108');

-- ===========
-- Restaurants
-- ===========
INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES
(1, 'Pizza Palace', 'Italian', 'Istanbul'),
(1, 'Sushi Stop', 'Japanese', 'Istanbul'),
(2, 'Burger Bonanza', 'American', 'Istanbul'),
(3, 'Kebab King', 'Turkish', 'Ankara'),
(3, 'Taco Town', 'Mexican', 'Ankara');

-- ===========
-- Restaurant Keywords
-- ===========
INSERT INTO RestaurantKeyword (restaurant_id, keyword) VALUES
(1, 'pizza'), (1, 'italian'),
(2, 'sushi'), (2, 'japanese'),
(3, 'burger'), (3, 'american'),
(4, 'kebab'), (4, 'turkish'),
(5, 'taco'), (5, 'mexican');

-- ===========
-- Discounts
-- ===========
INSERT INTO Discount (restaurant_id, discount_name, discount_description, discount_value, start_date, end_date) VALUES
(1, 'Pizza Deal', '10% off pizzas', 10.00, '2025-04-01 00:00:00', '2025-04-30 23:59:59'),
(2, 'Sushi Special', '15% off rolls', 15.00, '2025-04-01 00:00:00', '2025-04-30 23:59:59');

-- ===========
-- Menu Items  (no image column in DDL)
-- ===========
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

-- ===========
-- Orders
-- DDL: (customer_id, restaurant_id, order_status, order_date, delivery_address_id)
-- order_status ENUM('pending','preparing','sent','delivered')
-- Here we assume all sample orders are already delivered.
-- delivery_address_id chosen to match the customer's address_id (4..8)
-- ===========
INSERT INTO Order (customer_id, restaurant_id, order_status, order_date, delivery_address_id) VALUES
(4, 1, 'delivered', '2025-04-10 12:00:00', 4),
(4, 2, 'delivered', '2025-04-10 13:00:00', 4),
(5, 3, 'delivered', '2025-04-10 14:00:00', 5),
(5, 4, 'delivered', '2025-04-10 15:00:00', 5),
(6, 5, 'delivered', '2025-04-10 16:00:00', 6),
(6, 1, 'delivered', '2025-04-11 12:00:00', 6),
(7, 2, 'delivered', '2025-04-11 13:00:00', 7),
(7, 3, 'delivered', '2025-04-11 14:00:00', 7),
(8, 4, 'delivered', '2025-04-11 15:00:00', 8),
(8, 5, 'delivered', '2025-04-11 16:00:00', 8);

-- ===========
-- Order Items
-- ===========
INSERT INTO OrderItem (order_id, menu_item_id, quantity) VALUES
(1, 1, 2), (1, 2, 1),
(2, 4, 3),
(3, 7, 2), (3, 8, 2),
(4, 10, 1),
(5, 13, 2),
(6, 1, 1), (6, 3, 1),
(7, 4, 2),
(8, 7, 1), (8, 9, 1),
(9, 10, 2),
(10, 13, 1), (10, 15, 2);

-- ===========
-- Ratings
-- DDL columns: order_id, rating_value, review_text
-- (customer_id is referenced in PK/FK but NOT actually defined as a column in DDL)
-- So we only insert into the existing columns.
-- ===========
INSERT INTO Rating (order_id, rating_value, review_text) VALUES
-- Restaurant 1 (Pizza Palace)
(1, 5, 'Great pizza!'),
(6, 4, 'Good but slow'),

-- Restaurant 2 (Sushi Stop)
(2, 5, 'Fresh sushi'),
(7, 4, 'Nice rolls'),

-- Restaurant 3 (Burger Bonanza)
(3, 5, 'Best burgers'),
(8, 4, 'Nice fries'),

-- Restaurant 4 (Kebab King)
(4, 5, 'Amazing kebab'),
(9, 4, 'Nice lahmacun'),

-- Restaurant 5 (Taco Town)
(10, 4, 'Nice guacamole'),

-- Extra rating
(5, 4, 'Good service');

-- ===========
-- Optional: Sample Cards (if you want seed data here too)
-- ===========
INSERT INTO Card (customer_id, card_no, card_holder_name, expiry_date, cvv) VALUES
(4, '1111222233334444', 'Customer One', '2027-12-31', '123'),
(5, '5555666677778888', 'Customer Two', '2026-06-30', '456');