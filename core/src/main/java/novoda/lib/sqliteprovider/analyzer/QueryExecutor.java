
package novoda.lib.sqliteprovider.analyzer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class QueryExecutor {

    private final SQLiteDatabase database;

    QueryExecutor(SQLiteDatabase database) {
        this.database = database;
    }

    <T> List<T> getDataForQuery(Query<T> query) {
        final Cursor cursor = executeQuery(query.getSqlStatement());

        List<T> items = createResultListForCursor(cursor);

        while (cursor.moveToNext()) {
            final T item = query.parseRow(cursor);
            if (item != null) {
                items.add(item);
            }
        }

        cursor.close();

        return Collections.unmodifiableList(items);
    }

    private Cursor executeQuery(String query) {
        return database.rawQuery(query, null);
    }

    private <T> ArrayList<T> createResultListForCursor(Cursor cursor) {
        return new ArrayList<>(cursor.getCount());
    }

}
