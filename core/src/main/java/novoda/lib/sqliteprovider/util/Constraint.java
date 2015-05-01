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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Constraint that = (Constraint) o;

        return columns.equals(that.columns);

    }

    @Override
    public int hashCode() {
        return columns.hashCode();
    }
}
