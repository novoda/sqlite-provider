CREATE TABLE 'shops' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, postcode TEXT);
INSERT INTO 'shops' (name, postcode) VALUES ("TheBigBang Shop", "L300RA")
INSERT INTO 'shops' (name, postcode) VALUES ("Dragons Cave", "SK85NX")

CREATE TABLE 'fireworks' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, color TEXT, type TEXT, noise TEXT, shop INTEGER, FOREIGN KEY (shop) REFERENCES shops(_id));
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("The Big Boy", "Red", "Rocket", "Bang", 1);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("Water Falls", "Blue", "Fountain", "Fizz", 1);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("Rotar Motor", "Green", "Cathrine Wheel", "Whizz", 1);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("The Dieing Ghost", "White", "Screamer", "Eeek", 1);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("Asda Firework 1", "Brown", "Rocket", "Pop", 2);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("Asda Firework 2", "Green", "Rocket", "Bang", 2);
INSERT INTO 'fireworks' (name, color, type, noise, shop) VALUES ("Asda Firework 3", "Green and Brown", "Fountain", "Fizz", 2);