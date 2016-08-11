package reactivity.gui;

import javax.swing.JProgressBar;

import reactivity.ValueListener;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;

public class ProgressMeter extends JProgressBar implements ValueListener<Integer>{
	private static final long serialVersionUID = 1L;

	public ProgressMeter(IntegerValue v, int start, int stop) {
		super(start, stop);
		v.addListener(this);
	}

	@Override
	public void onValueChanged(Value<Integer> source, Integer newValue) {
		this.setValue(newValue);
	}

}
