
package novoda.lib.sqliteprovider.performance;

import novoda.lib.sqliteprovider.tests.R;
import novoda.lib.sqliteprovider.util.SQLFile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import java.io.InputStreamReader;
import java.util.List;

/*
 * Test case which will perform some tests against SQLite in order to define the best suited method for inserts/update/deletes etc...
 */
public class SQLitePerformance extends AndroidTestCase{

    private SQLiteDatabase db;

  //  private Intermediates mIntermediates;

    @Override
    protected void setUp() throws Exception {
        db = getContext().openOrCreateDatabase("performance.db", Context.MODE_PRIVATE, null);
        List<String> statements = SQLFile.statementsFrom(new InputStreamReader(getContext()
                .getResources().openRawResource(R.raw.create)));
        for (String statement : statements) {
            db.execSQL(statement);
        }
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        getContext().deleteDatabase("performance.db");
        super.tearDown();
    }

//    @Override
//    public boolean isPerformanceOnly() {
//        return true;
//    }
//
//    // This is never called
//    @Override
//    public int startPerformance(Intermediates intermediates) {
//        return 0;
//    }

    @LargeTest
    public void testWithoutPragmaSynchronous() throws Exception {
        List<String> statements = SQLFile.statementsFrom(new InputStreamReader(getContext()
                .getResources().openRawResource(R.raw.inserts_parent_no_tansaction_1000_rows)));
        for (String statement : statements) {
            db.execSQL(statement);
        }
    }
}
