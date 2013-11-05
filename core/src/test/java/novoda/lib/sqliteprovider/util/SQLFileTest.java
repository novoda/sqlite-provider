package novoda.lib.sqliteprovider.util;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RoboRunner.class)
public class SQLFileTest extends AndroidTestCase {

    String one = "CREATE TABLE 'parent' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, latitude REAL, longitude REAL, createdAt INTEGER);";

    String two = "CREATE TABLE 'child' (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, parent_id INTEGER NOT NULL, FOREIGN KEY(parent_id) REFERENCES parent(_id));";

    @Test
    public void testSimpleSQLFile() throws Exception {
        // BufferedReader reader = new BufferedReader(new InputStreamReader(getClass()
        // .getResourceAsStream("../resources/create.sql")));
        // List<String> statements = SQLFile.statementsFrom(reader);
        // List<String> expected = new ArrayList<String>();
        // expected.add(one);
        // expected.add(two);
        // assertSameList(statements, expected);
        // assertNotNull(this.getContext().getAssets());
    }

    // private void assertSameList(List<String> first, List<String> second) {
    // if (first.size() != second.size())
    // fail("should have same size array, " + first.size() + " " + second.size());
    //
    // String[] t1 = first.toArray(new String[] {});
    // String[] t2 = second.toArray(new String[] {});
    // Arrays.sort(t1);
    // Arrays.sort(t2);
    // for (int i = 0; i < t1.length; i++) {
    // if (!t1[i].equals(t2[i])) {
    // fail("value not the same: " + t1[i] + " compared to: " + t2[i]);
    // }
    // }
    // assertTrue(true);
    // }

    @Test
    public void testReadMultilineCommand() throws IOException {
        List<String> strings = SQLFile.statementsFrom(new StringReader("CREATE TABLE 'testTable'\n" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT;"));
        assertEquals("CREATE TABLE 'testTable' _id INTEGER PRIMARY KEY AUTOINCREMENT;", strings.get(0));
    }

    @Test
    public void testMultiVsSingleLine() throws IOException {
        List<String> oneLiners = readStatements("one_line_statements.sql");
        List<String> multiLiners = readStatements("multi_line_statements.sql");
        assertEquals(oneLiners.size(), multiLiners.size());
        for (int index = 0; index < oneLiners.size(); index++) {
            assertEquals(oneLiners.get(index), multiLiners.get(index));
        }
    }

    private List<String> readStatements(String fileName) throws IOException {
        return SQLFile.statementsFrom(getSqlFileReader(fileName));
    }

    private InputStreamReader getSqlFileReader(String fileName) {
        return new InputStreamReader(getClass().getResourceAsStream("/assets/sql/" + fileName));
    }
}
