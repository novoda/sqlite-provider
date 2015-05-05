package novoda.lib.sqliteprovider.util;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UriToSqlAttributesInspectorTest {

    private UriInspector uriInspector;

    private UriToSqlAttributes attrs;
    private Uri uri;

    @Before
    public void setup() {
        uriInspector = new UriInspector();
        uri = mock(Uri.class);
    }

    @Test
    public void testUriInspectorCreationBehaviour() {
        attrs = uriInspector.parse(uri);

        assertEquals(uri, attrs.getUri());
    }

    @Test
    public void testWhenHasWhereClauseInQueryThenResultTrue() {
        Uri uri = mock(Uri.class);
        when(uri.toString()).thenReturn("content://test.com/tableName?groupBy=col&having=value");
        attrs = uriInspector.parse(uri);

        assertTrue(attrs.hasWhereClauseInQuery());
    }

    @Test
    public void testWhenDoesNotHaveWhereClauseInQueryThenResultFalse() {
        Uri uri = mock(Uri.class);
        when(uri.toString()).thenReturn("content://test.com/item/1");
        attrs = uriInspector.parse(uri);

        assertFalse(attrs.hasWhereClauseInQuery());
    }
}
