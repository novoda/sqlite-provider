package novoda.lib.sqliteprovider.util;

import java.util.List;

class Constraint {
    private final List<String> columns;

    private Constraint(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }
}
