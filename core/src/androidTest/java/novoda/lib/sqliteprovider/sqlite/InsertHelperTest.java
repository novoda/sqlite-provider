package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import novoda.lib.sqliteprovider.provider.action.InsertHelper;

public class InsertHelperTest extends AndroidTestCase {

    private static final String A_NAME_VALUE = "a name";
    private static final String DIFFERENT_NAME_VALUE = "another name, not the same";
    private static final String A_DESCRIPTION_VALUE = "a description";
    private static final String DIFFERENT_DESCRIPTION_VALUE = "a different description";
    private static final String ID_COLUMN = "_id";

    private class ParentsColumns {
        static final String ID = ID_COLUMN;
        static final String NAME = "name";
        static final String DESCRIPTION = "description";
    }

    private class IntegerPrimaryKeyTableColumns {
        static final String ID = ID_COLUMN;
        static final String NAME = "name";
    }

    private static final String PARENTS_TABLE = "parents";
    private static final String INTEGER_PRIMARY_KEY_TABLE = "integer_primary_key_table";

    private static final String BASE_URI_STRING = "content://novoda.lib.sqliteprovider.test/";
    private static final Uri PARENTS_URI = Uri.parse(BASE_URI_STRING + PARENTS_TABLE);
    private static final Uri INTEGER_PRIMARY_KEY_TABLE_URI = Uri.parse(BASE_URI_STRING + INTEGER_PRIMARY_KEY_TABLE);

    private InsertHelper helper;
    private ExtendedSQLiteOpenHelper openHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        openHelper = new ExtendedSQLiteOpenHelper(getContext());
        helper = new InsertHelper(openHelper);

        clearParentsTable();
    }

    public void testInsertWithNoConflictShouldInsert() {
        helper.insert(PARENTS_URI, parentsContentValues(A_NAME_VALUE, A_DESCRIPTION_VALUE));

        onQueryOf(PARENTS_TABLE, new CursorOperations() {
            @Override
            public void doOperationsOn(Cursor cursor) {
                assertEquals(1, cursor.getCount());
                assertEquals(A_NAME_VALUE, stringFrom(cursor, ParentsColumns.NAME));
                assertEquals(A_DESCRIPTION_VALUE, stringFrom(cursor, ParentsColumns.DESCRIPTION));
            }
        });
    }

    public void testInsertWithUniqueColumnConflictShouldUpdate() {
        helper.insert(PARENTS_URI, parentsContentValues(A_NAME_VALUE, A_DESCRIPTION_VALUE));
        final int existingRowId = idOfFirstRowIn(queryOf(PARENTS_TABLE));

        helper.insert(PARENTS_URI, parentsContentValues(A_NAME_VALUE, DIFFERENT_DESCRIPTION_VALUE));

        onQueryOf(PARENTS_TABLE, new CursorOperations() {
            @Override
            public void doOperationsOn(Cursor cursor) {
                assertEquals(1, cursor.getCount());
                assertEquals(existingRowId, intFrom(cursor, ParentsColumns.ID));
                assertEquals(A_NAME_VALUE, stringFrom(cursor, ParentsColumns.NAME));
                assertEquals(DIFFERENT_DESCRIPTION_VALUE, stringFrom(cursor, ParentsColumns.DESCRIPTION));
            }
        });
    }

    public void testInsertWithIntegerPrimaryKeyConflictShouldUpdate() {
        helper.insert(INTEGER_PRIMARY_KEY_TABLE_URI, integerPrimaryKeyTableContentValues(null, A_NAME_VALUE));
        final int existingRowId = idOfFirstRowIn(queryOf(INTEGER_PRIMARY_KEY_TABLE));

        helper.insert(INTEGER_PRIMARY_KEY_TABLE_URI, integerPrimaryKeyTableContentValues(existingRowId, DIFFERENT_NAME_VALUE));

        onQueryOf(INTEGER_PRIMARY_KEY_TABLE, new CursorOperations() {
            @Override
            public void doOperationsOn(Cursor cursor) {
                assertEquals(1, cursor.getCount());
                assertEquals(existingRowId, intFrom(cursor, ParentsColumns.ID));
                assertEquals(DIFFERENT_NAME_VALUE, stringFrom(cursor, ParentsColumns.NAME));
            }
        });
    }

    private String stringFrom(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private int intFrom(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private int idOfFirstRowIn(Cursor cursor) {
        try {
            assertTrue("No rows in table!", cursor.getCount() > 0);
            cursor.moveToFirst();
            return intFrom(cursor, ID_COLUMN);
        } finally {
            cursor.close();
        }
    }

    private void clearParentsTable() {
        openHelper.getWritableDatabase().delete(PARENTS_TABLE, null, null);
    }

    private ContentValues parentsContentValues(String name, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ParentsColumns.NAME, name);
        contentValues.put(ParentsColumns.DESCRIPTION, description);
        return contentValues;
    }

    private ContentValues integerPrimaryKeyTableContentValues(Integer existingRowId, String name) {
        ContentValues contentValues = new ContentValues();
        if (existingRowId != null) {
            contentValues.put(IntegerPrimaryKeyTableColumns.ID, existingRowId);
        }
        contentValues.put(IntegerPrimaryKeyTableColumns.NAME, name);
        return contentValues;
    }

    private Cursor queryOf(String tableName) {
        return openHelper.getReadableDatabase().rawQuery("select * from " + tableName, null);
    }

    private void onQueryOf(String tableName, CursorOperations cursorOperations) {
        Cursor cursor = queryOf(tableName);
        cursor.moveToFirst();
        try {
            cursorOperations.doOperationsOn(cursor);
        } finally {
            cursor.close();
        }
    }

    private interface CursorOperations {
        void doOperationsOn(Cursor cursor);
    }
}
