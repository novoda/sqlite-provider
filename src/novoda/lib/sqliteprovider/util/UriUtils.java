
package novoda.lib.sqliteprovider.util;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UriUtils {
    
    private Uri wrapped = null;
    
    private boolean isItem = false;
    
    private Map<String, String> mappedIds = new HashMap<String, String>();

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

    public static UriUtils from(Uri uri) {
        UriUtils utils = new UriUtils();
        utils.wrapped = uri;
        return utils;
    }

    public Map<String, String> getMappedIds() {
        return mappedIds;
    }
}
