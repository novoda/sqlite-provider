package novoda.lib.sqliteprovider.cursor;

import android.database.AbstractCursor;

public class EmptyCursor extends AbstractCursor {

    @Override
    public String[] getColumnNames() {
        return new String[] {"_id"};
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getDouble(int column) {
        return 0;
    }

    @Override
    public float getFloat(int column) {
        return 0;
    }

    @Override
    public int getInt(int column) {
        return 0;
    }

    @Override
    public long getLong(int column) {
        return 0;
    }

    @Override
    public short getShort(int column) {
        return 0;
    }

    @Override
    public String getString(int column) {
        return null;
    }

    @Override
    public boolean isNull(int column) {
        return true;
    }
}
