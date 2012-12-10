package novoda.lib.sqliteprovider.util;

import static org.junit.Assert.*;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RoboRunner.class)
public class UriToSqlAttributesInspectorTest {

    private UriInspector uriInspector = new UriInspector();;
    private UriToSqlAttributes attrs = null;

    @Before
    public void setup() {
        uriInspector = new UriInspector();
    }

    @Test
    public void testUriInspectorCreationBehaviour() {
        attrs = uriInspector.parse(Uri.parse("content://test.com/item/1"));
        assertEquals("content://test.com/item/1", attrs.getUri().toString());
    }

    @Test
    public void testHasWhereClauseInQuery() {
        attrs = uriInspector.parse(Uri.parse("content://test.com/item/1"));
        assertFalse(attrs.hasWhereClauseInQuery());
        attrs = uriInspector.parse(Uri.parse("content://test.com/tableName?groupBy=col&having=value"));
        assertTrue(attrs.hasWhereClauseInQuery());
    }
}