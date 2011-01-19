
package novoda.lib.sqliteprovider.util;

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
}
