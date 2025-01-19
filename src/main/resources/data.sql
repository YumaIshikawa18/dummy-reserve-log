-- camps
INSERT INTO camps (camp_id, name, address, map_link, price, capacity, description, picture)
VALUES
    ('11111111-1111-1111-1111-111111111111', '富士山キャンプ場', '静岡県富士宮市', 'https://maps.google.com/?q=35.3606,138.7274', 5000, 10, '富士山の絶景を楽しめる...', NULL),
    ('22222222-2222-2222-2222-222222222222', '琵琶湖キャンプ場', '滋賀県大津市', 'https://maps.google.com/?q=35.2010,135.9847', 4500, 15, '琵琶湖の湖畔にある...', NULL);

-- facilities
INSERT INTO facilities (facility_id, name, points)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'サウナ', 0),
    ('00000000-0000-0000-0000-000000000002', 'キッチン', 3),
    ('00000000-0000-0000-0000-000000000003', '給湯', 1),
    ('00000000-0000-0000-0000-000000000004', 'AC電源', 3),
    ('00000000-0000-0000-0000-000000000005', '売店', 3),
    ('00000000-0000-0000-0000-000000000006', 'シャワー', 3),
    ('00000000-0000-0000-0000-000000000007', '水洗トイレ', 10),
    ('00000000-0000-0000-0000-000000000008', '遊具', 1),
    ('00000000-0000-0000-0000-000000000009', 'コインランドリー', 2),
    ('00000000-0000-0000-0000-000000000010', 'FREEWifi', 1);

-- camp_facilities
INSERT INTO camp_facilities (camp_id, facility_id)
VALUES
    -- 11111111-1111-1111-1111-111111111111 -> facilities(2,3,4,5,6,7)
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000002'),
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000003'),
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000004'),
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000005'),
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000006'),
    ('11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000007'),

    -- 22222222-2222-2222-2222-222222222222 -> facilities(1,5,7,8)
    ('22222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000001'),
    ('22222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000005'),
    ('22222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000007'),
    ('22222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000008');

-- rentals
INSERT INTO rentals (rental_id, name, price_per_day)
VALUES
    ('11111111-0000-0000-0000-111111111111', 'テント', 1000),
    ('22222222-0000-0000-0000-222222222222', '寝袋', 500),
    ('33333333-0000-0000-0000-333333333333', 'キャンプチェア', 300),
    ('44444444-0000-0000-0000-444444444444', 'ランタン', 200);

-- camp_rentals
INSERT INTO camp_rentals (camp_id, rental_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-0000-0000-0000-111111111111'),
    ('11111111-1111-1111-1111-111111111111', '22222222-0000-0000-0000-222222222222'),
    ('22222222-2222-2222-2222-222222222222', '11111111-0000-0000-0000-111111111111'),
    ('22222222-2222-2222-2222-222222222222', '44444444-0000-0000-0000-444444444444');
