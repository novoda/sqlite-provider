
package novoda.lib.sqliteprovider.provider;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
        protected SQLiteDatabase getWritableDatabase() {
            return db;
        }
        
    }
}
