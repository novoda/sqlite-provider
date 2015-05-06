package novoda.lib.sqliteprovider.analyzer;

import android.database.Cursor;

interface Query<T> {

    String getSqlStatement();

    T parseRow(Cursor cursor);
}
