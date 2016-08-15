package simulation;

import java.util.List;
import java.util.Map;

import csv.CSVEntry;
import csv.SimpleCSVParser;

public class DecayReaction {

	public final int element;
	public final int result;

	public final double prob;
	public final double enthalpy;
	public final double pressure;

	private final ElementIndexing indexer;

	public DecayReaction(ElementIndexing indexer, int element, int result, double prob,
			double enthalpy, double pressure) {
		this.indexer = indexer;
		this.element = element;
		this.result = result;
		this.prob = prob;
		this.enthalpy = enthalpy;
		this.pressure = pressure;

	}

	public void validate() {
		if (prob < 0.0 || prob > 1.0) {
			throw new IllegalArgumentException("Probabilities should be >=0 and <= 1");
		}
	}

	public static DecayReaction fromCSV(ElementIndexing indexer, Map<String, CSVEntry> csv) {
		int el0id = indexer.idFromName(csv.get("element").stringValue);
		int rel0id = indexer.idFromName(csv.get("result").stringValue);
		return new DecayReaction(indexer, el0id, rel0id, csv.get("prob").doubleWithDefault(1.0),
				csv.get("enthalpy").doubleWithDefault(0.0), csv.get("pressure").doubleWithDefault(0.0));
	}

	public static DecayReaction[] fromCSVs(ElementIndexing indexer, List<String> lines) {
		DecayReaction[] reactions = new DecayReaction[lines.size() - 1];
		List<Map<String, CSVEntry>> csvs = SimpleCSVParser.parseCSV(lines);

		for (int i = 0; i < reactions.length; i++) {
			reactions[i] = fromCSV(indexer, csvs.get(i));
		}

		return reactions;
	}

	@Override
	public String toString() {
		return indexer.nameOf(element) + " --> " + indexer.nameOf(result) + " (p=" + prob + ")";
	}
}
