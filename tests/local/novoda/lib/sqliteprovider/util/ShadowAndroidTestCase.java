
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

import android.content.Context;
import android.test.AndroidTestCase;

@Implements(AndroidTestCase.class)
public class ShadowAndroidTestCase {
    
    @Implementation
    public final Context getContext() {
        return Robolectric.application.getApplicationContext();
    }
}
