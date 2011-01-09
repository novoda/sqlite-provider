
package novoda.lib.sqliteprovider.migration;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import novoda.lib.sqliteprovider.util.RoboRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.io.File;

@RunWith(RoboRunner.class)
public class MigrationsTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldInsertOrNotFileCorrectly() {
        Migrations migration = new Migrations();
        assertTrue("a correct file name can be inserted", migration.add(new File("12_test.sql")));
        assertTrue("a correct file name can be inserted",
                migration.add(new File("121321313_test_teste.sql")));
        assertFalse("a file name without .sql can not be inserted",
                migration.add(new File("12_test")));
        assertFalse("a file name without timestamp can not be inserted",
                migration.add(new File("test.sql")));
        
        assertThat(migration.getMigrationsFiles().size(), equalTo(2));
    }

    @Test
    public void testOrderedOfImport() {
        Migrations migration = new Migrations();
        migration.add(new File("3_test.sql"));
        migration.add(new File("1_test.sql"));
        migration.add(new File("2_test.sql"));
        migration.add(new File("5_test.sql"));
        migration.add(new File("0_test.sql"));
        
        assertThat(migration.getMigrationsFiles().size(), equalTo(5));
        assertThat(migration.getMigrationsFiles().first().getName(), equalTo("0_test.sql"));
        assertThat(migration.getMigrationsFiles().last().getName(), equalTo("5_test.sql"));
    }
    
    @Test
    public void testStartDate() {
        Migrations migration = new Migrations(4);
        migration.add(new File("3_test.sql"));
        migration.add(new File("1_test.sql"));
        migration.add(new File("2_test.sql"));
        migration.add(new File("5_test.sql"));
        migration.add(new File("0_test.sql"));
        
        assertThat(migration.getMigrationsFiles().size(), equalTo(1));
        assertThat(migration.getMigrationsFiles().last().getName(), equalTo("5_test.sql"));
    }
}
