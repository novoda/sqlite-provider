package novoda.lib.sqliteprovider.util.analyzer;

import novoda.lib.sqliteprovider.sqlite.IDatabaseMetaInfo;

class Column {
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
}
