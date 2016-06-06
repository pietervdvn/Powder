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
public abstract class RangedIntegerParser extends Parser<Integer>{
	
	private final int max;
	private final int min;
	
	private final int scale;
	
	public RangedIntegerParser(int min, int max, int scale) {
		this.min = min;
		this.max = max;
		this.scale = scale;
	}
	
	public RangedIntegerParser(int min, int max){
		this(min, max, 1);
	}
	
	@Override
	public Integer parseValue(String toParse) throws ParseException {
		Integer val =(int) (Double.parseDouble(toParse)*scale);
		if(val<min || val>max){
			throw new ParseException("Cannot parse: "+toParse, 0);
		}
		return val;
	}
	
	@Override
	public String getShortestNotationFor(Integer value) {
		if(scale == 1){
			return ""+value;
		}
		double v = (double) value/scale;
		return ""+v;
	}

	@Override
	public boolean isParsable(String toParse) {
		try{
			parseValue(toParse);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

}
