
package novoda.lib.sqliteprovider.util.analyzer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.util.Constraint;

public class DatabaseAnalyzer {

    private final SQLiteDatabase database;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this.database = database;
    }

    public List<String> getForeignTables(final String table) {
        return getDataForQuery(new ForeignTablesQuery(table, getTableNames()));
    }

    /**
     * @return a list of tables
     */
    public List<String> getTableNames() {
        return getDataForQuery(new DatabaseTablesQuery());
    }

    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        Map<String, String> projection = new HashMap<String, String>();
        projection.put("_id", parent + "._id AS _id");
        addProjectionForTable(projection, parent);

        for (String foreignTable : foreignTables) {
            addProjectionForTable(projection, foreignTable);
        }

        return Collections.unmodifiableMap(projection);
    }

    private void addProjectionForTable(Map<String, String> projection, String table) {
        for (Column column : getColumns(table)) {
            String columnName = column.getName();
            addProjection(projection, table, columnName);
        }
    }

    private String addProjection(Map<String, String> projection, String table, String column) {
        return projection.put(table + "_" + column, table + "." + column + " AS " + table + "_" + column);
    }

    public List<Column> getColumns(final String table) {
        return getDataForQuery(new TableColumnsQuery(table));
    }

    /**
     * Gets the version of SQLite used by Android.
     *
     * @return the SQLite version
     */
    public String getSQLiteVersion() {
        List<String> data = getDataForQuery(new SqliteVersionQuery());
        return data.get(0);
    }

    public List<Constraint> getUniqueConstraints(final String table) {
        final List<String> constraintNames = getDataForQuery(new TableUniqueConstraintsQuery(table));

        List<Constraint> constraints = new ArrayList<>(constraintNames.size());
        for (String constraintName : constraintNames) {
            constraints.add(getConstraintFromIndex(constraintName));
        }
        return constraints;
    }

    private Constraint getConstraintFromIndex(String indexName) {
        List<String> columns = getDataForQuery(new IndexColumnsQuery(indexName));

        return new Constraint(columns);
    }

    private <T> List<T> getDataForQuery(Query<T> query) {
        final Cursor cursor = executeQuery(query.getSqlStatement());

        List<T> items = new ArrayList<>(cursor.getCount());

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

}
