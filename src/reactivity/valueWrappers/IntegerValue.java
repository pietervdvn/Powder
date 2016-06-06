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
public class IntegerValue extends PrimitiveValue<Integer>{

	public IntegerValue(Integer value, String name) {
		super(value, name);
	}

	public IntegerValue(Integer value, String name, Validator<Integer> v) {
		super(value, name, v);
	}

	@Override
	public Integer parse(String text) {
		return Integer.parseInt(text);
	}

}
