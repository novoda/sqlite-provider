package novoda.lib.sqliteprovider.util;

import android.net.Uri;

public class UriToSqlAttributes {

    private final Uri uri;

    public UriToSqlAttributes(Uri uri) {
        this.uri = uri;
    }

    public boolean hasWhereClauseInQuery() {
        if (uri.toString().split("\\?").length >= 2) {
            return true;
        }
        return false;
    }

    protected Uri getUri() {
        return uri;
    }
}
