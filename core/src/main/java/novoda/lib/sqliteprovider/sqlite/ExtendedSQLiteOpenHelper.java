
package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.migration.Migrations;
import novoda.lib.sqliteprovider.util.Constraint;
import novoda.lib.sqliteprovider.util.DBUtils;
import novoda.lib.sqliteprovider.util.Log;

// TODO caching?
public class ExtendedSQLiteOpenHelper extends SQLiteOpenHelper implements IDatabaseMetaInfo {

    private static final String MIGRATIONS_PATH = "migrations";

    private final Context context;

    /*
     * We cache the constrains.
     */
    private final Map<String, List<Constraint>> constraints = new HashMap<>();

    public ExtendedSQLiteOpenHelper(Context context) throws IOException {
        this(context, null);
    }

    public ExtendedSQLiteOpenHelper(Context context, CursorFactory factory) throws IOException {
        this(context, context.getPackageName() + ".db", factory, Migrations.getVersion(context.getAssets(), MIGRATIONS_PATH));
    }

    public ExtendedSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public Map<String, SQLiteType> getColumns(String table) {
        return DBUtils.getFields(getReadableDatabase(), table);
    }

    @Override
    public List<String> getTables() {
        return DBUtils.getTables(getReadableDatabase());
    }

    @Override
    public List<String> getForeignTables(String table) {
        return DBUtils.getForeignTables(getReadableDatabase(), table);
    }

    @Override
    public int getVersion() {
        return getReadableDatabase().getVersion();
    }

    @Override
    public void setVersion(int version) {
        getWritableDatabase().setVersion(version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Migrations.migrate(db, context.getAssets(), MIGRATIONS_PATH);
        } catch (IOException e) {
            Log.Migration.e(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        return DBUtils.getProjectionMap(getReadableDatabase(), parent, foreignTables);
    }

    @Override
    public List<Constraint> getUniqueConstraints(String table) {
        if (!constraints.containsKey(table)) {
            constraints.put(table, DBUtils.getUniqueConstraints(getReadableDatabase(), table));
        }
        return constraints.get(table);
    }

    public Constraint getFirstConstraint(String table, ContentValues values) {
        List<Constraint> constraints = getUniqueConstraints(table);
        if (constraints == null) {
            return null;
        }
        for (Constraint constraint : constraints) {
            boolean isValidConstraint = true;
            for (String column : constraint.getColumns()) {
                if (!values.containsKey(column)) {
                    isValidConstraint = false;
                }
            }
            if (isValidConstraint) {
                return constraint;
            }
        }
        return null;
    }
}
