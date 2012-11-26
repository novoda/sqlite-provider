CREATE TABLE 'shops' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, postcode TEXT);
INSERT INTO 'shops' (name, postcode) VALUES ("TheBigBang Shop", "L300RA")
INSERT INTO 'shops' (name, postcode) VALUES ("Dragons Cave", "SK85NX")
INSERT INTO 'shops' (name, postcode) VALUES ("Firework Corner", "BN14FT")
INSERT INTO 'shops' (name, postcode) VALUES ("Asda", "L319BH")

CREATE TABLE 'fireworks' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, color TEXT, ftype TEXT, noise TEXT, shops_id INTEGER, FOREIGN KEY (shops_id) REFERENCES shops(_id));
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("The Big Bertha", "Red", "Rocket", "Bang", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("The Bigger Bertha", "Red", "Rocket", "Bang", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("The Biggest Bertha", "Red", "Rocket", "BangBang", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Rotar Motor", "Green", "Cathrine Wheel", "Whizz", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Howler", "White", "Screamer", "Eeek", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Red Lorry", "Red", "Screamer", "Eeek", 1);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Yellow Lorry", "White", "Screamer", "Eeek", 2);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("RocketABC", "White", "Rocket", "Pop", 2);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("RocketABC", "Blue", "Rocket", "Bang", 2);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("RocketABC", "White", "Rocket", "Scream", 3);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 4);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 4);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 4);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 4);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 2", "Green", "Rocket", "Bang", 4);
INSERT INTO 'fireworks' (name, color, ftype, noise, shops_id) VALUES ("Asda Firework 3", "Green and Brown", "Fountain", "Fizz", 4);