package novoda.lib.sqliteprovider.util;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;
import android.test.RenamingDelegatingContext;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

import static android.database.DatabaseUtils.createDbFromSqlStatements;

public class DatabaseAnalyzerTest extends AndroidTestCase {

    private static final String DB_NAME = "testing.db";
    private static final String CREATE_TABLES = "CREATE TABLE t1(id INTEGER, name TEXT, r REAL);\n";
    private static final String CREATE_2_TABLES = "CREATE TABLE T(id INTEGER);\nCREATE TABLE T2(id INTEGER);\n";
    private static final String CREATE_2_TABLES_WITH_FOREIGN_KEY = "CREATE TABLE t(id INTEGER);\nCREATE TABLE t2(id INTEGER, t_id INTEGER);\n";
    private static final String CREATE_TABLE_WITH_CONSTRAINT = "CREATE TABLE t(id INTEGER, const TEXT UNIQUE NOT NULL);";
    private static final String CREATE_TABLE_WITH_MULTI_COLUMN_CONSTRAINT = "CREATE TABLE t(id INTEGER, name TEXT, desc TEXT NOT NULL, UNIQUE(name, desc) ON CONFLICT REPLACE);";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingDelegatingContext(getContext(), "_test_"));
    }

    public void testGetTables() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_2_TABLES);

        List<String> tables = new DatabaseAnalyzer(db).getTables();

        MoreAsserts.assertContentsInAnyOrder(tables, "T", "T2");
    }

    public void testGetForeignKey() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_2_TABLES_WITH_FOREIGN_KEY);

        List<String> foreignTables = new DatabaseAnalyzer(db).getForeignTables("t2");

        MoreAsserts.assertContentsInAnyOrder(foreignTables, "t");
    }

    public void testGettingColumns() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_TABLES);

        Map<String, SQLiteType> ft = new DatabaseAnalyzer(db).getColumns("t1");

        MoreAsserts.assertContentsInAnyOrder(ft.keySet(), "id", "name", "r");
    }

    public void testGettingProjectionMap() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_2_TABLES_WITH_FOREIGN_KEY);

        Map<String, String> ft = new DatabaseAnalyzer(db).getProjectionMap("t", "t2");

        assertEquals("t.id AS t_id", ft.get("t_id"));
        assertEquals("t2.id AS t2_id", ft.get("t2_id"));
    }

    public void testGettingUniqueConstraints() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_TABLE_WITH_CONSTRAINT);

        List<Constraint> constrains = new DatabaseAnalyzer(db).getUniqueConstraints(db, "t");

        MoreAsserts.assertContentsInAnyOrder(constrains, new Constraint(Arrays.asList("const")));
    }

    public void testGettingMultiColumnUniqueConstraints() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_TABLE_WITH_MULTI_COLUMN_CONSTRAINT);

        List<Constraint> constrains = new DatabaseAnalyzer(db).getUniqueConstraints(db, "t");

        MoreAsserts.assertContentsInAnyOrder(constrains, new Constraint(Arrays.asList("name", "desc")));
    }

    public void testGettingUniqueConstraintsIsEmpty() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement(CREATE_TABLES);

        List<Constraint> uniqueConstraints = new DatabaseAnalyzer(db).getUniqueConstraints(db, "t");

        MoreAsserts.assertEmpty(uniqueConstraints);
    }

    public void testGettingVersion() throws Exception {
        SQLiteDatabase db = createDatabaseWithStatement("");

        String version = new DatabaseAnalyzer(db).getSQLiteVersion();

        assertFalse(TextUtils.isEmpty(version));
    }

    private SQLiteDatabase createDatabaseWithStatement(String statement) {
        createDbFromSqlStatements(getContext(), DB_NAME, 1, statement);
        return getContext().openOrCreateDatabase(DB_NAME, 0, null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteAllDatabases();
    }

    private void deleteAllDatabases() {
        for (String databaseName : getContext().databaseList()) {
            getContext().deleteDatabase(databaseName);
        }
    }
}
