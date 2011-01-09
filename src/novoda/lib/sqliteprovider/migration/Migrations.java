
package novoda.lib.sqliteprovider.migration;

import novoda.lib.sqliteprovider.util.SQLFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Migrations implements Iterable<String> {

    private SortedSet<File> migrations;

    private long startDate;

    public Migrations() {
        this(-1);
    }

    public Migrations(long startDate) {
        this.startDate = startDate;
        migrations = new TreeSet<File>(comparator);
    }

    public boolean add(File migration) {
        if (shouldInsert(migration)) {
            return migrations.add(migration);
        } else {
            return false;
        }
    }

    private boolean shouldInsert(File migration) {
        return extractDate(migration) > startDate;
    }

    private long extractDate(File migration) {
        try {
            return Long.parseLong(migration.getName().split("_", 0)[0]);
        } catch (NumberFormatException e) {
            // Log
            return -1;
        }
    }

    public SortedSet<File> getMigrationsFiles() {
        return migrations;
    }

    @Override
    public Iterator<String> iterator() {
        ArrayList<String> l = new ArrayList<String>();
        try {
            for (File file : migrations) {
                l.addAll(SQLFile.statementsFrom(file));
            }
            return l.iterator();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Comparator against filename: <date>_create.sql vs <date2>_create.sql will
     * compare date with date2
     */
    /* package */Comparator<File> comparator = new Comparator<File>() {
        @Override
        public int compare(File file, File another) {
            return new Long(extractDate(file)).compareTo(new Long(extractDate(another)));
        }
    };

}
