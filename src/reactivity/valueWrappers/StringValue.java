package reactivity.valueWrappers;

import reactivity.valueWrappers.validators.Validator;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class StringValue extends PrimitiveValue<String> {
	
	public StringValue(String value, String name) {
		super(value, name);
	}

	public StringValue(String value, String name, Validator<String> v) {
		super(value, name, v);
	}

	@Override
	public String parse(String text) {
		return text;
	}

}
