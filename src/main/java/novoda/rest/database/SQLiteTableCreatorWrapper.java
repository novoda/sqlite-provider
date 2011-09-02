
package novoda.rest.database;

public class SQLiteTableCreatorWrapper implements SQLiteTableCreator {

    private SQLiteTableCreator wrapped;

    public SQLiteTableCreatorWrapper(SQLiteTableCreator original) {
        this.wrapped = original;
    }

    @Override
    public String getParentColumnName() {
        return wrapped.getParentColumnName();
    }

    @Override
    public String getParentPrimaryKey() {
        return wrapped.getParentPrimaryKey();
    }

    @Override
    public String getParentTableName() {
        return wrapped.getParentTableName();
    }

    @Override
    public SQLiteType getParentType() {
        return wrapped.getParentType();
    }

    @Override
    public String getPrimaryKey() {
        return wrapped.getPrimaryKey();
    }

    @Override
    public String[] getTableFields() {
        String[] C = new String[wrapped.getTableFields().length + appendColumns.length];
        System.arraycopy(wrapped.getTableFields(), 0, C, 0, wrapped.getTableFields().length);
        System
                .arraycopy(appendColumns, 0, C, wrapped.getTableFields().length,
                        appendColumns.length);
        return C;
    }

    @Override
    public String getTableName() {
        return wrapped.getTableName();
    }

    @Override
    public String[] getTriggers() {
        return wrapped.getTriggers();
    }

    @Override
    public SQLiteType getType(String field) {
        return wrapped.getType(field);
    }

    @Override
    public boolean isNullAllowed(String field) {
        return wrapped.isNullAllowed(field);
    }

    @Override
    public boolean isOneToMany() {
        return wrapped.isOneToMany();
    }

    @Override
    public boolean isUnique(String field) {
        return wrapped.isUnique(field);
    }

    @Override
    public SQLiteConflictClause onConflict(String field) {
        return wrapped.onConflict(field);
    }

    @Override
    public boolean shouldIndex(String field) {
        return wrapped.shouldIndex(field);
    }

    @Override
    public boolean shouldPKAutoIncrement() {
        return wrapped.shouldPKAutoIncrement();
    }

    public void appendColumns(String[] columns) {
        this.appendColumns = columns;
    }

    private String[] appendColumns = new String[]{};
}
