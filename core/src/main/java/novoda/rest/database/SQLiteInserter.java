package novoda.rest.database;

public interface SQLiteInserter {

    short CONTINUE = 0;

    short BREAK = 1;

    short ROLLBACK = 2;

    int getCount();

    // Do I need?
    String getInsertStatement(String tableName);

    int getInsertIndex(String field);

    SQLiteType getType(String field);

    Object get(String field, int index);

    // DO I need?
    String[] getColumns();

    // FIXME not fully working yet
    short onFailure(int index);

    // TODO v2
    // SQLiteTransactionListener getListener();
}
