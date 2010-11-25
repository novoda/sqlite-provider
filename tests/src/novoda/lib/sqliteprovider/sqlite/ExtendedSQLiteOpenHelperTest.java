
package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;

public class ExtendedSQLiteOpenHelperTest extends AndroidTestCase {

    private ExtendedSQLiteOpenHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new ExtendedSQLiteOpenHelper(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetTableNames() throws Exception {
        helper.getWritableDatabase().execSQL("CREATE TABLE 'test'(id integer);");
        assertEquals("test", helper.getTables().get(0));
    }

    public void testSettingFKFromCreateStatement() throws Exception {
        insertOne2Many("parent", "child");
        ContentValues values = new ContentValues();
        values.put("parent_id", 3);
        try {
            helper.getWritableDatabase().insert("child", null, values);
            fail("should throw an error upon insert against a one2many relationship");
        } catch (SQLiteException e) {
            assertTrue(true);
        }
    }

    private void insertTables(String... tables) {
        for (String table : tables) {
            helper.getWritableDatabase().execSQL("CREATE TABLE '?'(_id INTEGER);", new String[] {
                table
            });
        }
    }

    private void insertOne2Many(String parentTable, String childTable) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE '" + parentTable + "'(_id INTEGER PRIMARY KEY);");
        db.execSQL("CREATE TABLE '" + childTable + "'(_id INTEGER PRIMARY KEY, " + parentTable
                + "_id INTEGER, FOREIGN KEY(" + parentTable + "_id ) REFERENCES " + parentTable
                + "(_id) );");
        helper.executeForeignKeyTrigger();
    }
}
