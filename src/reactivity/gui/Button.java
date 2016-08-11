package reactivity.gui;

import javax.swing.JButton;

import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.Value;

public class Button extends JButton{
	private static final long serialVersionUID = 1L;

	private BooleanValue enablerToggle = null;
	private final ValueListener<Boolean> enabler = new ValueListener<Boolean>() {

		@Override
		public void onValueChanged(Value<Boolean> source, Boolean newValue) {
			Button.this.setEnabled(newValue);
		}
		
	};
	
	public Button(String name) {
		super(name);
	}

	public void setEnabled(BooleanValue enablerToggle) {
		if(this.enablerToggle != null){
			this.enablerToggle.removeListener(enabler);
		}
		enablerToggle.addListener(enabler);
		enabler.onValueChanged(enablerToggle, enablerToggle.get());
		this.enablerToggle = enablerToggle;
	}

}
