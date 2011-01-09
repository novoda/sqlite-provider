
package novoda.lib.sqliteprovider.util;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.List;

public class DBUtilsTest extends AndroidTestCase {

    private static final String CREATE_2_TABLES = "CREATE TABLE T(id INTEGER);\nCREATE TABLE T2(id INTEGER);\n";

    public DBUtilsTest() {
    }

    @Override
    protected void setUp() throws Exception {
        setContext(new RenamingDelegatingContext(getContext(), "_test_"));
        super.setUp();
    }

    public void testGetDBName() throws Exception {
        DatabaseUtils.createDbFromSqlStatements(getContext(), "testing.db", 1, CREATE_2_TABLES);
        SQLiteDatabase db = getContext().openOrCreateDatabase("testing.db", 0, null);
        //List<String> tables = DBUtils.
    }

}
