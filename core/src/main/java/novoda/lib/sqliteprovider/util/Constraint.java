package novoda.lib.sqliteprovider.util;

import java.util.List;

public class Constraint {
    private final List<String> columns;

    public Constraint(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }
}
