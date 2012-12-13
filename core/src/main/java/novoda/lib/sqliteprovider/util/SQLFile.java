
package novoda.lib.sqliteprovider.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsing .sql files and get single statements suitable for insertion.
 */
public class SQLFile {

    private List<String> statements;

    private boolean inComment = false;

    public void parse(Reader in) throws IOException {
        BufferedReader reader = new BufferedReader(in);
        statements = new ArrayList<String>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("--")) {
                continue;
            }

            if (line.startsWith("/*")) {
                inComment = true;
                continue;
            }

            if (line.endsWith("*/") && inComment) {
                inComment = false;
                continue;
            }

            if (inComment) {
                continue;
            }

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

    public static List<String> statementsFrom(File sqlfile) throws IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(sqlfile);
            SQLFile file = new SQLFile();
            file.parse(reader);
            return file.getStatements();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
