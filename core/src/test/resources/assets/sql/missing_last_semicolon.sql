-- We ommit the trailing semicolon of the last statement to test whether the parser detects that.
CREATE TABLE 'testTable'
        _id INTEGER PRIMARY KEY AUTOINCREMENT;
CREATE TABLE 'second'
        _id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        description TEXT,
        latitude REAL,
        longitude REAL,
        createdAt INTEGER