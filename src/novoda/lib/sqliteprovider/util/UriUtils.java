
package novoda.lib.sqliteprovider.util;

import android.net.Uri;

import java.util.List;

public class UriUtils {

    public static boolean isItem(final Uri uri) {
        return isItem("", uri);
    }

    public static boolean isDir(final Uri uri) {
        return isDir("", uri);
    }

    public static boolean isItem(final String rootPath, final Uri uri) {
        final List<String> segments = uri.getPathSegments();
        if (rootPath != null && !rootPath.equals("")) {
            return (((segments.size() - rootPath.split("/").length + 1) % 2) == 1);
        } else {
            return ((segments.size() % 2) == 0);
        }
    };

    public static boolean isDir(final String rootPath, final Uri uri) {
        return !isItem(rootPath, uri);
    };

    public static String getTableName(final Uri uri) {
        return getTableName("", uri);
    }

    public static String getTableName(final String rootPath, final Uri uri) {
        final List<String> segments = uri.getPathSegments();
        if (isItem(rootPath, uri)) {
            return segments.get(segments.size() - 2);
        } else {
            return uri.getLastPathSegment();
        }
    }
}
