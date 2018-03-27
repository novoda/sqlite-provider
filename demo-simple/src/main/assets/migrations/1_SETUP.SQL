CREATE TABLE 'city'(
    city_id INTEGER PRIMARY KEY,
    name TEXT
);
CREATE TABLE 'shop'(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    postcode TEXT,
    city_id TEXT,
    FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE
);

INSERT INTO 'city'
    (city_id, name) VALUES (1, "London");
INSERT INTO 'city'
    (city_id, name) VALUES (2, "Brighton");
INSERT INTO 'shop'
    (name, postcode, city_id) VALUES ("TheBigBang Shop", "L300RA", 1);
INSERT INTO 'shop'
    (name, postcode, city_id) VALUES ("Dragons Cave", "SK85NX", 1);
INSERT INTO 'shop'
    (name, postcode, city_id) VALUES ("Firework Corner", "BN14FT", 2);
INSERT INTO 'shop'
    (name, postcode, city_id) VALUES ("Asda", "L319BH", 2);
