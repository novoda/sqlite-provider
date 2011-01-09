
package novoda.lib.sqliteprovider.sqlite;

import novoda.lib.sqliteprovider.migration.Migrations;
import novoda.lib.sqliteprovider.util.DBUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO caching?
public class ExtendedSQLiteOpenHelper2 extends SQLiteOpenHelper implements IDatabaseMetaInfo {

    private Migrations migration;

    private Context context;

    public ExtendedSQLiteOpenHelper2(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public Map<String, SQLiteType> getColumns(String table) {
        return null;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
