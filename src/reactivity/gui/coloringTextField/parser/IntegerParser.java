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
public abstract class IntegerParser extends Parser<Integer>{

	@Override
	public boolean isParsable(String toParse) {
		try{
			Integer.parseInt(toParse);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Integer parseValue(String toParse) throws ParseException {
		return Integer.parseInt(toParse);
	}

	@Override
	public String getShortestNotationFor(Integer value) {
		return value.toString();
	}

}
