
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import android.database.sqlite.SQLiteQueryBuilder;

@Implements(SQLiteQueryBuilder.class)
public class ShadowSQLiteQueryBuilder {

    @Implementation
    public void appendWhere(CharSequence inWhere) {
        System.out.println("TESTSGASD ");
    }
    
    @Implementation
    public void setTable(String inWhere) {
        System.out.println("TESTSGASD ");
    }

}
