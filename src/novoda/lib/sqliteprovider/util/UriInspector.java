package novoda.lib.sqliteprovider.util;

import android.net.Uri;

public class UriInspector {

	public UriInspector() {
	}

	public UriToSqlAttributes parse(Uri uri) {
		UriToSqlAttributes attrs = new UriToSqlAttributes(uri);
		return attrs;
	}

}
