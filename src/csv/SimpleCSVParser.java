package csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCSVParser {

	public static List<Map<String, CSVEntry>> parseCSV(String csv) {
		return parseCSV(Arrays.asList(csv.split("\n")));

	}

	public static List<Map<String, CSVEntry>> parseCSV(List<String> lines) {

		String[] header = lines.get(0).split(",");
		for (int i = 0; i < header.length; i++) {
			header[i] = header[i].trim();
		}

		List<Map<String, CSVEntry>> parsed = new ArrayList<>(lines.size() - 1);

		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] parts = line.split(",");
			Map<String, CSVEntry> lineDict = new HashMap<>(header.length);
			for (int j = 0; j < header.length; j++) {
				String part = j < parts.length ? parts[j] : "";
				CSVEntry entry = new CSVEntry(part.trim());
				lineDict.put(header[j], entry);
			}
			parsed.add(lineDict);
		}

		return parsed;
	}

}
