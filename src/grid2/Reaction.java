package grid2;

import java.util.List;
import java.util.Map;

import csv.CSVEntry;
import csv.SimpleCSVParser;

public class Reaction {

	public final int element0;
	public final int element1;
	
	public final int result0;
	public final int result1;
	
	public final double prob;

	private final ElementIndexing indexer;

	public Reaction(ElementIndexing indexer, int element0, int element1, int result0, int result1, double prob) {
		this.indexer = indexer;
		this.element0 = element0;
		this.element1 = element1;
		this.result0 = result0;
		this.result1 = result1;
		this.prob = prob;
	}

	public void validate() {
		if (prob < 0.0 || prob > 1.0) {
			throw new IllegalArgumentException("Probabilities should be >=0 and <= 1");
		}
	}

	public static Reaction fromCSV(ElementIndexing indexer, Map<String, CSVEntry> csv) {
			int el0id = indexer.idFromName(csv.get("element0").stringValue);
			int el1id = indexer.idFromName(csv.get("element1").stringValue);
			int rel0id = indexer.idFromName(csv.get("result0").stringValue);
			int rel1id = indexer.idFromName(csv.get("result1").stringValue);
		return new Reaction(indexer, el0id, el1id, rel0id, rel1id, csv.get("prob").doubleWithDefault(1.0));
	}

	public static Reaction[] fromCSVs(ElementIndexing indexer, List<String> lines) {
		Reaction[] reactions = new Reaction[lines.size() - 1];
		List<Map<String, CSVEntry>> csvs = SimpleCSVParser.parseCSV(lines);

		for (int i = 0; i < reactions.length; i++) {
			reactions[i] = fromCSV(indexer, csvs.get(i));
		}

		return reactions;
	}

	@Override
	public String toString() {
		return indexer.nameOf(element0) + " + " + indexer.nameOf(element1) + " --> "
				+ indexer.nameOf(result0) + " + " + indexer.nameOf(result1)+" (p=" + prob + ")";
	}

}
