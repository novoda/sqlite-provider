
package novoda.lib.sqliteprovider.sqlite;

import java.util.List;
import java.util.Map;

import novoda.lib.sqliteprovider.util.Constraint;

public interface IDatabaseMetaInfo {



    public enum SQLiteType {
        NULL, INTEGER, REAL, TEXT, BLOB, NUMERIC;

        public static SQLiteType fromName(String columnType) {
            return valueOf(columnType.toUpperCase());
        }
    }
    Map<String, SQLiteType> getColumns(String table);

    List<String> getTables();

    List<String> getForeignTables(String table);

    List<Constraint> getUniqueConstraints(String table);

    Map<String, String> getProjectionMap(String parent, String... foreignTables);

    int getVersion();

    void setVersion(int version);
}
