
package novoda.lib.sqliteprovider.sqlite;

import android.test.AndroidTestCase;

public class AssetHelperTest extends AndroidTestCase {

    public AssetHelperTest() {
        super();
    }

    public void testSomething() throws Exception {
        String[] strings = getContext().getAssets().list("sql");
        assertEquals(strings[0], "create.sql");
        getContext().getAssets().openFd("create.sql").getFileDescriptor();
    }
}
