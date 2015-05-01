
package novoda.lib.sqliteprovider.util;

import android.content.Context;
import android.test.AndroidTestCase;

import org.robolectric.Robolectric;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;


@Implements(AndroidTestCase.class)
public class ShadowAndroidTestCase {

    @Implementation
    public final Context getContext() {
        return Robolectric.application.getApplicationContext();
    }
}
