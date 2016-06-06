package reactivity.gui.coloringTextField.parser;

import java.text.ParseException;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public abstract class DoubleParser extends Parser<Double>{

	private final int round;
	
	private String infinite = "infinite";
	public DoubleParser(int numbersAfterPoint) {
		this.round = numbersAfterPoint;
	}
	
	@Override
	public boolean isParsable(String toParse) {
		try{
			parseValue(toParse);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	@Override
	public Double parseValue(String toParse) throws ParseException {
		if(toParse.toLowerCase().equals(infinite.toLowerCase())){
			return Double.POSITIVE_INFINITY;
		}
		return Double.parseDouble(toParse);
	}

	@Override
	public String getShortestNotationFor(Double value) {
		if(value.isInfinite()){
			return infinite;
		}
		if(value.isNaN()){
			return "Not a number";
		}
		double val = Math.round(value*round)/(double) round;
		return ""+val;
	}

}
