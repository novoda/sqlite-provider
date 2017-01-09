
package novoda.lib.sqliteprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.io.IOException;

import novoda.lib.sqliteprovider.sqlite.MigratingSQLiteOpenHelper;

public class ContentProviderTest extends AndroidTestCase {

    private static final String TABLE_NAME = "test";
    private static final String COLUMN_PRIMARY_KEY = "column_primary_key";
    private static final String ANY_COLUMN = "any_column";

    public ContentProviderTest() {
        super();
    }

    public void testMapping() throws Exception {
        Cursor cursor = getContext().getContentResolver().query(
                Uri.parse("content://novoda.lib.sqliteprovider.test/childs?expand=parents"), null,
                null, null, null);
        assertTrue(cursor.getColumnIndexOrThrow("childs__id") > -1);
        assertTrue(cursor.getColumnIndexOrThrow("parents_name") > -1);
    }

    public void test_GivenATableWithData_When_UpdatingNonexistentRow_Then_NumberOfAffectedRowsShouldBeZero() throws Exception {
        givenATableWithData();

        ContentValues values = new ContentValues(1);
        values.put(ANY_COLUMN, 1);

        int numRows = new MigratingSQLiteOpenHelper(getContext())
                .getWritableDatabase()
                .update(TABLE_NAME, values, COLUMN_PRIMARY_KEY + "=?", new String[]{"2"});

        assertEquals(0, numRows);

        numRows = getContext().getContentResolver().update(
                Uri.parse("content://novoda.lib.sqliteprovider.test/" + TABLE_NAME),
                values,
                COLUMN_PRIMARY_KEY + "=?",
                new String[]{"2"});

        assertEquals(0, numRows);
    }

    private void givenATableWithData() throws IOException {
        MigratingSQLiteOpenHelper helper = new MigratingSQLiteOpenHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` (" +
                           "`" + COLUMN_PRIMARY_KEY + "` INTEGER PRIMARY KEY," +
                           "`" + ANY_COLUMN + "` INTEGER UNIQUE)");

        ContentValues values = new ContentValues(2);
        values.put(COLUMN_PRIMARY_KEY, 1);
        values.put(ANY_COLUMN, 1);
        helper.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    @Override
    protected void tearDown() throws Exception {
        MigratingSQLiteOpenHelper helper = new MigratingSQLiteOpenHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS `" + TABLE_NAME + "`");
    }
}
