
package novoda.lib.sqliteprovider.analyzer;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAnalyzer {

    private final QueryExecutor queryExecutor;

    public DatabaseAnalyzer(SQLiteDatabase database) {
        this(new QueryExecutor(database));
    }

    DatabaseAnalyzer(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public List<String> getForeignTables(final String table) {
        return executeQuery(new ForeignTablesQuery(table, getTableNames()));
    }

    /**
     * @return a list of tables
     */
    public List<String> getTableNames() {
        return executeQuery(new DatabaseTablesQuery());
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
            addProjection(projection, table, column.getName());
        }
    }

    private String addProjection(Map<String, String> projection, String table, String column) {
        return projection.put(table + "_" + column, table + "." + column + " AS " + table + "_" + column);
    }

    public List<Column> getColumns(final String table) {
        return executeQuery(new TableColumnsQuery(table));
    }

    /**
     * Gets the version of SQLite used by Android.
     *
     * @return the SQLite version
     */
    public String getSQLiteVersion() {
        List<String> data = executeQuery(new SqliteVersionQuery());
        return data.get(0);
    }

    public List<Constraint> getUniqueConstraints(final String table) {
        final List<String> constraintNames = executeQuery(new TableUniqueConstraintsQuery(table));

        List<Constraint> constraints = new ArrayList<>(constraintNames.size());
        for (String constraintName : constraintNames) {
            final List<String> indexColumns = getColumnsInIndex(constraintName);
            constraints.add(new Constraint(indexColumns));
        }
        return constraints;
    }

    private List<String> getColumnsInIndex(String indexName) {
        return executeQuery(new IndexColumnsQuery(indexName));
    }

    private <T> List<T> executeQuery(Query<T> query) {
        return queryExecutor.getDataForQuery(query);
    }

}
