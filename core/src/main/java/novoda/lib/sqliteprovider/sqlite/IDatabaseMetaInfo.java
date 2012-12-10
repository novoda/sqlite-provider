
package novoda.lib.sqliteprovider.sqlite;

import java.util.List;
import java.util.Map;

public interface IDatabaseMetaInfo {

    public enum SQLiteType {
        NULL, INTEGER, REAL, TEXT, BLOB, NUMERIC
    }

    Map<String, SQLiteType> getColumns(String table);

    List<String> getTables();

    List<String> getForeignTables(String table);

    List<String> getUniqueConstrains(String table);

    Map<String, String> getProjectionMap(String parent, String... foreignTables);

    int getVersion();

    void setVersion(int version);
}
