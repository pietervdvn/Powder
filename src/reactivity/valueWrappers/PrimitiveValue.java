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
public abstract class PrimitiveValue<T> extends Value<T>{

	public PrimitiveValue(T value, String name) {
		super(value, name);
	}

	public PrimitiveValue(T value, String name, Validator<T> v) {
		super(value, name, v);
	}

	public abstract T parse(String text);
}
