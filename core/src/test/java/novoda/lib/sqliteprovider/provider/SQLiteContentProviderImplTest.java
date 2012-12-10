
package novoda.lib.sqliteprovider.provider;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SQLiteContentProviderImplTest {

    SQLiteContentProviderImplT provider;

    @Mock
    SQLiteDatabase db;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        provider = new SQLiteContentProviderImplT();
    }

    public void testInsertWithConflictShouldUpdateRatherThenInsert() throws Exception {
        //    provider.insertInTransaction(Uri.parse("content://test.com/test"), values)
    }

    public class SQLiteContentProviderImplT extends SQLiteContentProviderImpl {
        @Override
        protected SQLiteDatabase getWritableDatabase() {
            return db;
        }

    }
}
