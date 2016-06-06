package reactivity.gui.coloringTextField;

import reactivity.ValueListener;
import reactivity.gui.coloringTextField.parser.RangedIntegerParser;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class IntegerValueTextField extends ColoringTextField<Integer> implements ValueListener<Integer>{
	private static final long serialVersionUID = 7935559637265433617L;

	public IntegerValueTextField(GUIColorModel colorSettings, final Value<Integer> value, int min, int max, int scale) {
		super(colorSettings, new RangedIntegerParser(min, max, scale) {
			
			@Override
			public void newValueAction(Integer newVal) {
				value.set(newVal);
			}
		});
		value.addListener(this);
		setValue(value.get());
	}
	
	public IntegerValueTextField(GUIColorModel colorSettings, final Value<Integer> value, int min, int max) {
		this(colorSettings, value, min, max, 1);
	}


	@Override
	public void onValueChanged(Value<Integer> source, Integer newValue) {
		setValue(newValue);
	}


}
