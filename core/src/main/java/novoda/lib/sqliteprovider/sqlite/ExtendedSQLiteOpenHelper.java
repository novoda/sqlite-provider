
package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import novoda.lib.sqliteprovider.migration.Migrations;
import novoda.lib.sqliteprovider.util.DBUtils;
import novoda.lib.sqliteprovider.util.Log;

import java.io.IOException;
import java.util.*;

// TODO caching?
public class ExtendedSQLiteOpenHelper extends SQLiteOpenHelper implements IDatabaseMetaInfo {

    private static final String MIGRATIONS_PATH = "migrations";

    private final Context context;

    /*
     * We cache the constrains.
     */
    private final Map<String, List<String>> constrains = new HashMap<String, List<String>>();

    public ExtendedSQLiteOpenHelper(Context context) throws IOException {
        this(context, context.getPackageName() + ".db", null, Migrations.getVersion(context.getAssets(), MIGRATIONS_PATH));
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
    public List<String> getUniqueConstrains(String table) {
        if (!constrains.containsKey(table)) {
            constrains.put(table, DBUtils.getUniqueConstrains(getReadableDatabase(), table));
        }
        return constrains.get(table);
    }

    public String getFirstConstrain(String table, ContentValues values) {
        List<String> localConstrains = getUniqueConstrains(table);
        if (localConstrains == null) {
            return null;
        }
        for (String c : localConstrains) {
            if (values.containsKey(c)) {
                return c;
            }
        }
        return null;
    }
}
