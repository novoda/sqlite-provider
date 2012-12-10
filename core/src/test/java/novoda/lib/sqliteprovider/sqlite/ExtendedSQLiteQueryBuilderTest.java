
package novoda.lib.sqliteprovider.sqlite;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import novoda.lib.sqliteprovider.util.RoboRunner;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

@RunWith(RoboRunner.class)
public class ExtendedSQLiteQueryBuilderTest {

    @Mock
    SQLiteQueryBuilder qb;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInnerJoins() throws Exception {
        ExtendedSQLiteQueryBuilder builder = new ExtendedSQLiteQueryBuilder(qb);
        when(qb.getTables()).thenReturn("test");
        builder.addInnerJoin("groups");
        verify(qb).setTables("test LEFT JOIN groups ON test.group_id=groups._id");

        Mockito.reset(qb);
        when(qb.getTables()).thenReturn("test");
        builder.addInnerJoin("groups", "anothers");
        verify(qb)
        .setTables(
                "test LEFT JOIN groups ON test.group_id=groups._id LEFT JOIN anothers ON test.another_id=anothers._id");
    }

    @Test(expected = IllegalStateException.class)
    public void testShouldThrowAnExceptionIfTableNotSet() {
        ExtendedSQLiteQueryBuilder builder = new ExtendedSQLiteQueryBuilder(qb);
        builder.addInnerJoin("groups");
        verify(qb).setTables("test INNER JOIN groups ON test.group_id=groups._id");
    }

    @Test
    public void testUriParsing() throws Exception {
        Uri uri = Uri.parse("content://test.com/?q=1&q=2");
        List<String> p = uri.getQueryParameters("q");
        assertThat(p, hasItems("1", "2"));
    }
}
