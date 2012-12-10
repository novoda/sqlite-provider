package novoda.lib.sqliteprovider.util;

import android.database.DatabaseUtils;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

@Implements(DatabaseUtils.class)
public class ShadowDatabaseUtils extends com.xtremelabs.robolectric.shadows.ShadowDatabaseUtils {

    @Implementation
    public static void appendEscapedSQLString(StringBuilder sb, String sqlString){
        sb.append('\'');
        if (sqlString.indexOf('\'') != -1) {
            int length = sqlString.length();
            for (int i = 0; i < length; i++) {
                char c = sqlString.charAt(i);
                if (c == '\'') {
                    sb.append('\'');
                }
                sb.append(c);
            }
        } else
            sb.append(sqlString);
        sb.append('\'');
    }

}
