
package novoda.lib.sqliteprovider.util;

import android.net.Uri;

import junit.framework.TestCase;

public class UriUtilsTest extends TestCase {

    public void testItem() throws Exception {
        Uri uri = Uri.parse("content://test.com/item/1");
        assertTrue(UriUtils.isItem("", uri));
        assertEquals("item", UriUtils.getTableName("", uri));
        assertTrue(UriUtils.isItem(uri));
        assertEquals("item", UriUtils.getTableName(uri));
    }

    public void testRootNotNull() throws Exception {
        Uri uri = Uri.parse("content://test.com/root/item/1");
        assertTrue(UriUtils.isItem("root", uri));
        assertEquals("item", UriUtils.getTableName("root", uri));
        uri = Uri.parse("content://test.com/root/root2/item/1");
        assertTrue(UriUtils.isItem("root/root2", uri));
        assertEquals("item", UriUtils.getTableName("root/root2", uri));
    }
}
