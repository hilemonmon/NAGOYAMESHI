# Sample data for NAGOYAMESHI

INSERT INTO roles (name) VALUES
('ROLE_FREE'),
('ROLE_PAID'),
('ROLE_ADMIN');

INSERT INTO users (name, furigana, postal_code, address, phone_number, birthday, occupation, email, password, role_id, enabled, stripe_customer_id, created_at, updated_at) VALUES
('Taro Yamada', 'ヤマダタロウ', '460-0001', 'Nagoya', '000-0000-0001', '1990-01-01', 'Engineer', 'taro@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hanako Suzuki', 'スズキハナコ', '460-0002', 'Nagoya', '000-0000-0002', '1992-02-02', 'Designer', 'hanako@example.com', '{noop}password', 2, TRUE, 'cus_test', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Admin User', 'アドミンユーザー', '460-0003', 'Nagoya', '000-0000-0003', '1985-03-03', 'Admin', 'admin@example.com', '{noop}password', 3, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User4', 'ユーザー4', '460-0004', 'Nagoya', '000-0000-0004', '1990-05-05', 'Tester', 'user4@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User5', 'ユーザー5', '460-0005', 'Nagoya', '000-0000-0005', '1990-06-06', 'Tester', 'user5@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User6', 'ユーザー6', '460-0006', 'Nagoya', '000-0000-0006', '1990-07-07', 'Tester', 'user6@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User7', 'ユーザー7', '460-0007', 'Nagoya', '000-0000-0007', '1990-08-08', 'Tester', 'user7@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User8', 'ユーザー8', '460-0008', 'Nagoya', '000-0000-0008', '1990-09-09', 'Tester', 'user8@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User9', 'ユーザー9', '460-0009', 'Nagoya', '000-0000-0009', '1990-10-10', 'Tester', 'user9@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User10', 'ユーザー10', '460-0010', 'Nagoya', '000-0000-0010', '1990-11-11', 'Tester', 'user10@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User11', 'ユーザー11', '460-0011', 'Nagoya', '000-0000-0011', '1990-12-12', 'Tester', 'user11@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User12', 'ユーザー12', '460-0012', 'Nagoya', '000-0000-0012', '1990-01-13', 'Tester', 'user12@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User13', 'ユーザー13', '460-0013', 'Nagoya', '000-0000-0013', '1990-02-14', 'Tester', 'user13@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User14', 'ユーザー14', '460-0014', 'Nagoya', '000-0000-0014', '1990-03-15', 'Tester', 'user14@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User15', 'ユーザー15', '460-0015', 'Nagoya', '000-0000-0015', '1990-04-16', 'Tester', 'user15@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User16', 'ユーザー16', '460-0016', 'Nagoya', '000-0000-0016', '1990-05-17', 'Tester', 'user16@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User17', 'ユーザー17', '460-0017', 'Nagoya', '000-0000-0017', '1990-06-18', 'Tester', 'user17@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User18', 'ユーザー18', '460-0018', 'Nagoya', '000-0000-0018', '1990-07-19', 'Tester', 'user18@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User19', 'ユーザー19', '460-0019', 'Nagoya', '000-0000-0019', '1990-08-20', 'Tester', 'user19@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User20', 'ユーザー20', '460-0020', 'Nagoya', '000-0000-0020', '1990-09-21', 'Tester', 'user20@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User21', 'ユーザー21', '460-0021', 'Nagoya', '000-0000-0021', '1990-10-22', 'Tester', 'user21@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User22', 'ユーザー22', '460-0022', 'Nagoya', '000-0000-0022', '1990-11-23', 'Tester', 'user22@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User23', 'ユーザー23', '460-0023', 'Nagoya', '000-0000-0023', '1990-12-24', 'Tester', 'user23@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('User24', 'ユーザー24', '460-0024', 'Nagoya', '000-0000-0024', '1990-01-25', 'Tester', 'user24@example.com', '{noop}password', 1, TRUE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO verification_tokens (user_id, created_at, updated_at) VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO categories (name) VALUES
('Ramen'),
('Cafe'),
('Local');

INSERT INTO restaurants (name, image, description, lowest_price, highest_price, postal_code, address, opening_time, closing_time, seating_capacity, created_at, updated_at) VALUES
('Yabaton', 'yabaton.jpg', 'Famous miso katsu restaurant', 800, 2000, '460-0011', 'Nagoya, Sakae', '11:00:00', '22:00:00', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Komeda Coffee', 'komeda.jpg', 'Popular coffee shop', 400, 1000, '460-0012', 'Nagoya, Fushimi', '07:00:00', '23:00:00', 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant3', 'restaurant3.jpg', 'Description for restaurant 3', 530, 1060, '460-0203', 'Nagoya Address 3', '09:00:00', '21:00:00', 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant4', 'restaurant4.jpg', 'Description for restaurant 4', 540, 1080, '460-0204', 'Nagoya Address 4', '09:00:00', '21:00:00', 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant5', 'restaurant5.jpg', 'Description for restaurant 5', 550, 1100, '460-0205', 'Nagoya Address 5', '09:00:00', '21:00:00', 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant6', 'restaurant6.jpg', 'Description for restaurant 6', 560, 1120, '460-0206', 'Nagoya Address 6', '09:00:00', '21:00:00', 36, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant7', 'restaurant7.jpg', 'Description for restaurant 7', 570, 1140, '460-0207', 'Nagoya Address 7', '09:00:00', '21:00:00', 37, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant8', 'restaurant8.jpg', 'Description for restaurant 8', 580, 1160, '460-0208', 'Nagoya Address 8', '09:00:00', '21:00:00', 38, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant9', 'restaurant9.jpg', 'Description for restaurant 9', 590, 1180, '460-0209', 'Nagoya Address 9', '09:00:00', '21:00:00', 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant10', 'restaurant10.jpg', 'Description for restaurant 10', 600, 1200, '460-0210', 'Nagoya Address 10', '09:00:00', '21:00:00', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant11', 'restaurant11.jpg', 'Description for restaurant 11', 610, 1220, '460-0211', 'Nagoya Address 11', '09:00:00', '21:00:00', 41, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant12', 'restaurant12.jpg', 'Description for restaurant 12', 620, 1240, '460-0212', 'Nagoya Address 12', '09:00:00', '21:00:00', 42, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant13', 'restaurant13.jpg', 'Description for restaurant 13', 630, 1260, '460-0213', 'Nagoya Address 13', '09:00:00', '21:00:00', 43, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant14', 'restaurant14.jpg', 'Description for restaurant 14', 640, 1280, '460-0214', 'Nagoya Address 14', '09:00:00', '21:00:00', 44, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant15', 'restaurant15.jpg', 'Description for restaurant 15', 650, 1300, '460-0215', 'Nagoya Address 15', '09:00:00', '21:00:00', 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant16', 'restaurant16.jpg', 'Description for restaurant 16', 660, 1320, '460-0216', 'Nagoya Address 16', '09:00:00', '21:00:00', 46, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant17', 'restaurant17.jpg', 'Description for restaurant 17', 670, 1340, '460-0217', 'Nagoya Address 17', '09:00:00', '21:00:00', 47, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant18', 'restaurant18.jpg', 'Description for restaurant 18', 680, 1360, '460-0218', 'Nagoya Address 18', '09:00:00', '21:00:00', 48, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant19', 'restaurant19.jpg', 'Description for restaurant 19', 690, 1380, '460-0219', 'Nagoya Address 19', '09:00:00', '21:00:00', 49, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant20', 'restaurant20.jpg', 'Description for restaurant 20', 700, 1400, '460-0220', 'Nagoya Address 20', '09:00:00', '21:00:00', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant21', 'restaurant21.jpg', 'Description for restaurant 21', 710, 1420, '460-0221', 'Nagoya Address 21', '09:00:00', '21:00:00', 51, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant22', 'restaurant22.jpg', 'Description for restaurant 22', 720, 1440, '460-0222', 'Nagoya Address 22', '09:00:00', '21:00:00', 52, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant23', 'restaurant23.jpg', 'Description for restaurant 23', 730, 1460, '460-0223', 'Nagoya Address 23', '09:00:00', '21:00:00', 53, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Restaurant24', 'restaurant24.jpg', 'Description for restaurant 24', 740, 1480, '460-0224', 'Nagoya Address 24', '09:00:00', '21:00:00', 54, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category_restaurant (restaurant_id, category_id, created_at, updated_at) VALUES
(1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reviews (content, score, restaurant_id, user_id, created_at, updated_at) VALUES
('Great miso katsu!', 5, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Loved the morning set.', 4, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservations (reserved_datetime, number_of_people, restaurant_id, user_id, created_at, updated_at) VALUES
('2024-08-01 18:00:00', 2, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-02 18:00:00', 2, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-03 18:00:00', 2, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-04 18:00:00', 2, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-05 18:00:00', 2, 5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-06 18:00:00', 2, 6, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-07 18:00:00', 2, 7, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-08 18:00:00', 2, 8, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-09 18:00:00', 2, 9, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-10 18:00:00', 2, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-11 18:00:00', 2, 11, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-12 18:00:00', 2, 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-01 18:00:00', 3, 13, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-02 18:00:00', 3, 14, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-03 18:00:00', 3, 15, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-04 18:00:00', 3, 16, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-05 18:00:00', 3, 17, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-06 18:00:00', 3, 18, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-07 18:00:00', 3, 19, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-08 18:00:00', 3, 20, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-09 18:00:00', 3, 21, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-10 18:00:00', 3, 22, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-11 18:00:00', 3, 23, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-12 18:00:00', 3, 24, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-01 18:00:00', 4, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-02 18:00:00', 4, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-03 18:00:00', 4, 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-04 18:00:00', 4, 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-05 18:00:00', 4, 5, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-06 18:00:00', 4, 6, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-07 18:00:00', 4, 7, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-08 18:00:00', 4, 8, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-09 18:00:00', 4, 9, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-10 18:00:00', 4, 10, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-11 18:00:00', 4, 11, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2024-08-12 18:00:00', 4, 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO favorites (restaurant_id, user_id, created_at, updated_at) VALUES
(2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO companies (name, postal_code, address, representative, capital, business, number_of_employees, created_at, updated_at) VALUES
('Nagoyameshi Inc.', '460-0001', 'Nagoya, Japan', 'Taro Manager', '10M JPY', 'Web Service', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
