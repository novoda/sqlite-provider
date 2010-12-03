
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
    public void testGenericItemWithinCollectionQuery() {
		final Uri uri = Uri.parse("content://test.com/item/1");
        assertTrue(UriUtils.isNumberedEntryWithinCollection(uri));
        assertEquals("item", UriUtils.getItemDirID(uri));
    }
    
    @Test
    public void testChangingRootOfQuery() {
        Uri uri = Uri.parse("content://test.com/root/item/1");
        assertTrue(UriUtils.isItem("root", uri));
        assertEquals("item", UriUtils.getItemDirID("root", uri));
        uri = Uri.parse("content://test.com/root/root2/item/1");
        assertTrue(UriUtils.isItem("root/root2", uri)); 	
        assertEquals("item", UriUtils.getItemDirID("root/root2", uri));
    }

    @Test
    public void testGettingRowIds() {
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
        
        uri = Uri.parse("content://test.com/parent/1/child/6");
        result = UriUtils.from(uri).getMappedIds();
        assertTrue(result.size() == 2);
        assertTrue(result.containsKey("parent") && result.containsKey("child"));
        assertEquals("1", result.get("parent"));
        assertEquals("6", result.get("child"));
    }
}
