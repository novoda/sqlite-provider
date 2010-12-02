
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import android.content.Context;
import android.test.AndroidTestCase;

@Implements(AndroidTestCase.class)
public class ShadowAndroidTestCase {
    
    @Implementation
    public final Context getContext() {
        return Robolectric.application.getApplicationContext();
    }
    
    
}
