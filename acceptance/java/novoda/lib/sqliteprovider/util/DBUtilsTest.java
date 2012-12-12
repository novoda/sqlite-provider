package novoda.lib.sqliteprovider.util;

import android.database.sqlite.SQLiteDatabase;
import android.test.*;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo.SQLiteType;

import java.util.List;
import java.util.Map;

public class DBUtilsTest extends AndroidTestCase {

    private static final String DB_NAME = "testing.db";
    private static final String CREATE_TABLES = "CREATE TABLE t1(id INTEGER, name TEXT, r REAL);\n";
    private static final String CREATE_2_TABLES = "CREATE TABLE T(id INTEGER);\nCREATE TABLE T2(id INTEGER);\n";
    private static final String CREATE_2_TABLES_WITH_FOREIGN_KEY = "CREATE TABLE t(id INTEGER);\nCREATE TABLE t2(id INTEGER, t_id INTEGER);\n";
    private static final String CREATE_TABLE_WITH_CONSTRAIN = "CREATE TABLE t(id INTEGER, const TEXT UNIQUE NOT NULL);";

    @Override
    protected void setUp() throws Exception {
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

    public void testGetDBName() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_2_TABLES);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        List<String> tables = DBUtils.getTables(db);
        MoreAsserts.assertContentsInAnyOrder(tables, "T", "T2");
    }

    public void testGetForeignKey() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_2_TABLES_WITH_FOREIGN_KEY);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        List<String> ft = DBUtils.getForeignTables(db, "t2");
        MoreAsserts.assertContentsInAnyOrder(ft, "t");
    }

    public void testGettingFieldsMap() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_TABLES);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        Map<String, SQLiteType> ft = DBUtils.getFields(db, "t1");
        MoreAsserts.assertContentsInAnyOrder(ft.keySet(), "id", "name", "r");
    }

    public void testGettingProjectionMap() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_2_TABLES_WITH_FOREIGN_KEY);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        Map<String, String> ft = DBUtils.getProjectionMap(db, "t", "t2");
        assertEquals("t.id AS t_id", ft.get("t_id"));
        assertEquals("t2.id AS t2_id", ft.get("t2_id"));
    }

    public void testGettingUniqueConstrains() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_TABLE_WITH_CONSTRAIN);

        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        List<String> constrains = DBUtils.getUniqueConstrains(db, "t");
        MoreAsserts.assertContentsInAnyOrder(constrains, "const");
    }

    public void testGettingUniqueConstrainsIsEmpty() throws Exception {
        android.database.DatabaseUtils.createDbFromSqlStatements(getContext(), DB_NAME, 1, CREATE_TABLES);
        SQLiteDatabase db = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        List<String> constrains = DBUtils.getUniqueConstrains(db, "t");
        MoreAsserts.assertEmpty(constrains);
    }
}
