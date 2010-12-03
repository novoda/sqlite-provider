package novoda.lib.sqliteprovider.util;

import com.xtremelabs.robolectric.ClassHandler;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricClassLoader;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import org.junit.runners.model.InitializationError;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.test.AndroidTestCase;

import java.lang.reflect.Method;

public class RoboRunner extends RobolectricTestRunner {

    public RoboRunner(Class<?> arg0, ClassHandler arg1, RobolectricClassLoader arg2, String arg3,
            String arg4) throws InitializationError {
        super(arg0, arg1, arg2, arg3, arg4);
    }

    public RoboRunner(Class<?> testClass, ClassHandler classHandler, String androidManifestPath,
            String resourceDirectory) throws InitializationError {
        super(testClass, classHandler, androidManifestPath, resourceDirectory);
    }

    public RoboRunner(Class<?> testClass, String androidManifestPath, String resourceDirectory)
            throws InitializationError {
        super(testClass, androidManifestPath, resourceDirectory);
    }

    public RoboRunner(Class<?> testClass, String androidProjectRoot) throws InitializationError {
        super(testClass, androidProjectRoot);
    }

    public RoboRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
    
    @Override public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowAndroidTestCase.class);
        Robolectric.bindShadowClass(ShadowSQLiteQueryBuilder.class);

    }

}
