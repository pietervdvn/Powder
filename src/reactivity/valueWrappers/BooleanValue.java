package reactivity.valueWrappers;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class BooleanValue extends PrimitiveValue<Boolean> {
	
	public BooleanValue(boolean initVal, String xmlName){
		super(initVal, xmlName);
	}
	
	@Override
	public Boolean parse(String text) {
		return Boolean.parseBoolean(text);
	}

}
