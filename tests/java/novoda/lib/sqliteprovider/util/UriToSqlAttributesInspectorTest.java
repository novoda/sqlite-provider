package novoda.lib.sqliteprovider.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import junit.framework.TestCase;

@RunWith(RobolectricTestRunner.class)
public class UriToSqlAttributesInspectorTest extends TestCase{
	
	private UriInspector uriInspector = new UriInspector();;
	private UriToSqlAttributes attrs = null;

	private void setup() {
		uriInspector = new UriInspector();
	}
	
    @Test
	public void testUriInspectorCreationBehaviour(){
		attrs = uriInspector.parse(Uri.parse("content://test.com/item/1"));
		assertEquals("content://test.com/item/1", attrs.uri.toString());
	}
    
    @Test
	public void testHasWhereClauseInQuery(){
    	attrs = uriInspector.parse(Uri.parse("content://test.com/item/1"));
    	assertFalse(attrs.hasWhereClauseInQuery());
    	
    	attrs = uriInspector.parse(Uri.parse("content://test.com/tableName?groupBy=col&having=value"));
    	assertTrue(attrs.hasWhereClauseInQuery());
    }
	
}