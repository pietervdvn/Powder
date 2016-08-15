package grid2;

import static utils.Utils.INF;

import java.awt.Color;
import java.util.Map;

import csv.CSVEntry;

/*
 * Basic element data - no behaviour at all
 */
public class Element {

	/*
	 * Internal integer representation. Note: 0 is 'dissolve' and should never
	 * be used!
	 */
	public final int id;

	public final String name;
	public final Color color;

	public final double condensPoint;
	public final double densityAtLowest;
	public final double enthalpyCold;
	public final double pressureCold;
	public final int cooledState; // an ID

	public final double vaporPoint;
	public final double densityAtHighest;
	public final double enthalpyHot;
	public final double pressureHot;
	public final int heatedState; // an ID

	public final int fixed;
	public final double movability;
	public final double movabilitySame;

	public final double heatExchange;
	public final double heatExchangeSame;

	public final double pressureExchange;
	public final double pressureExchangeSame;
	
	// calculated values
	public final double weightDiff;
	public final double tempRange;

	public Element(int id, String name, Color color, double densityAtLowest, double condensPoint, double enthalpyCold,
			double pressureCold, int cooledState, double densityAtHighest, double vaporPoint, double enthalpyHot,
			double pressureHot, int heatedState, int fixed, double movabiltiy, double movabilitySame,
			double heatExchange, double heatExchangeSame, double pressureExchange, double pressureExchangeSame) {
		this.id = id;
		this.name = name.substring(0, 1) + name.substring(1).toLowerCase();
		this.color = color;
		this.densityAtLowest = densityAtLowest;
		this.condensPoint = condensPoint;
		this.enthalpyCold = enthalpyCold;
		this.pressureCold = pressureCold;
		this.cooledState = cooledState;
		this.densityAtHighest = densityAtHighest;
		this.vaporPoint = vaporPoint;
		this.enthalpyHot = enthalpyHot;
		this.pressureHot = pressureHot;
		this.heatedState = heatedState;
		this.fixed = fixed;
		this.movability = movabiltiy;
		this.movabilitySame = movabilitySame;
		this.heatExchange = heatExchange;
		this.heatExchangeSame = heatExchangeSame;
		this.pressureExchange = pressureExchange;
		this.pressureExchangeSame = pressureExchangeSame;
		
		this.weightDiff = densityAtLowest - densityAtHighest;
		this.tempRange = vaporPoint - condensPoint;
	}
	
	public double weight(double temperature) {
		return densityAtLowest + weightDiff * (temperature - condensPoint) / tempRange;
	}


	private void ve(String message) {
		throw new IllegalArgumentException("Element " + name + ": " + message);
	}

	private void gz(String field, double value) {
		if (value < 0) {
			ve(field + " should be bigger then zero");
		}
	}

	public void crossValidate(Element[] otherElements) {
		if (heatedState >= otherElements.length) {
			ve("Element ID " + heatedState + " for heated state of " + name + " does not exist (out of bounds)");
		}
		if (cooledState >= otherElements.length) {
			ve("Element ID " + cooledState + " for heated state of " + name + " does not exist (out of bounds)");
		}

	}

	public void validate() {
		if (name == null || name.equals("")) {
			ve("Name is required");
		}
		if (condensPoint >= vaporPoint) {
			ve("Condens point is higher then the vapor point");
		}
		gz("condensPoint", condensPoint);
		gz("vaporPoint", vaporPoint);
		if (!(fixed == 0 || fixed == 1)) {
			ve("Fixed should be either 0 or 1");
		}
		gz("movability", movability);
		gz("movabilitySame", movabilitySame);

		gz("heatExchange", heatExchange);
		gz("heatExchangeSame", heatExchangeSame);

		gz("pressureExchange", pressureExchange);
		gz("pressureExchangeSame", pressureExchangeSame);

	}

	public static Element fromCSV(int id, Map<String, Integer> elementIDs, Map<String, CSVEntry> csv) {
		String name = csv.get("name").stringValue;
		Color color = new Color(csv.get("colorred").intWithDefault(0), csv.get("colorgreen").intWithDefault(0),
				csv.get("colorblue").intWithDefault(0));

		double condensPoint = csv.get("condensPoint").doubleWithDefault(0);
		double densityAtLowest = csv.get("densityAtLowest").doubleValue;
		double enthalpyCold = csv.get("enthalpyCold").doubleWithDefault(0);
		double pressureCold = csv.get("pressureCold").doubleWithDefault(0);
		String cooledName = csv.get("cooledState").stringValue.toUpperCase();
		if (!cooledName.equals("") && !elementIDs.containsKey(cooledName)) {
			throw new IllegalArgumentException("Element " + name + ": " + cooledName + " is not a known element");
		}
		int cooledState = elementIDs.getOrDefault(cooledName, -1);

		double vaporPoint = csv.get("vaporPoint").doubleWithDefault(INF);
		double densityAtHighest = csv.get("densityAtHighest").doubleValue;
		double enthalpyHot = csv.get("enthalpyHot").doubleWithDefault(0);
		double pressureHot = csv.get("pressureHot").doubleWithDefault(0);

		String heatedName = csv.get("heatedState").stringValue.toUpperCase();
		if (!heatedName.equals("") && !elementIDs.containsKey(heatedName)) {
			throw new IllegalArgumentException("Element " + name + ": " + heatedName + " is not a known element");
		}
		int heatedState = elementIDs.getOrDefault(heatedName, -1);

		int fixed = csv.get("fixed").intWithDefault(0);
		double movabiltiy = csv.get("movability").doubleWithDefault(0);
		double movabilitySame = csv.get("movabilitySame").doubleWithDefault(movabiltiy);

		double heatExchange = csv.get("heatExchange").doubleWithDefault(0);
		double heatExchangeSame = csv.get("heatExchangeSame").doubleWithDefault(heatExchange);

		double pressureExchange = csv.get("pressureExchange").doubleWithDefault(0);
		double pressureExchangeSame = csv.get("pressureExchangeSame").doubleWithDefault(pressureExchange);

		return new Element(id, name, color, densityAtLowest, condensPoint, enthalpyCold, pressureCold, cooledState,
				densityAtHighest, vaporPoint, enthalpyHot, pressureHot, heatedState, fixed, movabiltiy, movabilitySame,
				heatExchange, heatExchangeSame, pressureExchange, pressureExchangeSame);

	}

	@Override
	public String toString() {
		return name + " (" + id + ")";
	}

}
