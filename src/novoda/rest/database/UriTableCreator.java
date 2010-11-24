
package novoda.rest.database;

import novoda.rest.parsers.Node;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriTableCreator implements SQLiteTableCreator {

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
            return new StringBuffer(pathSegments.get(pathSegments.size() - 3)).append("_id")
                    .toString();
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
        return "_id";
    }

    @Override
    public String getTableName() {
        return pathSegments.get(pathSegments.size() - 1);
    }

    /**
     * By default, we will delete all relationship on INSERT, UPDATE, DELETE as
     * we would expect the parent node to contain the one to many relationship
     * and reinsert the children into the database. This is a safe approach
     * rather then updating or appending values.
     */
    @Override
    public String[] getTriggers() {
        if (isOneToMany()) {
            return SQLiteUtil.getTriggers(getParentTableName(), getParentPrimaryKey(),
                    getTableName(), getParentColumnName());
        }
        return null;
    }

    @Override
    public String getParentPrimaryKey() {
        return "_id";
    }

    @Override
    public SQLiteType getType(String field) {
        if (field.equals("_id")) {
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
        if (getPrimaryKey() == null || getPrimaryKey().equals("_id")) {
            return true;
        }
        return false;
    }

    public void setUri(Uri uri) {
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

    public static SQLiteTableCreator fromNode(final Node<?> node) {
        if (node.getOptions().insertUri == null) {
            throw new IllegalStateException("can not create a table without a URI attach to a node");
        }
        return new UriTableCreator(node.getOptions().insertUri) {
            @Override
            public String[] getTableFields() {
                return node.getColumns();
            }
        };
    }

    public String createAlterStatement(Node<?> node) {
        throw new UnsupportedOperationException("not implemented");
    }

    // TODO?
    @Override
    public String[] getTableFields() {
        return new String[] {
            getPrimaryKey()
        };
    }
}
