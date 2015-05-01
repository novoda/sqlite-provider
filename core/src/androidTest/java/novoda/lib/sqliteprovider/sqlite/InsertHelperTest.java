package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import novoda.lib.sqliteprovider.provider.action.InsertHelper;

public class InsertHelperTest extends AndroidTestCase {

    private InsertHelper helper;
    private ExtendedSQLiteOpenHelper openHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        openHelper = new ExtendedSQLiteOpenHelper(getContext());
        helper = new InsertHelper(openHelper);
    }

    public void testInsertWithKeyShouldInsertThenUpdate() throws Exception {
        Uri uri = Uri.parse("content://novoda.lib.sqliteprovider.test/parents");
        ContentValues values = new ContentValues();
        values.put("name", "something unique");
        values.put("description", "something");
        helper.insert(uri, values);

        Cursor cursor = openHelper.getReadableDatabase().rawQuery("select * from parents", null);
        assertEquals(1, cursor.getCount());
        cursor.close();

        values.put("description", "else");
        helper.insert(uri, values);

        cursor = openHelper.getReadableDatabase().rawQuery("select * from parents", null);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        assertEquals("else", cursor.getString(cursor.getColumnIndex("description")));

    }
}
