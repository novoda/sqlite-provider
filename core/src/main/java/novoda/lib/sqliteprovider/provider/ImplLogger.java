package novoda.lib.sqliteprovider.provider;

import static novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl.ID;

import android.net.Uri;

import novoda.lib.sqliteprovider.sqlite.ExtendedSQLiteQueryBuilder;
import novoda.lib.sqliteprovider.util.Log.Provider;
import novoda.lib.sqliteprovider.util.UriUtils;

import java.util.Arrays;
import java.util.Map;

public class ImplLogger {

	protected void logStart(Uri uri) {
		if (Provider.verboseLoggingEnabled()) {
			Provider.v("==================== start of query =======================");
			Provider.v("Uri: " + uri.toString());
		}
	}

	protected void logAppendWhere(Uri uri) {
		if (Provider.verboseLoggingEnabled()) {
			Provider.v("Appending to where clause: " + ID + "=" + uri.getLastPathSegment());
		}
	}

	protected void logAppendWhereOnParent(Uri uri, StringBuilder escapedWhere) {
		if (Provider.verboseLoggingEnabled()) {
			Provider.v("Appending to where clause: " + UriUtils.getParentColumnName(uri) + ID + "=" + escapedWhere.toString());
		}
	}

	protected void logEnd(String[] projection, String selection, String[] selectionArgs, String sortOrder,
								final ExtendedSQLiteQueryBuilder builder,  final String groupBy, final String having, 
																				final String limit, Map<String, String> autoproj) {
		if (Provider.verboseLoggingEnabled()) {
			Provider.v("table: " + builder.getTables());

			if (projection != null) {
				Provider.v("projection:" + Arrays.toString(projection));
			}
			if (selection != null) {
				Provider.v("selection: " + selection + " with arguments " + Arrays.toString(selectionArgs));
			}
			Provider.v("extra args: " + groupBy + " ,having: " + having + " ,sort order: " + sortOrder + " ,limit: " + limit);

			if (autoproj != null) {
				Provider.v("projectionAutomated: " + autoproj);
			}
			Provider.v("==================== end of query =======================");
		}
	}

}
