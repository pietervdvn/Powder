package reactivity.valueWrappers.validators;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class IntegerRangeValidator implements Validator<Integer>{
	
	private final int min;
	private final int max;
	
	/**
	 * Validates if the value is inside the range.
	 * Note: min and max are inclusive.
	 * @param min
	 * @param max
	 */
	public IntegerRangeValidator(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean isValidValue(Integer value) {
		return (min<= value) && (max>= value);
	}

}
