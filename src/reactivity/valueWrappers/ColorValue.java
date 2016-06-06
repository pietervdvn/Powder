package reactivity.valueWrappers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
public class ColorValue extends Value<Color> implements ValueListener<Integer>{
	
	private IntegerValue red = new IntegerValue(0, "red");
	private IntegerValue green = new IntegerValue(0, "green");
	private IntegerValue blue = new IntegerValue(0, "blue");
	private IntegerValue alpha = new IntegerValue(0, "alpha");
	
	private List<IntegerValue> xmlables = new ArrayList<IntegerValue>();
	
	public ColorValue(Color value, String name) {
		super(value, name);
		red.set(value.getRed());
		blue.set(value.getBlue());
		green.set(value.getGreen());
		alpha.set(value.getAlpha());
		
		xmlables.add(red);
		xmlables.add(green);
		xmlables.add(blue);
		xmlables.add(alpha);
		
		for(IntegerValue v : xmlables){
			v.addListener(this);
		}

	}
	
	@Override
	public boolean set(Color value) {
		red.set(value.getRed());
		blue.set(value.getBlue());
		green.set(value.getGreen());
		alpha.set(value.getAlpha());
		return super.set(value);
	}
	
	@Override
	public void onValueChanged(Value<Integer> source, Integer newValue) {
		set(new Color(red.get(), green.get(), blue.get(), alpha.get()));
	}

}
