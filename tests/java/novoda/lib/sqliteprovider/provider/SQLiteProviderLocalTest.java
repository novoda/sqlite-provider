
package novoda.lib.sqliteprovider.provider;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

@RunWith(RobolectricTestRunner.class)
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
    
  @Test
    public void testInsertAgainstCorrectTable() throws Exception{    	
    	Robolectric.bindShadowClass(ShadowContentUris.class);    	
    	ContentValues cv = new ContentValues();
    	cv.put("column1", "2erverver");
    	when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(2L);
    	insert("test.com/table1",cv);
    	insert("test.com/parent/1/child", cv);
    	verify(db).insert(eq("table1"), anyString(), (ContentValues) anyObject());
    	verify(db).insert(eq("child"), anyString(), (ContentValues) anyObject());
    }
    
    @Test
    public void testInsertAgainstOneToManyShouldInputCorrectParam() throws Exception {
    	Robolectric.bindShadowClass(ShadowContentUris.class);
    	when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(2L);
    	ContentValues values = new ContentValues();
    	values.put("test", "test"); 
    	values.put("parent_id", "1"); 
    	insert("test.com/parent/1/children", values);
    	verify(db).insert(eq("children"), anyString(), (ContentValues) anyObject());
    }
    
    @Test
    public void testGroupByQuery() throws Exception {
    	query("test.com/table?orderBy=table");
    	//verify(builder).query(db, projectionIn, selection, selectionArgs, "table", having, sortOrder);
    	
    	// having
    }
    
    @Implements(ContentUris.class)
	static
    class ShadowContentUris {
    	@Implementation
    	public static Uri withAppendedId(Uri uri, long id) {
    		return Uri.parse("content://test.com");
    	}
    }
   
    
    private void insert(String uri, ContentValues cv){
    	provider.insert(Uri.parse("content://" + uri), cv);
    }
    private void query(String uri) {
        provider.query(Uri.parse("content://" + uri), null, null, null, null);
    }

    public class SQLiteProviderImpl extends SQLiteProvider {
        @Override
        protected SQLiteDatabase getReadableDatabase() {
            return db;
        }
        
        @Override
		public
        void notifyUriChange(Uri uri) {
        }
        
        @Override
        protected SQLiteDatabase getWritableDatabase() {
            return db;
        }

        @Override
        SQLiteQueryBuilder getSQLiteQueryBuilder() {
            return builder;
        }
    }
}
