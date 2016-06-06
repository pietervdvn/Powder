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
public class DoubleRangeValidator implements Validator<Double>{
	
	private final double min;
	private final double max;
	
	/**
	 * Validates if the value is inside the range.
	 * Note: min and max are inclusive.
	 * @param min
	 * @param max
	 */
	public DoubleRangeValidator(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean isValidValue(Double value) {
		return (min<= value) && (max>= value);
	}

}
