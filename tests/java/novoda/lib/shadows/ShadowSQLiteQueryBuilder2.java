package novoda.lib.shadows;

import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import android.database.sqlite.SQLiteQueryBuilder;

@Implements(SQLiteQueryBuilder.class)
public class ShadowSQLiteQueryBuilder2 {

    @Implementation
    public String getTables() {
      //  Robolectric.directlyOn(super).
        return "";
    }
    
    
}
