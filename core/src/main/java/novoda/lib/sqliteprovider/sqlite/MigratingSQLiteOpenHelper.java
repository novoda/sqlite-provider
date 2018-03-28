
package novoda.lib.sqliteprovider.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.IOException;

import novoda.lib.sqliteprovider.migration.Migrations;
import novoda.lib.sqliteprovider.util.Log;

public class MigratingSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String MIGRATIONS_PATH = "migrations";

    private final Context context;

    public MigratingSQLiteOpenHelper(Context context) throws IOException {
        this(context, null);
    }

    public MigratingSQLiteOpenHelper(Context context, CursorFactory factory) throws IOException {
        this(context, context.getPackageName() + ".db", factory, Migrations.getVersion(context.getAssets(), MIGRATIONS_PATH));
    }

    public MigratingSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Migrations.migrate(db, context.getAssets(), MIGRATIONS_PATH);
        } catch (IOException e) {
            Log.Migration.e(e);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
