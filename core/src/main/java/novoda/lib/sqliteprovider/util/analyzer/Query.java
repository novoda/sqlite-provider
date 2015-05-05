package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;

interface Query<T> {

    String getSqlStatement();

    T parseRow(Cursor cursor);
}
