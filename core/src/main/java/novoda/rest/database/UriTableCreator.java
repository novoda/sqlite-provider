package novoda.rest.database;

import android.net.Uri;

import java.util.*;

public class UriTableCreator implements SQLiteTableCreator {

    private static final String COL_PRIMARY_KEY = "_id";

    private Uri uri;

    private List<String> pathSegments;

    protected UriTableCreator() {
    }

    protected UriTableCreator(final Uri uri) {
        this.setUri(uri);
    }

    @Override
    public String getParentColumnName() {
        if (isOneToMany()) {
            return new StringBuffer(pathSegments.get(pathSegments.size() - 3)).append(COL_PRIMARY_KEY).toString();
        }
        return null;
    }

    @Override
    public String getParentTableName() {
        if (isOneToMany()) {
            return pathSegments.get(pathSegments.size() - 3);
        }
        return null;
    }

    @Override
    public SQLiteType getParentType() {
        return SQLiteType.INTEGER;
    }

    @Override
    public String getPrimaryKey() {
        return COL_PRIMARY_KEY;
    }

    @Override
    public String getTableName() {
        return pathSegments.get(pathSegments.size() - 1);
    }

    /**
     * By default, we will delete all relationship on INSERT, UPDATE, DELETE
     * as we would expect the parent node to contain the one to many relationship and
     * reinsert the children into the database. This is a safe approach rather then updating or appending values.
     */
    @Override
    public String[] getTriggers() {
        if (isOneToMany()) {
            return SQLiteUtil.getTriggers(getParentTableName(), getParentPrimaryKey(), getTableName(), getParentColumnName());
        }
        return null;
    }

    @Override
    public String getParentPrimaryKey() {
        return COL_PRIMARY_KEY;
    }

    @Override
    public SQLiteType getType(String field) {
        if (field.equals(COL_PRIMARY_KEY)) {
            return SQLiteType.INTEGER;
        }
        return SQLiteType.TEXT;
    }

    @Override
    public boolean isNullAllowed(String field) {
        return true;
    }

    @Override
    public boolean isOneToMany() {
        return (pathSegments.size() > 2);
    }

    @Override
    public boolean isUnique(String field) {
        if (field.equals(getPrimaryKey())) {
            return true;
        }
        return false;
    }

    @Override
    public SQLiteConflictClause onConflict(String field) {
        return SQLiteConflictClause.REPLACE;
    }

    @Override
    public boolean shouldIndex(String field) {
        if (field.equals(getPrimaryKey())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldPKAutoIncrement() {
        if (getPrimaryKey() == null || getPrimaryKey().equals(COL_PRIMARY_KEY)) {
            return true;
        }
        return false;
    }

    public final void setUri(Uri uri) {
        // just to overcome an issue with # sign and getPath would only result
        // in the path after #
        this.uri = Uri.parse(uri.toString().replace('#', '1'));
        pathSegments = new ArrayList<String>(Arrays.asList(this.uri.getPath().split("/")));
        pathSegments.remove(0);
    }

    public Uri getUri() {
        return uri;
    }

    public static SQLiteTableCreator fromUri(final Uri uri) {
        UriTableCreator utc = new UriTableCreator(uri) {
        };
        return utc;
    }

    @Override
    public String[] getTableFields() {
        return new String[] {getPrimaryKey()};
    }
}
