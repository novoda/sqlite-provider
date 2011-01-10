
package novoda.lib.sqliteprovider.migration;

import static novoda.lib.sqliteprovider.util.Log.Migration.e;
import static novoda.lib.sqliteprovider.util.Log.Migration.i;
import static novoda.lib.sqliteprovider.util.Log.Migration.infoLoggingEnabled;
import novoda.lib.sqliteprovider.util.SQLFile;

import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Migrations {

    private SortedSet<String> migrations;

    private long startDate;

    public Migrations() {
        this(-1);
    }

    public Migrations(long startDate) {
        this.startDate = startDate;
        migrations = new TreeSet<String>(comparator);
    }

    public boolean add(String migration) {
        if (shouldInsert(migration)) {
            return migrations.add(migration);
        } else {
            return false;
        }
    }

    private boolean shouldInsert(String migration) {
        return extractDate(migration) > startDate;
    }

    private long extractDate(String migration) {
        try {
            return Long.parseLong(migration.split("_", 0)[0]);
        } catch (NumberFormatException e) {
            // Log
            return -1;
        }
    }

    public SortedSet<String> getMigrationsFiles() {
        return migrations;
    }

    /**
     * Comparator against filename: <date>_create.sql vs <date2>_create.sql will
     * compare date with date2
     */
    /* package */Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String file, String another) {
            return new Long(extractDate(file)).compareTo(new Long(extractDate(another)));
        }
    };

    public static void migrate(SQLiteDatabase db, AssetManager manager, String assetLocation)
            throws IOException {
        String[] sqls = manager.list(assetLocation);
        Migrations migrations = new Migrations(db.getVersion());
        Reader reader;

        for (String sqlfile : sqls) {
            migrations.add(sqlfile);
        }
        for (String sql : migrations.getMigrationsFiles()) {
            reader = new InputStreamReader(manager.open(assetLocation + File.pathSeparator + sql,
                    AssetManager.ACCESS_RANDOM));

            if (infoLoggingEnabled()) {
                i("executing SQL file: " + assetLocation + File.pathSeparator + sql);
            }
            try {
                db.beginTransaction();
                for (String insert : SQLFile.statementsFrom(reader)) {
                    db.execSQL(insert);
                }
                db.setTransactionSuccessful();
            } catch (SQLException exception) {
                e("error in migrate against file: " + sql);
            } finally {
                db.endTransaction();
            }
        }

        long v = migrations.extractDate(migrations.getMigrationsFiles().last());
        if (infoLoggingEnabled()) {
            i("setting version of DB to: " + v / 1000);
        }
        db.setVersion((int) v / 1000);
    }

    public static int getVersion(AssetManager assets, String migrationsPath) throws IOException {
        String[] sqls = assets.list(migrationsPath);
        Migrations migrations = new Migrations(-1);
        for (String sqlfile : sqls) {
            migrations.add(sqlfile);
        }
        int version = (int) (migrations.extractDate(migrations.getMigrationsFiles().last()) / 1000);
        if (infoLoggingEnabled()) {
            i("version of DB is version: " + version);
        }
        return version;
    }
}
