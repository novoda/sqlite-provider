
package novoda.rest.database;

/**
 * Definition of the methods needed to create a table out of an object - will
 * mostly likely be a cursor.
 *
 * @author Carl-Gustaf Harroch
 */
public interface SQLiteTableCreator {

    /**
     * @return the primary key field within the cursor. If null, will use _id
     *         which auto increments
     */
    String getPrimaryKey();

    boolean shouldPKAutoIncrement();

    SQLiteType getType(final String field);

    boolean isNullAllowed(final String field);

    boolean isUnique(final String field);

    // Could all this be put in a SQLiteTableCreator?
    boolean isOneToMany();

    String getParentColumnName();

    SQLiteType getParentType();

    String getParentTableName();

    String getParentPrimaryKey();
    // End

    boolean shouldIndex(final String field);

    SQLiteConflictClause onConflict(final String field);

    String getTableName();

    String[] getTableFields();

    String[] getTriggers();
}
