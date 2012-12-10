
package novoda.lib.sqliteprovider.util;

import android.content.ContentValues;
import android.net.Uri;

import java.util.Arrays;

public class Log {

    public static class Provider {
        public static final String TAG = "SQLiteProvider";

        public static final boolean infoLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.INFO);
        }

        public static final boolean debugLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.DEBUG);
        }

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
        }

        public static final boolean warningLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.WARN);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
        }

        public static final void i(String msg) {
            android.util.Log.i(TAG, msg);
        }

        public static final void d(String msg) {
            android.util.Log.d(TAG, msg);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable e) {
            android.util.Log.e(TAG, msg, e);
        }

        public static final void e(Throwable e) {
            android.util.Log.e(TAG, e.getClass().getSimpleName(), e);
        }

        public static final void i(String msg, Throwable e) {
            android.util.Log.e(TAG, msg, e);
        }

        public static final void w(String msg) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static class Migration {
        public static final String TAG = "SQLiteProvider-Mig";

        public static final boolean infoLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.INFO);
        }

        public static final boolean debugLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.DEBUG);
        }

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
        }

        public static final boolean warningLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.WARN);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
        }

        public static final void i(String msg) {
            android.util.Log.i(TAG, msg);
        }

        public static final void d(String msg) {
            android.util.Log.d(TAG, msg);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable e) {
            android.util.Log.e(TAG, msg, e);
        }

        public static final void e(Throwable e) {
            android.util.Log.e(TAG, e.getClass().getSimpleName(), e);
        }

        public static final void i(String msg, Throwable e) {
            android.util.Log.e(TAG, msg, e);
        }

        public static final void w(String msg) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static class ContentProvider {
        public enum CPType {
            INSERT, QUERY, DELETE, GET_TYPE, UPDATE
        }

        public static final void log(CPType type, String logTag, Uri uri, String[] projection,
                String selection, String[] selectionArgs, String sortOrder, ContentValues values) {

            StringBuilder builder = new StringBuilder("==== Content Provider Logger ====")
            .append('\n');
            builder.append(type.name()).append(" request received").append('\n');
            builder.append("Uri: ").append(uri.toString()).append('\n');

            if (projection != null) {
                builder.append("Projection: ").append(Arrays.toString(projection)).append('\n');
            }

            if (selection != null) {
                builder.append("selection: ").append(selection).append('\n');
            }

            if (selectionArgs != null) {
                builder.append("Selection Argument: ").append(Arrays.toString(selectionArgs))
                .append('\n');
            }

            if (sortOrder != null) {
                builder.append("Sort Order: ").append(sortOrder).append('\n');
            }

            if (values != null) {
                builder.append("content values:").append(values.toString()).append('\n');
            }
            builder.append("==== End Provider Logger ====").append('\n');
            android.util.Log.i(logTag, builder.toString());
        }
    }
}
