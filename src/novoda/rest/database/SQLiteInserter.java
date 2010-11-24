
package novoda.rest.database;


public interface SQLiteInserter {

    public static short CONTINUE = 0;

    public static short BREAK = 1;

    public static short ROLLBACK = 2;

    public int getCount();

    // Do I need?
    public String getInsertStatement(String tableName);
    
    public int getInsertIndex(String field);

    public SQLiteType getType(String field);

    public Object get(String field, int index);

    // DO I need?
    public String[] getColumns();

    // FIXME not fully working yet
    public short onFailure(int index);

    // TODO v2
    // public SQLiteTransactionListener getListener();
}
