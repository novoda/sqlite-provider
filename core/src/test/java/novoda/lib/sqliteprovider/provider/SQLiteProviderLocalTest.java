package novoda.lib.sqliteprovider.provider;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteOpenHelper;
import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteQueryBuilder;
import novoda.lib.sqliteprovider.util.RoboRunner;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(RoboRunner.class)
public class SQLiteProviderLocalTest {

    private SQLiteProviderImpl provider;

    @Mock private SQLiteDatabase db;
    @Mock private ExtendedSQLiteQueryBuilder builder;
    @Mock private Cursor mockCursor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Robolectric.bindShadowClass(ShadowContentUris.class);

        stub(builder.query((SQLiteDatabase) anyObject(), (String[]) anyObject(), anyString(), (String[]) anyObject(), anyString(),
                anyString(), anyString(), anyString())).toReturn(mockCursor);
        stub(db.rawQuery(anyString(), (String[]) anyObject())).toReturn(mockCursor);

        provider = new SQLiteProviderImpl();
        provider.onCreate();
    }

    @Test
    public void testSelectTableFromUri() {
        // Simple directory listing against table test
        query("test.com/test");
        verify(builder).setTables("test");
    }

    @Test
    public void testSelectTableItemFromUri() {
        // Single item against table test
        query("test.com/test/1");
        verify(builder).setTables("test");
        verify(builder).appendWhere("_id=1");
    }

    @Test
    public void testSelectTableForChildren() {
        query("test.com/parent/1/children");
        verify(builder).setTables("children");
        verify(builder).appendWhere("parent_id='1'");
    }

    @Test
    public void testInsertAgainstCorrectTable() {
        when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(2L);
        ContentValues cv = new ContentValues();
        cv.put("column1", "2erverver");

        insert("test.com/table1", cv);
        insert("test.com/parent/1/child", cv);

        verify(db).insert(eq("table1"), anyString(), (ContentValues) anyObject());
        verify(db).insert(eq("child"), anyString(), (ContentValues) anyObject());
    }

    @Test
    public void testInsertAgainstOneToManyShouldInputCorrectParam() {
        when(db.insert(anyString(), anyString(), (ContentValues) anyObject())).thenReturn(2L);
        ContentValues values = new ContentValues();
        values.put("test", "test");
        values.put("parent_id", "1");

        insert("test.com/parent/1/children", values);

        verify(db).insert(eq("children"), anyString(), (ContentValues) anyObject());
    }

    @Test
    public void testDeleteFromSingleTable() {
        when(db.delete(anyString(), anyString(), (String[]) anyObject())).thenReturn(0);

        delete("test.com/parent", null, null);

        verify(db).delete(eq("parent"), anyString(), (String[]) anyObject());
    }

    @Test
    public void testUpdateFromSingleTable() {
        when(db.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(2);
        ContentValues values = new ContentValues();
        values.put("test", "test");

        update("test.com/parent", values, null, null);

        verify(db).update(eq("parent"), (ContentValues) anyObject(), anyString(), (String[]) anyObject());
    }

    @Test
    public void testUpdateFromSOneToMany() {
        when(db.update(anyString(), (ContentValues) anyObject(), anyString(), (String[]) anyObject())).thenReturn(2);
        ContentValues values = new ContentValues();
        values.put("test", "test");

        update("test.com/parent/1/child", values, null, null);

        verify(db).update(eq("child"), (ContentValues) anyObject(), anyString(), (String[]) anyObject());
    }

    @Test
    public void testDeleteFromOneToManyTable() {
        when(db.delete(anyString(), anyString(), (String[]) anyObject())).thenReturn(0);

        delete("test.com/parent/1/child", null, null);

        verify(db).delete(eq("child"), anyString(), (String[]) anyObject());
    }

    @Test
    public void testGroupByQuery() {
        query("test.com/table?groupBy=table");

        verify(builder).query((SQLiteDatabase) anyObject(), (String[]) anyObject(), anyString(), (String[]) anyObject(), eq("table"),
                anyString(), anyString(), anyString());
    }

    @Test
    public void testHavingQuery() {
        query("test.com/table?having=g");

        verify(builder).query((SQLiteDatabase) anyObject(), (String[]) anyObject(), anyString(), (String[]) anyObject(), anyString(),
                eq("g"), anyString(), anyString());
    }

    @Test
    public void testLimitQuery() {
        query("test.com/table?limit=100");

        verify(builder).query((SQLiteDatabase) anyObject(), (String[]) anyObject(), anyString(), (String[]) anyObject(), anyString(),
                anyString(), anyString(), eq("100"));
    }

    @Test
    public void testDistinct() {
        query("test.com/table?distinct=true");

        verify(builder).setDistinct(true);
    }

    @Test
    public void testNotDistinct() {
        query("test.com/table?distinct=false");

        verify(builder).setDistinct(false);
    }

    @Test
    public void testExpand() {
        when(builder.getTables()).thenReturn("table");
        Uri uri = Uri.parse("content://test.com/?q=1&q=2");
        List<String> p = uri.getQueryParameters("q");
        assertThat(p, hasItems("1", "2"));

        query("test.com/table?expand=childs");
        verify(builder).setTables("table");
        // verify(builder).setTables("table INNER JOIN childs ON table.child_id=childs._id");
    }

    @Implements(ContentUris.class)
    static class ShadowContentUris {
        @SuppressWarnings("unused")
        @Implementation
        public static Uri withAppendedId(Uri uri, long id) {
            return Uri.parse("content://test.com");
        }
    }

    private void update(String uri, ContentValues initialValues, String selection, String[] selectionArgs) {
        provider.update(Uri.parse("content://" + uri), initialValues, selection, selectionArgs);
    }

    private void delete(String uri, String where, String[] whereArgs) {
        provider.delete(Uri.parse("content://" + uri), where, whereArgs);
    }

    private void insert(String uri, ContentValues cv) {
        provider.insert(Uri.parse("content://" + uri), cv);
    }

    private void query(String uri) {
        provider.query(Uri.parse("content://" + uri), null, null, null, null);
    }

    public class SQLiteProviderImpl extends SQLiteContentProviderImpl {
        @Override
        protected SQLiteDatabase getReadableDatabase() {
            return db;
        }

        @Override
        protected SQLiteDatabase getWritableDatabase() {
            return db;
        }

        @Override
        protected ExtendedSQLiteQueryBuilder getSQLiteQueryBuilder() {
            return builder;
        }

        @Override
        protected SQLiteOpenHelper getDatabaseHelper(Context context) {
            try {
                return new ExtendedSQLiteOpenHelper(getContext()){
                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        // dont do a migrate
                    }

                    @Override
                    public synchronized SQLiteDatabase getReadableDatabase() {
                        return db;
                    }

                    @Override
                    public synchronized SQLiteDatabase getWritableDatabase() {
                        return db;
                    }
                };
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void notifyUriChange(Uri uri) {
        }
    }
}
