CREATE TABLE 'parents' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER);
CREATE TABLE 'childs' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, parent_id INTEGER NOT NULL, FOREIGN KEY(parent_id) REFERENCES parent(_id));
-- This table intentionally has no Foreign Keys, no UNIQUE fields, no indexes, apart from the implicit index from the INTEGER PRIMARY KEY.
CREATE TABLE 'integer_primary_key_table' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);
