package reactivity.valueWrappers;

import reactivity.ValueListener;

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
	
	public static class Not extends BooleanValue implements ValueListener<Boolean> {

		public Not(BooleanValue src) {
			super(!src.get(), src.getName());
			src.addListener(this);
		}

		@Override
		public void onValueChanged(Value<Boolean> source, Boolean newValue) {
			set(!newValue);
		}

		
	}
	
}
