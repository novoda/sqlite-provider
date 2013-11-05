
package novoda.lib.sqliteprovider.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsing .sql files and get single statements suitable for insertion.
 */
public class SQLFile {

    private static final String STATEMENT_END_CHARACTER = ";";
    private static final String LINE_COMMENT_START_CHARACTERS = "--";
    private static final String BLOCK_COMMENT_START_CHARACTERS = "/*";
    private static final String CLOCK_COMENT_END_CHARACTERS = "*/";
    private static final String LINE_CONCATENATION_CHARACTER = " ";

    private List<String> statements;

    private boolean inComment = false;

    public void parse(Reader in) throws IOException {
        BufferedReader reader = new BufferedReader(in);
        statements = new ArrayList<String>();
        String line = null;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith(LINE_COMMENT_START_CHARACTERS)) {
                continue;
            }

            if (line.startsWith(BLOCK_COMMENT_START_CHARACTERS)) {
                inComment = true;
                continue;
            }

            if (line.endsWith(CLOCK_COMENT_END_CHARACTERS) && inComment) {
                inComment = false;
                continue;
            }

            if (inComment) {
                continue;
            }

            statement.append(line);
            if (!line.endsWith(STATEMENT_END_CHARACTER)) {
                statement.append(LINE_CONCATENATION_CHARACTER);
                continue;
            }

            statements.add(statement.toString());
            statement.setLength(0);
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
