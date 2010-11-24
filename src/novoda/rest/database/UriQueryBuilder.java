
package novoda.rest.database;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriQueryBuilder extends SQLiteQueryBuilder {

    private List<String> pathSegments;

    private Uri uri;

    public UriQueryBuilder(final Uri uri) {
        this.setUri(uri);
    }

    public static SQLiteQueryBuilder fromUri(final Uri uri) {
        UriQueryBuilder builder = new UriQueryBuilder(uri);
        return builder;
    }

    public String getTable() {
        if (isDirectory())
            return pathSegments.get(pathSegments.size() - 1);
        else
            return pathSegments.get(pathSegments.size() - 2);
    }

    public boolean isDirectory() {
        return (pathSegments.size() % 2 == 1);
    }

    public boolean isOneToMany() {
        return (pathSegments.size() > 2);
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        init();
    }

    private void init() {
        pathSegments = new ArrayList<String>(Arrays.asList(uri.getPath().split("/")));
        pathSegments.remove(0);
        setTables(getTable());
        // appendWhereEscapeString(getWhere());
    }

    public String getWhere() {
        StringBuffer buf = new StringBuffer();
        if (!isDirectory()) {
            buf.append(getPKColumn()).append("=").append(pathSegments.get(pathSegments.size() - 1));
        }
        if (isOneToMany()) {
            if (!isDirectory()) {
                buf.append(" AND ");
            }
            buf.append(getFKColumn()).append("=").append(pathSegments.get(pathSegments.size() - 3));
        }
        return buf.toString();
    }

    private String getPKColumn() {
        return "_id";
    }

    private String getFKColumn() {
        return pathSegments.get(pathSegments.size() - 4) + "_id";
    }

    public Uri getUri() {
        return uri;
    }

    
}
