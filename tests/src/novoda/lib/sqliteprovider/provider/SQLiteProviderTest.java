
package novoda.lib.sqliteprovider.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.test.ProviderTestCase2;

public class SQLiteProviderTest extends ProviderTestCase2<SQLiteProviderImpl> {

    public SQLiteProviderTest(Class<SQLiteProviderImpl> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public SQLiteProviderTest() {
        super(SQLiteProviderImpl.class, "novoda.lib.sqliteprovider.provider");
    }

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
    }

    public void testQueryShouldSelectTableFromURI() throws Exception {
        assertNotNull(getSingleTable().query(Uri.parse("content://uri/test"), null, null, null,
                null));
    }

    private ContentResolver getSingleTable() throws IllegalAccessException, InstantiationException {
        return newResolverWithContentProviderFromSql(getContext(), "test",
                SQLiteProviderImpl.class, "uri", "novoda.lib.sqliteprovider.db", 1,
                "CREATE TABLE test(_id integer);");
    }
}
