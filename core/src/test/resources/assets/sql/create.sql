-- comments

/*
 * comments
 */

CREATE TABLE 'parent' (_id INTEGER PRIMARY KEY AUTO INCREMENT, name TEXT, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER);
CREATE TABLE 'child' (_id INTEGER PRIMARY KEY AUTO INCREMENT, name TEXT, parent_id INTEGER NOT NULL, FOREIGN KEY(parent_id) REFERENCES parent(_id));
FOR
	test;
END;