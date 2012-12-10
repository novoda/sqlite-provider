package novoda.lib.sqliteprovider.util;

import android.net.Uri;

public class UriInspector {

    public UriInspector() {
    }

    public UriToSqlAttributes parse(Uri uri) {
        return new UriToSqlAttributes(uri);
    }

}
