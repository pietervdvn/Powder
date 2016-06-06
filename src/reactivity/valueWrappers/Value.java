package reactivity.valueWrappers;

import reactivity.EventSource;
import reactivity.SafeList;
import reactivity.ValueListener;
import reactivity.valueWrappers.validators.Validator;

/**
 * Valurwrappers are a minimodel, for value's in bigger -real- models.
 */
/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class Value<T> extends EventSource<ValueListener<T>> {

	private final static Value<Integer> NUMBER_OF_VALUES = new IntegerValue(1, "numberOfValues");
	public static Value<Integer> getNumberOfValues() {
		return NUMBER_OF_VALUES;
	}
	
	
	private T value;
	
	private final Validator<T> validator;
	private final String name;
	
	public Value(T value, String name){
		this(value, name, null);
	}
	
	public Value(T value, String name, Validator<T> v) {
		this.value = value;
		this.validator = v;
		this.name = name;
		if(NUMBER_OF_VALUES != null){
			NUMBER_OF_VALUES.set(NUMBER_OF_VALUES.get()+1);
		}
	}

	public T get() {
		return value;
	}
	
	
	/**
	 * Returns true if value changed
	 * @param value
	 * @return
	 */
	public boolean set(T value) {
		if(this.value == value || this.value.equals(value)){
			return false;
		}
		if(!(validator == null) && !validator.isValidValue(value)){
			throw new IllegalArgumentException("This value "+value.toString()+"is refused by the set validator. "+validator.toString());
		}
		this.value = value;
		throwEvent();
		return true;
	}
	
	private void throwEvent(){
		SafeList<ValueListener<T>> l = getListeners();
		synchronized (l) {
			l.resetCounter();
			while(l.hasNext()){
				l.next().onValueChanged(this, value);
			}
		}
	}

	public String getName() {
		return name;
	}
	
	
}
