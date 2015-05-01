package novoda.lib.sqliteprovider.util;


import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SQLFileTest {

    private static final String MISSING_LAST_SEMICOLON = "-- We ommit the trailing semicolon of the last statement to test whether the parser detects that.\n" +
            "CREATE TABLE 'testTable'\n" +
            "        _id INTEGER PRIMARY KEY AUTOINCREMENT;\n" +
            "CREATE TABLE 'second'\n" +
            "        _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "        name TEXT,\n" +
            "        description TEXT,\n" +
            "        latitude REAL,\n" +
            "        longitude REAL,\n" +
            "        createdAt INTEGER";

    private static final String MULTI_LINE = "-- This file should contain the same sql statements as one_line_statements.sql, only formatting and comments can differ.\n" +
            "CREATE TABLE 'testTable'\n" +
            "        _id INTEGER PRIMARY KEY AUTOINCREMENT;\n" +
            "CREATE TABLE 'second'\n" +
            "        _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "        -- the name\n" +
            "        name TEXT, -- now even this is a supported comment!!!\n" +
            "        -- a meaningful description\n" +
            "        description TEXT,--and this is working as well.\n" +
            "        -- geo-coordinates\n" +
            "        latitude REAL,\n" +
            "        longitude REAL,\n" +
            "        -- create timestamp\n" +
            "        createdAt INTEGER;";
    
    private static final String ONE_LINE = "-- This file should contain the same sql statements as multi_line_statements.sql, only formatting and comments can differ.\n" +
            "CREATE TABLE 'testTable' _id INTEGER PRIMARY KEY AUTOINCREMENT;\n" +
            "-- a comment\n" +
            "CREATE TABLE 'second' _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER;";

    @Test
    public void testReadMultiLineCommand() throws IOException {
        String firstLine = "CREATE TABLE 'testTable'";
        String secondLine = "_id INTEGER PRIMARY KEY AUTOINCREMENT;";
        String singleLineCommand = firstLine + " " + secondLine;
        String multiLineCommand = firstLine + "\n" + secondLine;
        List<String> statementsFromMultiline = SQLFile.statementsFrom(new StringReader(multiLineCommand));
        assertEquals(singleLineCommand, statementsFromMultiline.get(0));
    }

    @Test
    public void testSkippingTrailingComments() throws IOException {
        String rawLine = "CREATE TABLE 'testTable'";
        String comment = "-- adding some comment that should get stripped off";
        String nextLine = "_id INTEGER PRIMARY KEY AUTOINCREMENT;";
        String lineCommand = rawLine + comment + "\n" + nextLine;
        List<String> statements = SQLFile.statementsFrom(new StringReader(lineCommand));
        assertEquals(rawLine + " " + nextLine, statements.get(0));
    }

    @Test
    public void testMultiLineEqualsSingleLineStatements() throws IOException {
        List<String> oneLiners = readStatements(ONE_LINE);
        List<String> multiLiners = readStatements(MULTI_LINE);
        assertEquals(oneLiners, multiLiners);
    }

    @Test
    public void testMultiLineEqualsSingleLineCount() throws IOException {
        List<String> oneLiners = readStatements(ONE_LINE);
        List<String> multiLiners = readStatements(MULTI_LINE);
        assertEquals(oneLiners.size(), multiLiners.size());
    }

    @Test(expected = IOException.class)
    public void testIncompleteLastStatementDetection() throws IOException {
        readStatements(MISSING_LAST_SEMICOLON);
    }

    private List<String> readStatements(String sqlString) throws IOException {
        return SQLFile.statementsFrom(new StringReader(sqlString));
    }

}
