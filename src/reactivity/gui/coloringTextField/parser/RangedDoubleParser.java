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
public abstract class RangedDoubleParser extends DoubleParser{
	
	private final double max;
	private final double min;
	
	private final int scale;
	
	public RangedDoubleParser(int round, double min, double max, int scale) {
		super(round);
		this.min = min;
		this.max = max;
		this.scale = scale;
	}
	
	public RangedDoubleParser(int round, double min, double max) {
		this(round, min, max, 1);
	}

	
	@Override
	public Double parseValue(String toParse) throws ParseException {
		Double val = super.parseValue(toParse);
		if(val<min || val>max){
			throw new ParseException("Cannot parse: "+toParse, 0);
		}
		return val*scale;
	}
	
	@Override
	public String getShortestNotationFor(Double value) {
		return super.getShortestNotationFor(value/scale);
	}

}
