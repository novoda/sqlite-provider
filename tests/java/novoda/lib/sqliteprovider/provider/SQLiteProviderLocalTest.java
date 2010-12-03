
package novoda.lib.sqliteprovider.provider;

import static org.mockito.Mockito.verify;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import novoda.lib.sqliteprovider.util.RoboRunner;
import novoda.lib.sqliteprovider.util.ShadowSQLiteQueryBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

@RunWith(RoboRunner.class)
public class SQLiteProviderLocalTest {

    SQLiteProviderImpl provider;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        provider = new SQLiteProviderImpl();
    }

    @Mock
    SQLiteDatabase db;

    @Mock
    SQLiteQueryBuilder builder;

    @Test
    public void testSelectTableFromUri() throws Exception {
        // Simple directory listing against table test
        query("test.com/test");
        verify(builder).setTables("test");
        
Robolectric.bindShadowClass(ShadowContentUris.class);
    }
    
    @Implements(ContentUris.class)
    public class ShadowContentUris {

        @Implementation
        public void appendWhere(CharSequence inWhere) {
            System.out.println("TESTSGASD ");
        }
        
        @Implementation
        public void setTable(String inWhere) {
            System.out.println("TESTSGASD ");
        }

    }
    
    @Test
    public void testSelectTableItemFromUri() throws Exception {
        // Single item against table test
        query("test.com/test/1");
        verify(builder).setTables("test");
        verify(builder).appendWhere("_id=1");
    }


    @Test
    public void testSelectTableForChildren() throws Exception {
        query("test.com/parent/1/children");
        verify(builder).setTables("children");
        verify(builder).appendWhereEscapeString("parent_id=1");
    }

    private void query(String uri) {
        provider.query(Uri.parse("content://" + uri), null, null, null, null);
    }

    public class SQLiteProviderImpl extends SQLiteProvider {
        @Override
        protected SQLiteDatabase getReadableDatabase() {
            return db;
        }

//        @Override
//        SQLiteQueryBuilder getSQLiteQueryBuilder() {
//            return builder;
//        }
    }
}
