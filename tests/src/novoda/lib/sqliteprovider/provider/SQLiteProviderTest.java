
package novoda.lib.sqliteprovider.provider;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

@RunWith(RobolectricTestRunner.class)
public class SQLiteProviderTest {

    @Before
    public void init(){
       MockitoAnnotations.initMocks(this);
    }
    
    @Mock
    SQLiteDatabase db;

    @Test
    public void testRootNotNull() throws Exception {
        SQLiteProviderImpl o = new SQLiteProviderImpl();
        o.query(Uri.parse("content://test.com/test"), null, null, null, null);
        verify(db).query("test2", null, null, null, null, null, null);
    }

    public class SQLiteProviderImpl extends SQLiteProvider {
        @Override
        protected SQLiteDatabase getReadableDatabase() {
            return db;
        }
    }
}
