package novoda.rest.database;

public class Column {
	public String name;
	public SQLiteType type = SQLiteType.TEXT;
	public boolean unique = false;
	public boolean allowNull = false;
	public SQLiteConflictClause onConflict = SQLiteConflictClause.REPLACE;
}
