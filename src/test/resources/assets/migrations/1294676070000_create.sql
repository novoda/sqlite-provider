CREATE TABLE 'parents' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER);
CREATE TABLE 'childs' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, parent_id INTEGER NOT NULL, FOREIGN KEY(parent_id) REFERENCES parent(_id));
