DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INTEGER NOT NULL,
    login TEXT NOT NULL,
    money_amount INTEGER NOT NULL,
    card_number TEXT NOT NULL,
    status TEXT NOT NULL
);
INSERT INTO users VALUES
        (1,     'admin',        100,            '4485090066857647',      'active'),
        (2,     'Sacha',        50,             '4485958225638951',      'active'),
        (7,     'Kostolom',     5000,           '4466521636468652',      'active'),
        (4,     'Misha',        5,              '4716077590081704',      'sleep'),
        (3,     'Vasia',        10,             '4539575849312650',      'active'),
        (5,     'Gosha',        4,              '4532882256150400',      'sleep'),
        (6,     'Nikita',       50,             '5263089117850002',      'sleep')
;
/* passwords: */
DROP TABLE IF EXISTS passwords;
CREATE TABLE passwords (
    id INTEGER NOT NULL,
    password TEXT NOT NULL
);
INSERT INTO passwords VALUES
        (1,     'ololo'),
        (2,     'Sacha010'),
        (3,     'Vasia0154'),
        (4,     'hahaha'),
        (5,     'Nevergonnagiveyouup'),
        (6,     'Sky010@cool'),
        (7,     'Cyberschool')
;