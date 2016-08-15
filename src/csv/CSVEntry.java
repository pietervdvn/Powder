package csv;

public class CSVEntry {

	public final String stringValue;
	public final Integer intValue;
	public final Double doubleValue;

	public CSVEntry(String origin) {
		this.stringValue = origin;

		Integer i;
		try {
			i = Integer.parseInt(origin);
		} catch (Exception e) {
			i = null;
		}
		intValue = i;

		Double d;
		try {
			d = Double.parseDouble(origin);
		} catch (Exception e) {
			d = null;
		}
		doubleValue = d;
	}

	public int intWithDefault(int d) {
		if (intValue == null) {
			return d;
		}
		return intValue;
	}

	public double doubleWithDefault(double d) {
		if (doubleValue == null) {
			return d;
		}
		return doubleValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}

}
