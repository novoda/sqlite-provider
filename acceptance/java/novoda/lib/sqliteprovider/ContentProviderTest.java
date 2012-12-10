
package novoda.lib.sqliteprovider;

import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

public class ContentProviderTest extends AndroidTestCase {

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
}
