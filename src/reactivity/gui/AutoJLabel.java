package reactivity.gui;


import javax.swing.JLabel;

import reactivity.ValueListener;
import reactivity.valueWrappers.StringValue;
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
public class AutoJLabel extends JLabel implements ValueListener<String>{
	private static final long serialVersionUID = -8430174910120914248L;

	private final String endSeq;
	
	public AutoJLabel(StringValue tracker){
		this(tracker, ":");
	}
	
	public AutoJLabel(StringValue tracker, String endSeq) {
		this.endSeq = endSeq;
		tracker.addListener(this);
		onValueChanged(tracker, tracker.get());
	}

	@Override
	public void onValueChanged(Value<String> source, String newValue) {
		this.setText(newValue+endSeq);
	}
	
	
	

}
