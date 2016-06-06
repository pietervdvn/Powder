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
public class DoubleValue extends PrimitiveValue<Double> {

	public DoubleValue(Double value, String name) {
		super(value, name);
	}

	public DoubleValue(Double value, String name, Validator<Double> v) {
		super(value, name, v);
	}

	@Override
	public Double parse(String text) {
		return Double.parseDouble(text);
	}

}
