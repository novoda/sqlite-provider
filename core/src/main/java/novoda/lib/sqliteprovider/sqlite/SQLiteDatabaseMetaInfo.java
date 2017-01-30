package novoda.lib.sqliteprovider.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.util.Constraint;
import novoda.lib.sqliteprovider.util.DBUtils;

public class SQLiteDatabaseMetaInfo implements DatabaseMetaInfo {

    private final Map<String, List<Constraint>> constraints = new HashMap<String, List<Constraint>>();
    private final SQLiteOpenHelper helper;

    public SQLiteDatabaseMetaInfo(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    @Override
    public Map<String, SQLiteType> getColumns(String table) {
        return DBUtils.getFields(helper.getReadableDatabase(), table);
    }

    @Override
    public List<String> getTables() {
        return DBUtils.getTables(helper.getReadableDatabase());
    }

    @Override
    public List<String> getForeignTables(String table) {
        return DBUtils.getForeignTables(helper.getReadableDatabase(), table);
    }

    @Override
    public int getVersion() {
        return helper.getReadableDatabase().getVersion();
    }

    @Override
    public void setVersion(int version) {
        helper.getWritableDatabase().setVersion(version);
    }


    @Override
    public Map<String, String> getProjectionMap(String parent, String... foreignTables) {
        return DBUtils.getProjectionMap(helper.getReadableDatabase(), parent, foreignTables);
    }

    @Override
    public List<Constraint> getUniqueConstraints(String table) {
        if (!constraints.containsKey(table)) {
            constraints.put(table, DBUtils.getUniqueConstraints(helper.getReadableDatabase(), table));
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
