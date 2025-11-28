-- DML.sql
USE food_ordering_system;

-- Insert Users (3 Managers, 5 Customers)
INSERT INTO User (username, password, user_type) VALUES
('manager1', 'scrypt:32768:8:1$7IdM7QMQWaP9H4VY$aafffed24c0189ed01225db105d71cf17ec3c92f99ebb657f379d5b9e9645798005ecc7b21e63f78f2551ecc5b94c571ed695f9f7a82f0d32111bec29b261ff2', 'manager'),
('manager2', 'scrypt:32768:8:1$YKoHwGuGKfZj2sCK$dda5da6853b60a1d9fff38cb56347019d0fcddb82874c49b41fa5f519e523740c72d32bc49a706bb373cd8021bf8415768d23a16347fd2c0fd19931bb5791c74', 'manager'),
('manager3', 'scrypt:32768:8:1$YKoHwGuGKfZj2sCK$dda5da6853b60a1d9fff38cb56347019d0fcddb82874c49b41fa5f519e523740c72d32bc49a706bb373cd8021bf8415768d23a16347fd2c0fd19931bb5791c74', 'manager'),
('customer1', 'scrypt:32768:8:1$YKoHwGuGKfZj2sCK$dda5da6853b60a1d9fff38cb56347019d0fcddb82874c49b41fa5f519e523740c72d32bc49a706bb373cd8021bf8415768d23a16347fd2c0fd19931bb5791c74', 'customer'),
('customer2', 'hashed_password5', 'customer'),
('customer3', 'hashed_password6', 'customer'),
('customer4', 'hashed_password7', 'customer'),
('customer5', 'hashed_password8', 'customer');

-- Insert Addresses
INSERT INTO Address (user_id, address_line1, city) VALUES
(1, '123 Manager St', 'Istanbul'),
(2, '456 Manager Ave', 'Istanbul'),
(3, '789 Manager Rd', 'Ankara'),
(4, '101 Customer Ln', 'Istanbul'),
(5, '202 Customer Dr', 'Istanbul'),
(6, '303 Customer Blvd', 'Ankara'),
(7, '404 Customer Cir', 'Istanbul'),
(8, '505 Customer Way', 'Ankara');

-- Insert Phones
INSERT INTO Phone (user_id, phone_number) VALUES
(1, '555-0101'),
(2, '555-0102'),
(3, '555-0103'),
(4, '555-0104'),
(5, '555-0105'),
(6, '555-0106'),
(7, '555-0107'),
(8, '555-0108');

-- Insert Restaurants
INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES
(1, 'Pizza Palace', 'Italian', 'Istanbul'),
(1, 'Sushi Stop', 'Japanese', 'Istanbul'),
(2, 'Burger Bonanza', 'American', 'Istanbul'),
(3, 'Kebab King', 'Turkish', 'Ankara'),
(3, 'Taco Town', 'Mexican', 'Ankara');

-- Insert Restaurant Keywords
INSERT INTO RestaurantKeyword (restaurant_id, keyword) VALUES
(1, 'pizza'), (1, 'italian'),
(2, 'sushi'), (2, 'japanese'),
(3, 'burger'), (3, 'american'),
(4, 'kebab'), (4, 'turkish'),
(5, 'taco'), (5, 'mexican');

-- Insert Discounts
INSERT INTO Discount (restaurant_id, discount_name, discount_description, discount_value, start_date, end_date) VALUES
(1, 'Pizza Deal', '10% off pizzas', 10.00, '2025-04-01 00:00:00', '2025-04-30 23:59:59'),
(2, 'Sushi Special', '15% off rolls', 15.00, '2025-04-01 00:00:00', '2025-04-30 23:59:59');

-- Insert Menu Items (15 total)
INSERT INTO MenuItem (restaurant_id, item_name, description, price, image) VALUES
(1, 'Margherita Pizza', 'Classic tomato and mozzarella', 12.99, 'margherita.jpg'),
(1, 'Pepperoni Pizza', 'Pepperoni and cheese', 14.99, 'pepperoni.jpg'),
(1, 'Pasta Carbonara', 'Creamy pasta with bacon', 11.99, 'carbonara.jpg'),
(2, 'California Roll', 'Crab and avocado roll', 8.99, 'california.jpg'),
(2, 'Spicy Tuna Roll', 'Spicy tuna and cucumber', 9.99, 'tuna.jpg'),
(2, 'Miso Soup', 'Traditional miso soup', 3.99, 'miso.jpg'),
(3, 'Cheeseburger', 'Beef patty with cheese', 7.99, 'cheeseburger.jpg'),
(3, 'French Fries', 'Crispy fries', 3.99, 'fries.jpg'),
(3, 'Milkshake', 'Vanilla milkshake', 4.99, 'milkshake.jpg'),
(4, 'Doner Kebab', 'Grilled lamb wrap', 6.99, 'doner.jpg'),
(4, 'Lahmacun', 'Turkish pizza', 5.99, 'lahmacun.jpg'),
(4, 'Ayran', 'Yogurt drink', 1.99, 'ayran.jpg'),
(5, 'Beef Taco', 'Beef and salsa taco', 4.99, 'beeftaco.jpg'),
(5, 'Chicken Quesadilla', 'Chicken and cheese', 6.99, 'quesadilla.jpg'),
(5, 'Guacamole', 'Fresh avocado dip', 3.99, 'guacamole.jpg');

-- Insert Carts (10 total)
INSERT INTO Cart (customer_id) VALUES
(4), (4), (5), (5), (6), (6), (7), (7), (8), (8);

-- Insert Cart Items
INSERT INTO CartItem (cart_id, menu_item_id, quantity) VALUES
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

-- Insert Orders (10 total)
INSERT INTO `Order` (customer_id, restaurant_id, order_status, order_date) VALUES
(4, 1, 'accepted', '2025-04-10 12:00:00'),
(4, 2, 'accepted', '2025-04-10 13:00:00'),
(5, 3, 'accepted', '2025-04-10 14:00:00'),
(5, 4, 'accepted', '2025-04-10 15:00:00'),
(6, 5, 'accepted', '2025-04-10 16:00:00'),
(6, 1, 'accepted', '2025-04-11 12:00:00'),
(7, 2, 'accepted', '2025-04-11 13:00:00'),
(7, 3, 'accepted', '2025-04-11 14:00:00'),
(8, 4, 'accepted', '2025-04-11 15:00:00'),
(8, 5, 'accepted', '2025-04-11 16:00:00');

-- Insert Order Items
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

-- Insert Ratings (10 per restaurant, 50 total)
INSERT INTO Rating (customer_id, order_id, rating_value, review_text) VALUES
-- Restaurant 1 (Pizza Palace)
(4, 1, 5, 'Great pizza!'),
(6, 6, 4, 'Good but slow'),

-- Restaurant 2 (Sushi Stop)
(4, 2, 5, 'Fresh sushi'),
(7, 7, 4, 'Nice rolls'),

-- Restaurant 3 (Burger Bonanza)
(5, 3, 5, 'Best burgers'),
(7, 8, 4, 'Nice fries'),
-- Restaurant 4 (Kebab King)
(5, 4, 5, 'Amazing kebab'),
(8, 9, 4, 'Nice lahmacun'),

-- Restaurant 5 (Taco Town)
(8, 10, 4, 'Nice guacamole'),

(6, 5, 4, 'Good service');