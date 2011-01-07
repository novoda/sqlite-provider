
package novoda.lib.sqliteprovider.sqlite;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.sqliteprovider.util.RoboRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.database.sqlite.SQLiteQueryBuilder;

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
        verify(qb).setTables("test INNER JOIN groups ON test.group_id=groups._id");

        Mockito.reset(qb);
        when(qb.getTables()).thenReturn("test");
        builder.addInnerJoin("groups", "another");
        verify(qb)
                .setTables(
                        "test INNER JOIN groups,another ON test.group_id=groups._id AND test.another_id=another._id");
    }

    @Test(expected = IllegalStateException.class)
    public void testShouldThrowAnExceptionIfTableNotSet() {
        ExtendedSQLiteQueryBuilder builder = new ExtendedSQLiteQueryBuilder(qb);
        builder.addInnerJoin("groups");
        verify(qb).setTables("test INNER JOIN groups ON test.group_id=groups._id");
    }
}
