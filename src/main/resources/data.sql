INSERT INTO members
(id, email, nickname, profile_image_url, thumbnail_image_url, refresh_token)
VALUES (2370842997, 'dhdustnr0134@naver.com', '정현석',
        'http://k.kakaocdn.net/dn/wtMIN/btrII2nrJAv/KWEi4dNNGqeBYjzr0KZGK1/img_640x640.jpg',
        'http://k.kakaocdn.net/dn/wtMIN/btrII2nrJAv/KWEi4dNNGqeBYjzr0KZGK1/img_110x110.jpg',
        NULL),
       (2355841047, 'x_x_x@hanmail.net', '한승연',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg',
        NULL),
       (2355841022, 'jinjeon@test.com', '진전',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg',
        NULL),
       (2355841033, 'khan@test.com', '칸',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg',
        'http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg',
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJSZWZyZXNoLVRva2VuIiwiYXVkIjoiMjM1NTg0MTA0NyIsImlzcyI6ImxvdWllMXNlIiwiZXhwIjoxNjYwMDMyNTEwLCJpYXQiOjE2NTk0Mjc3MTAsIm1lbWJlcklkIjoyMzU1ODQxMDQ3fQ.WBlJBO8HwEFGTxHzA--TGw22lO5vivrt_2qjntdcU-s');

INSERT INTO share
(id, appointment_date_time, created_date_time, description, latitude, longitude, location,
 original_price, price, recruitment, title, type, writer_id)
VALUES (1, '2023-08-03-16-00', '2022-08-03-16-00', '떡볶이 쉐어 설명', 36.6576769, 128.3007637, '강남역',
        30000, 10000, 3, '강남역에서 떡볶이 먹을 사람 모집합니다.', 'DELIVERY', 2370842997),
       (2, '2023-07-03-16-00', '2022-07-03-16-00', '치킨 쉐어 설명', 37.3951627, 127.1117136, '판교역',
        28000, 7000, 4, '판교역에서 치킨 먹을 사람 모집합니다.', 'DELIVERY', 2355841047),
       (3, '2023-07-03-16-00', '2022-07-03-16-00', '양파 쉐어 설명', 37.5132612, 127.1001336, '잠실역', 9000,
        3000, 3, '잠실역에서 양파 구매할사람 모집합니다.', 'INGREDIENT', 2355841022),
       (4, '2023-06-03-16-00', '2022-07-03-16-00', '햄버거 쉐어 설명', 37.5132612, 127.1001336, '잠실역',
        24000, 8000, 3, '잠실역에서 햄버거 먹을 사람 모집합니다.', 'DELIVERY', 2355841033);

INSERT INTO share_image
    (id, share_id, image_url)
VALUES (1, 1,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg'),
       (2, 1,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.jpeg'),
       (3, 2,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/louie1se/%E1%84%8E%E1%85%B5%E1%84%8F%E1%85%B5%E1%86%AB1.jpeg'),
       (4, 2,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/louie1se/%E1%84%8E%E1%85%B5%E1%84%8F%E1%85%B5%E1%86%AB2.jpeg'),
       (5, 3,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%A3%E1%86%BC%E1%84%91%E1%85%A1.jpeg'),
       (6, 4,
        'https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%83%E1%85%A1%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%85%E1%85%A9%E1%84%83%E1%85%B3.jpeg');

INSERT INTO entry
    (id, member_id, share_id)
VALUES
    (1, 2370842997, 1),
    (2, 2355841047, 1),
    (3, 2355841022, 1),
    (4, 2355841047, 2),
    (5, 2355841022, 2),
    (6, 2370842997, 2);

INSERT INTO wish
    (id, member_id, share_id)
VALUES
    (1, 2355841047, 1),
    (2, 2370842997, 2),
    (3, 2355841047, 2);

INSERT INTO chat_room
    (id, share_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO chat_room_member
    (id, chat_room_id, member_id)
VALUES
    (1, 1, 2370842997),
    (2, 1, 2355841047),
    (3, 1, 2355841022),
    (4, 2, 2370842997),
    (5, 2, 2355841047),
    (6, 2, 2355841022);

INSERT INTO chat
    (id, contents, written_date_time, chat_room_id, writer_id)
VALUES
    (1, '내용1', '2022-07-03-16-00', 1, 2370842997),
    (2, '내용2', '2022-07-03-16-01', 1, 2355841047),
    (3, '내용3', '2022-07-03-16-02', 1, 2370842997),
    (4, '내용4', '2022-10-03-16-00', 1, 2355841047),
    (5, '내용1', '2022-07-03-16-00', 2, 2355841022),
    (6, '내용2', '2022-07-03-16-01', 2, 2355841047),
    (7, '내용3', '2022-07-03-16-02', 2, 2355841022),
    (8, '내용4', '2022-10-03-16-00', 2, 2355841047);

INSERT INTO chat_log
    (id, recent_read_datetime, chat_room_id, member_id)
VALUES
    (1, '2022-08-03-16-00', 1, 2370842997),
    (2, '2022-10-03-16-00', 1, 2355841047);

INSERT INTO notification
(id, created_date_time, type, member_id, share_id)
VALUES
    (1, '2022-07-03-16-00', 'ACTIVITY', 2355841047, 2),
    (2, '2022-07-03-16-00', 'KEYWORD', 2370842997, 1);

INSERT INTO activity_notification
    (activity_type, id, entry_member_id)
VALUES
    ('ENTRY', 1, 2370842997);

INSERT INTO keyword
    (id, contents, location, latitude, longitude, member_id)
VALUES
    (1, '떡볶이', '강남역', 37.498095, 127.027610, 2370842997);
