package novoda.lib.sqliteprovider.analyzer;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo;

public class Column {
    private final String name;
    private final IDatabaseMetaInfo.SQLiteType type;

    public Column(String name, IDatabaseMetaInfo.SQLiteType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public IDatabaseMetaInfo.SQLiteType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        return name.equals(column.name) && type == column.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
