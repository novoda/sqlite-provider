
package novoda.lib.sqliteprovider.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsing .sql files and get single statements suitable for insertion.
 */
public class SQLFile {

    private BufferedReader reader;

    private List<String> statements;

    private boolean inComment = false;

    public void parse(Reader in) throws IOException {
        reader = new BufferedReader(in);
        statements = new ArrayList<String>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("--"))
                continue;

            if (line.startsWith("/*")) {
                inComment = true;
                continue;
            }

            if (line.endsWith("*/") && inComment == true) {
                inComment = false;
                continue;
            }

            if (inComment == true)
                continue;

            statements.add(line);
        }
        reader.close();
    }

    public List<String> getStatements() {
        return statements;
    }

    public static List<String> statementsFrom(Reader reader) throws IOException {
        SQLFile file = new SQLFile();
        file.parse(reader);
        return file.getStatements();
    }
}
