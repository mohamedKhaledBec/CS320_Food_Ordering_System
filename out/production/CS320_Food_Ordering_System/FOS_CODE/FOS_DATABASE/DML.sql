USE food_ordering_system;

INSERT INTO User (email, password, user_type) VALUES
('manager.1@gmail.com', '$2a$10$7tPkODJIS/0IENdIh1fCOusytjGgcjep/U2sPphzDgoEagTvLG0Ra', 'manager'),
('manager.2@gmail.com', '$2a$10$iPSOSNT0LRdxLPTtpZZGPexnUS3Fs9lNbyxOrbcLwMSsjzHEfVPQa', 'manager'),
('manager.3@gmail.com', '$2a$10$Ud7q.ZXvKF1p0C4w6Pp12OO6xaDz7/iJbeZbDjdo0zAu2b6903MRu', 'manager'),
('customer.1@gmail.com', '$2a$10$QYjRugyWC3sHYEqJCljG8OsPiCEMFNb4IPdtj/o9MPyW6zFbWrAP6', 'customer'),
('customer.2@gmail.com', '$2a$10$4KqLI5nNNpPlP4fdOSVljeKCVtsxOZyjtb/kAKCw0MnrTFYuoqYVa', 'customer'),
('customer.3@gmail.com', '$2a$10$q/wuVA4yA5uz18aqRoTc5edVffPSiAOLbraJAkLB4liyu42dFkkiW', 'customer'),
('customer.4@gmail.com', '$2a$10$K8Isy/sdmnCMoOyvBy3KA.IWswaLD7GWWywbG.HOD2fpzOYch3lqG', 'customer'),
('customer.5@gmail.com', '$2a$10$JljDtMj78ZS8LsGo.EP0buhDVri0uwQS7lpvsSUOKcvfn3k8GKVve', 'customer');

INSERT INTO Address (customer_id, address_line, city, state, zip) VALUES
(4,'Kadıköy Moda Cd No:12','İstanbul','Marmara','34710'),
(4,'Beşiktaş Çarşı Sk No:7','İstanbul','Marmara','34353'),
(4,'Üsküdar Bağlarbaşı Cd No:19','İstanbul','Marmara','34664'),
(5,'Çankaya Atatürk Blv No:45','Ankara','İç Anadolu','06690'),
(5,'Kızılay Mithatpaşa Cd No:18','Ankara','İç Anadolu','06420'),
(5,'Batıkent Kentkoop Mh No:77','Ankara','İç Anadolu','06370'),
(6,'Konak Cumhuriyet Cd No:22','İzmir','Ege','35250'),
(6,'Alsancak Kordonboyu No:5','İzmir','Ege','35220'),
(7,'Nilüfer FSM Blv No:30','Bursa','Marmara','16140'),
(7,'Osmangazi Altıparmak Cd No:16','Bursa','Marmara','16050'),
(8,'Muratpaşa Lara Cd No:88','Antalya','Akdeniz','07160'),
(8,'Konyaaltı Atatürk Blv No:102','Antalya','Akdeniz','07070');

INSERT INTO Phone (customer_id, phone_number) VALUES
(4,'5551001'),(4,'5551002'),(4,'5551003'),
(5,'5552001'),(5,'5552002'),(5,'5552003'),
(6,'5553001'),(6,'5553002'),
(7,'5554001'),(7,'5554002'),
(8,'5555001'),(8,'5555002');

INSERT INTO Restaurant (manager_id,name,cuisine_type,city) VALUES
(1,'Pizza Palace','Italian','İstanbul'),
(1,'Sushi Stop','Japanese','İstanbul'),
(1,'Burger House','American','İstanbul'),
(2,'Burger Bonanza','American','Ankara'),
(2,'Ankara Kebapçısı','Turkish','Ankara'),
(3,'Kebab King','Turkish','İzmir'),
(3,'Ege Seafood','Seafood','İzmir'),
(3,'Taco Town','Mexican','Antalya'),
(3,'Antalya Grill','Turkish','Antalya');

INSERT INTO RestaurantKeyword (restaurant_id,keyword) VALUES
(1,'pizza'),(1,'italian'),
(2,'sushi'),(2,'japanese'),
(3,'burger'),(3,'fastfood'),
(4,'burger'),(4,'american'),
(5,'kebab'),(5,'turkish'),
(6,'kebab'),(6,'grill'),
(7,'fish'),(7,'seafood'),
(8,'taco'),(8,'mexican'),
(9,'grill'),(9,'turkish');

