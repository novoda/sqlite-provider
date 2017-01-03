package novoda.lib.sqliteprovider.util;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

import static android.database.DatabaseUtils.createDbFromSqlStatements;
import static android.test.MoreAsserts.assertContentsInAnyOrder;
import static android.test.MoreAsserts.assertEmpty;

public class DatabaseStructureTest extends AndroidTestCase {

    private static final String DB_NAME = "testing.db";
    private static final String CREATE_TABLES = "CREATE TABLE t1(id INTEGER, name TEXT, r REAL);\n";
    private static final String CREATE_2_TABLES = "CREATE TABLE T(id INTEGER);\nCREATE TABLE T2(id INTEGER);\n";
    private static final String CREATE_2_TABLES_WITH_FOREIGN_KEY = "CREATE TABLE t(id INTEGER);\nCREATE TABLE t2(id INTEGER, t_id INTEGER);\n";
    private static final String CREATE_TABLE_WITH_CONSTRAINT = "CREATE TABLE t(id INTEGER, const TEXT UNIQUE NOT NULL);";
    private static final String CREATE_TABLE_WITH_MULTI_COLUMN_CONSTRAINT = "CREATE TABLE t(id INTEGER, name TEXT, desc TEXT NOT NULL, UNIQUE(name, desc) ON CONFLICT REPLACE);";
    private static final String CREATE_TABLE_WITH_INTEGER_PRIMARY_KEY = "CREATE TABLE t(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";

    private DatabaseStructure databaseStructure;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingDelegatingContext(getContext(), "_test_"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for (String db : getContext().databaseList()) {
            getContext().deleteDatabase(db);
        }
    }

    public void testTables() {
        createDatabase(CREATE_2_TABLES);
        List<String> tables = databaseStructure.tables();
        assertContentsInAnyOrder(tables, "T", "T2");
    }

    public void testForeignTable() {
        createDatabase(CREATE_2_TABLES_WITH_FOREIGN_KEY);
        List<String> foreignTables = databaseStructure.foreignTables("t2");
        assertContentsInAnyOrder(foreignTables, "t");
    }

    public void testColumns() {
        createDatabase(CREATE_TABLES);
        Map<String, SQLiteType> columns = databaseStructure.columns("t1");
        assertContentsInAnyOrder(columns.keySet(), "id", "name", "r");
    }

    public void testProjectionMap() {
        createDatabase(CREATE_2_TABLES_WITH_FOREIGN_KEY);
        Map<String, String> projectionMap = databaseStructure.projectionMap("t", "t2");
        assertEquals("t.id AS t_id", projectionMap.get("t_id"));
        assertEquals("t2.id AS t2_id", projectionMap.get("t2_id"));
    }

    public void testUniqueConstraints() {
        createDatabase(CREATE_TABLE_WITH_CONSTRAINT);
        List<Constraint> constraints = databaseStructure.uniqueConstraints("t");
        assertContentsInAnyOrder(constraints, new Constraint(Collections.singletonList("const")));
    }

    public void testUniqueConstraintsForIntegerPrimaryKey() {
        createDatabase(CREATE_TABLE_WITH_INTEGER_PRIMARY_KEY);
        List<Constraint> constraints = databaseStructure.uniqueConstraints("t");
        assertContentsInAnyOrder(constraints, new Constraint(Collections.singletonList("_id")));
    }

    public void testMultiColumnUniqueConstraints() {
        createDatabase(CREATE_TABLE_WITH_MULTI_COLUMN_CONSTRAINT);
        List<Constraint> constraints = databaseStructure.uniqueConstraints("t");
        assertContentsInAnyOrder(constraints, new Constraint(Arrays.asList("name", "desc")));
    }

    public void testEmptyUniqueConstraints() {
        createDatabase(CREATE_TABLES);
        List<Constraint> constraints = databaseStructure.uniqueConstraints("t");
        assertEmpty(constraints);
    }

    private void createDatabase(String createSqlStatements) {
        createDbFromSqlStatements(getContext(), DB_NAME, 1, createSqlStatements);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        databaseStructure = new DatabaseStructure(db);
    }
}
