CREATE TABLE 'shop'
    (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, postcode TEXT, UNIQUE (name, postcode) ON CONFLICT REPLACE);
INSERT INTO 'shop'
    (name, postcode) VALUES ("TheBigBang Shop", "L300RA");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Dragons Cave", "SK85NX");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Firework Corner", "BN14FT");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Asda", "L319BH");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Tesco", "L319BH");
INSERT INTO 'shop'
    (name, postcode) VALUES ("Asda", "SK85NX");

CREATE TABLE 'firework'
    (_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    color TEXT,
    ftype TEXT,
    noise TEXT,
    price REAL,
    shop_id INTEGER,
    FOREIGN KEY (shop_id) REFERENCES shop(_id));
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("The Big Bertha", "Red", "Rocket", "Bang", 10.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("The Bigger Bertha", "Red", "Rocket", "Bang", 10.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("The Biggest Bertha", "Red", "Rocket", "BangBang", 10.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 5.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 5.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 5.00, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Rotar Motor", "Green", "Cathrine Wheel", "Whizz", 7.50, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Howler", "White", "Screamer", "Eeek", 9.99, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Red Lorry", "Red", "Screamer", "Eeek", 9.99, 1);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Yellow Lorry", "White", "Screamer", "Eeek", 9.99, 2);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("RocketABC", "White", "Rocket", "Pop", 15.00, 2);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("RocketABC", "Blue", "Rocket", "Bang", 15.00, 2);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("RocketABC", "White", "Rocket", "Scream", 15.00, 3);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 1.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 1.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 1.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 1.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 2", "Green", "Rocket", "Bang", 1.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 3", "Green and Brown", "Fountain", "Fizz", 100.00, 4);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Tesco Value Fireworks", "Invisible", "Imaginary", "Silent", 1.00, 5);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 1.00, 6);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 2", "Green", "Rocket", "Bang", 1.00, 6);
INSERT INTO 'firework'
    (name, color, ftype, noise, price, shop_id)
        VALUES ("Asda Firework 3", "Green and Brown", "Fountain", "Fizz", 100.00, 6);