INSERT INTO MenuItem (restaurant_id,item_name,description,price) VALUES
(1,'Margherita Pizza','Classic pizza',12.00),
(1,'Pepperoni Pizza','Pepperoni and cheese',14.00),
(1,'Four Cheese Pizza','Cheese blend',15.00),
(1,'Garlic Bread','Side dish',5.00),
(2,'California Roll','Crab avocado',9.00),
(2,'Spicy Tuna Roll','Spicy tuna',9.50),
(2,'Salmon Nigiri','Salmon sushi',11.00),
(2,'Miso Soup','Soup',4.00),
(3,'Cheeseburger','Beef burger',8.50),
(3,'Double Cheeseburger','Double beef',11.00),
(3,'French Fries','Crispy fries',4.00),
(3,'Onion Rings','Fried onions',4.50),
(4,'Classic Burger','Single beef',9.50),
(4,'Double Burger','Double beef',11.00),
(4,'Milkshake','Vanilla shake',5.00),
(5,'Adana Kebab','Spicy kebab',13.00),
(5,'Urfa Kebab','Mild kebab',12.50),
(5,'Lahmacun','Thin dough',4.50),
(6,'Döner Wrap','Chicken döner',7.50),
(6,'Döner Plate','Beef döner',10.00),
(6,'Ayran','Yogurt drink',2.00),
(7,'Grilled Sea Bass','Fresh fish',18.00),
(7,'Calamari','Fried calamari',12.00),
(7,'Shrimp','Garlic shrimp',15.00),
(8,'Beef Taco','Beef taco',6.50),
(8,'Chicken Taco','Chicken taco',6.00),
(8,'Nachos','Cheese nachos',5.50),
(9,'Mixed Grill','Mixed meat',19.00),
(9,'Chicken Skewer','Grilled chicken',14.00),
(9,'Rice Pilaf','Side dish',3.50);

INSERT INTO Discount (menu_item_id,discount_name,discount_description,discount_percentage,start_date,end_date) VALUES
(1,'Autumn Pizza','Fall campaign',10.00,'2025-09-01','2025-11-30'),
(2,'Pepperoni Deal','Seasonal',15.00,'2025-09-15','2025-12-15'),
(5,'Sushi Days','Roll discount',20.00,'2025-10-01','2025-12-01'),
(9,'Burger Week','Burger promo',10.00,'2025-09-10','2025-10-31'),
(16,'Kebab Fest','Festival',20.00,'2025-10-01','2025-11-15'),
(22,'Seafood Special','Fresh catch',15.00,'2025-11-01','2025-12-20'),
(26,'Taco Night','Mexican night',10.00,'2025-09-05','2025-10-20'),
(29,'Grill Season','Grill promo',12.00,'2025-10-15','2025-12-31');

INSERT INTO Card (customer_id,card_no,card_holder_name,expiry_date,cvv) VALUES
(4,'1111222233334444','Customer One','2027-12-31','123'),
(5,'5555666677778888','Customer Two','2026-06-30','456'),
(6,'9999000011112222','Customer Three','2028-09-30','789');

INSERT INTO `Order` (customer_id,restaurant_id,phone_number,order_status,order_date,delivery_address_id,card_no) VALUES
(4,1,'5551001','delivered','2025-09-05 12:00:00',1,'1111222233334444'),
(4,2,'5551002','delivered','2025-09-10 13:00:00',2,'1111222233334444'),
(4,3,'5551003','sent','2025-10-01 14:00:00',3,'1111222233334444'),
(4,1,'5551001','pending','2025-12-10 09:00:00',1,'1111222233334444'),
(5,4,'5552001','delivered','2025-09-06 15:00:00',4,'5555666677778888'),
(5,5,'5552002','delivered','2025-09-20 16:00:00',5,'5555666677778888'),
(5,4,'5552003','sent','2025-11-03 17:00:00',6,'5555666677778888'),
(6,6,'5553001','delivered','2025-09-15 18:00:00',7,'9999000011112222'),
(6,7,'5553002','preparing','2025-12-05 11:00:00',8,NULL),
(7,3,'5554001','delivered','2025-10-02 19:00:00',9,NULL),
(7,3,'5554002','sent','2025-12-08 12:00:00',10,NULL),
(8,8,'5555001','delivered','2025-10-05 20:00:00',11,NULL),
(8,9,'5555002','delivered','2025-11-01 21:00:00',12,NULL),
(8,9,'5555001','pending','2025-12-12 13:00:00',11,NULL);

INSERT INTO CartItem (order_id,menu_item_id,price,quantity) VALUES
(1,1,12.00,1),(1,4,5.00,1),
(2,5,9.00,2),
(3,9,8.50,1),
(4,2,14.00,2),
(5,13,9.50,2),
(6,16,13.00,1),(6,18,4.50,2),
(7,14,11.00,1),
(8,19,7.50,2),
(9,21,18.00,1),(9,22,12.00,1),
(10,9,8.50,2),
(11,9,8.50,1),
(12,26,6.50,3),
(13,29,19.00,1);

INSERT INTO Rating (order_id,rating_value,rating_comment) VALUES
(1,5,'Excellent'),
(2,4,'Very good'),
(5,5,'Perfect kebab'),
(6,4,'Tasty'),
(8,5,'Fresh food'),
(10,4,'Nice burgers'),
(12,5,'Great tacos'),
(13,4,'Good grill');









