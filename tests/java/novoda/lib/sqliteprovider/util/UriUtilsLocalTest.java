
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import java.util.Map;

import junit.framework.TestCase;

@RunWith(RobolectricTestRunner.class)
public class UriUtilsLocalTest extends TestCase {

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

    @Test
    public void testGettingIds() throws Exception {
        Uri uri = Uri.parse("content://test.com");
        Map<String, String> result = UriUtils.from(uri).getMappedIds();
        assertTrue(result.size() == 0);

        uri = Uri.parse("content://test.com/parent");
        result = UriUtils.from(uri).getMappedIds();
        assertTrue(result.size() == 0);
        
        uri = Uri.parse("content://test.com/parent/1");
        result = UriUtils.from(uri).getMappedIds();
        assertEquals(result.size(),1);
        assertTrue(result.containsKey("parent"));
        assertEquals("1", result.get("parent"));
        
        uri = Uri.parse("content://test.com/parent/1/child");
        result = UriUtils.from(uri).getMappedIds();
        assertTrue(result.size() == 1);
        assertTrue(result.containsKey("parent"));
        assertEquals("1", result.get("parent"));
        
        uri = Uri.parse("content://test.com/parent/1/child/2");
        result = UriUtils.from(uri).getMappedIds();
        assertTrue(result.size() == 2);
        assertTrue(result.containsKey("parent") && result.containsKey("child"));
        assertEquals("1", result.get("parent"));
        assertEquals("2", result.get("child"));

    }
}
