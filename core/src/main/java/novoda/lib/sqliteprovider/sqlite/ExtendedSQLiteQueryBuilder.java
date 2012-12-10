
package novoda.lib.sqliteprovider.sqlite;

import android.database.Cursor;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

public class ExtendedSQLiteQueryBuilder {

    private final SQLiteQueryBuilder delegate;

    public ExtendedSQLiteQueryBuilder() {
        delegate = new SQLiteQueryBuilder();
    }

    public ExtendedSQLiteQueryBuilder(SQLiteQueryBuilder d) {
        delegate = d;
    }

    /*
     * select * from parent inner join child1, child2 on parent.id=child1.id and parent.id=child2.id
     */
    public void addInnerJoin(String... children) {
        final String parent = delegate.getTables();
        if (parent == null || TextUtils.isEmpty(parent)) {
            throw new IllegalStateException("You need to call setTable prior to call addInnerJoin");
        }
        final StringBuilder table = new StringBuilder(parent);
        for (String c : children) {
            table.append(String.format(" LEFT JOIN %1$s ON %2$s.%3$s_id=%1$s._id", c, parent,
                    singularize(c)));
        }
        delegate.setTables(table.toString());
    }

    private String singularize(String c) {
        return (c.endsWith("s")) ? c.substring(0, c.length() - 1) : c;
    }

    public String getTables() {
        return delegate.getTables();
    }

    public void appendWhere(CharSequence inWhere) {
        delegate.appendWhere(inWhere);
    }

    public void appendWhereEscapeString(String inWhere) {
        delegate.appendWhereEscapeString(inWhere);
    }

    public Cursor query(SQLiteDatabase db, String[] projectionIn, String selection,
            String[] selectionArgs, String groupBy, String having, String sortOrder) {
        return delegate.query(db, projectionIn, selection, selectionArgs, groupBy, having,
                sortOrder);
    }

    public Cursor query(SQLiteDatabase db, String[] projectionIn, String selection,
            String[] selectionArgs, String groupBy, String having, String sortOrder, String limit) {
        return delegate.query(db, projectionIn, selection, selectionArgs, groupBy, having,
                sortOrder, limit);
    }

    public String buildQuery(String[] projectionIn, String selection, String[] selectionArgs,
            String groupBy, String having, String sortOrder, String limit) {

        return delegate.buildQuery(projectionIn, selection, selectionArgs, groupBy, having,
                sortOrder, limit);
    }

    public String buildUnionSubQuery(String typeDiscriminatorColumn, String[] unionColumns,
            Set<String> columnsPresentInTable, int computedColumnsOffset,
            String typeDiscriminatorValue, String selection, String[] selectionArgs,
            String groupBy, String having) {

        return delegate.buildUnionSubQuery(typeDiscriminatorColumn, unionColumns,
                columnsPresentInTable, computedColumnsOffset, typeDiscriminatorValue, selection,
                selectionArgs, groupBy, having);
    }

    public String buildUnionQuery(String[] subQueries, String sortOrder, String limit) {
        return delegate.buildUnionQuery(subQueries, sortOrder, limit);
    }

    public void setDistinct(boolean distinct) {
        delegate.setDistinct(distinct);
    }

    public void setTables(String inTables) {
        delegate.setTables(inTables);
    }

    public void setProjectionMap(Map<String, String> columnMap) {
        delegate.setProjectionMap(columnMap);
    }

    public void setCursorFactory(CursorFactory factory) {
        delegate.setCursorFactory(factory);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
