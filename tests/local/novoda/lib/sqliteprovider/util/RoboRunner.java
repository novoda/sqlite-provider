
package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.runners.model.InitializationError;

import java.io.File;
import java.lang.reflect.Method;

public class RoboRunner extends RobolectricTestRunner {

    public RoboRunner(Class<?> testClass) throws InitializationError {
        super(testClass, new File("../.."));
    }

    @Override
    public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowAndroidTestCase.class);
        Robolectric.logMissingInvokedShadowMethods();
    }
}
