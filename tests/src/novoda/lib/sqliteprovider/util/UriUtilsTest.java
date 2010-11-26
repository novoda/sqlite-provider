
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import junit.framework.TestCase;

@RunWith(RobolectricTestRunner.class)
public class UriUtilsTest extends TestCase {

    @Test
    public void testItem() throws Exception {
        Uri uri = Uri.parse("content://test.com/item/1");
        assertTrue(UriUtils.isItem("", uri));
        assertEquals("item", UriUtils.getTableName("", uri));
        assertTrue(UriUtils.isItem(uri));
        assertEquals("item", UriUtils.getTableName(uri));
    }

    @Test
    public void testRootNotNull() throws Exception {
        Uri uri = Uri.parse("content://test.com/root/item/1");
        assertTrue(UriUtils.isItem("root", uri));
        assertEquals("item", UriUtils.getTableName("root", uri));
        uri = Uri.parse("content://test.com/root/root2/item/1");
        assertTrue(UriUtils.isItem("root/root2", uri));
        assertEquals("item", UriUtils.getTableName("root/root2", uri));
    }
}
