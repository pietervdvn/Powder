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
public abstract class Parser <T> {

	public abstract boolean isParsable(String toParse);
	public T parse(String toParse) throws ParseException{
		if(isParsable(toParse)){
			T parsed = parseValue(toParse);
			return parsed;
		}else{
			throw new ParseException("Cannot parse: "+toParse, 0);
		}
	}
	protected abstract T parseValue(String toParse) throws ParseException;
	public abstract String getShortestNotationFor(T value);
	public abstract void newValueAction(T value);
	
}
