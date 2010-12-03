package novoda.lib.sqliteprovider.util;

import java.util.List;

import android.net.Uri;

public class UriToSqlAttributes {

	public Uri uri;
	public UriToSqlAttributes(Uri uri) {
		this.uri = uri;
	}
	
	public boolean hasWhereClauseInQuery() {
		if (uri.toString().split("\\?").length >=2){
			 return true;
		 }
		 return false;
	}
}
