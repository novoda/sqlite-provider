
package novoda.lib.sqliteprovider.sqlite;

import android.test.InstrumentationTestCase;

public class AssetHelperTest extends InstrumentationTestCase {

    public AssetHelperTest() {
        super();
    }

    public void testSomething() throws Exception {
        String[] strings = getInstrumentation().getContext().getAssets().list("sql");

        assertEquals(strings[0], "create.sql");
    }
}
