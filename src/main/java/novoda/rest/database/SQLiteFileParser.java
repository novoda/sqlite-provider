package novoda.rest.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/*
 * Very simple parser.
 * Currently only support per line sql statements
 */
public class SQLiteFileParser implements Iterator<String>, Iterable<String> {

	private BufferedReader br;

	private String currentLine = null;

	private boolean inHasNext = false;

	public SQLiteFileParser(InputStream in) {
		br = new BufferedReader(new InputStreamReader(in));
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		try {
			inHasNext  = true;
			currentLine = getNextSQL();
			if (currentLine != null) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String next() {
		try {
			if (inHasNext) {
				inHasNext = false;
				return currentLine;
			}
			return getNextSQL();
		} catch (IOException e) {
			return null;
		}
	}

	private String getNextSQL() throws IOException {
		currentLine = br.readLine();
		if (currentLine != null
				&& (currentLine.startsWith("--") || currentLine.trim().length() == 0)) {
			return getNextSQL();
		}
		return currentLine;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("not supported");
	}

	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			// do nothing
		}
	}

}
