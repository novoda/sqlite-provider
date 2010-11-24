
package novoda.rest.database;


public class SQLiteUtil {

    private static final short DELETE = 0;

    private static final short INSERT = 1;

    private static final short UPDATE = 2;

    public static String[] getTriggers(final String parentTable, final String parentPKName,
            final String childTable, final String parentFKName) {
        return new String[] {
                getUpdateTrigger(parentTable, parentPKName, childTable, parentFKName),
                //getInsertTrigger(parentTable, parentPKName, childTable, parentFKName),
                getDeleteTrigger(parentTable, parentPKName, childTable, parentFKName)
        };
    }

    public static String getUpdateTrigger(final String parentTable, final String parentPKName,
            final String childTable, final String parentFKName) {
        return SQLiteUtil.getTrigger(UPDATE, parentTable, parentPKName, childTable, parentFKName);
    }

    public static String getInsertTrigger(final String parentTable, final String parentPKName,
            final String childTable, final String parentFKName) {
        return SQLiteUtil.getTrigger(INSERT, parentTable, parentPKName, childTable, parentFKName);
    }

    public static String getDeleteTrigger(final String parentTable, final String parentPKName,
            final String childTable, final String parentFKName) {
        return SQLiteUtil.getTrigger(DELETE, parentTable, parentPKName, childTable, parentFKName);
    }

    private static String getTrigger(final short type, final String parentTable,
            final String parentPKName, final String childTable, final String parentFKName) {
        StringBuffer buf = new StringBuffer();
        String typeString = null;
        buf.append("CREATE TRIGGER ");
        switch (type) {
            case DELETE:
                typeString = "DELETE";
                break;
            case INSERT:
                typeString = "INSERT";
                break;
            case UPDATE:
                typeString = "UPDATE";
                break;
            default:
                throw new IllegalStateException("type must be valid");
        }
        buf.append(typeString.toLowerCase()).append("_").append(parentTable);
        buf.append(" BEFORE ").append(typeString).append(" ON ").append(parentTable).append(
                " FOR EACH ROW BEGIN ").append("DELETE").append(" from ").append(childTable)
                .append(" WHERE ").append(parentFKName).append(" = OLD.").append(parentPKName)
                .append("; END;");
        return buf.toString();
    }
}
