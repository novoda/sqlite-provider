-- comments

/*
 * comments
 */
CREATE TABLE 'parent' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER);
CREATE TABLE 'child' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, parent_id INTEGER NOT NULL, FOREIGN KEY(parent_id) REFERENCES parent(_id));