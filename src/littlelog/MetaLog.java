package littlelog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetaLog {

	private String[] columns;

	public MetaLog(final String path) throws IOException {
		this.interpretSchema(path);
	}

	private List<String> removeFalsyValues(final String[] array) {
		final List<String> filteredResults = new ArrayList<>();
		for (final String column : array) {
			if (column != null && !column.isEmpty()) {
				filteredResults.add(column);
			}
		}
		return filteredResults;
	}

	private void interpretSchema(final String path) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(path));
		final String[] CSVHeader = reader.readLine().split(",");
		final List<String> filteredResults = removeFalsyValues(CSVHeader);
		this.columns = filteredResults.toArray(new String[0]);
	}

	public String[] getColumns() {
		return this.columns;
	}
}
