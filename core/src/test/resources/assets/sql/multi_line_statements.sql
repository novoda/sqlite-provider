-- This file should contain the same sql statements as one_line_statements.sql, only formatting and comments can differ.
CREATE TABLE 'testTable'
        _id INTEGER PRIMARY KEY AUTOINCREMENT;
CREATE TABLE 'second'
        _id INTEGER PRIMARY KEY AUTOINCREMENT,
        -- the name
        name TEXT, -- now even this is a supported comment!!!
        -- a meaningful description
        description TEXT,--and this is working as well.
        -- geo-coordinates
        latitude REAL,
        longitude REAL,
        -- create timestamp
        createdAt INTEGER;