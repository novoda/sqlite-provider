
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;

public class RoboRunner extends RobolectricTestRunner {

    public RoboRunner(Class<?> testClass) throws InitializationError {
        super(testClass, new File("src/test/resources"));
    }

    @Override
    public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowDatabaseUtils.class);
        Robolectric.bindShadowClass(ShadowAndroidTestCase.class);
    }
}
