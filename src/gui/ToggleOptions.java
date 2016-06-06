package gui;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import reactivity.gui.Toggle;
import reactivity.valueWrappers.BooleanValue;

public class ToggleOptions extends JPanel {
	private static final long serialVersionUID = -2281771652897523014L;

	public ToggleOptions(List<BooleanValue> toggles, boolean vertical) {
		BoxLayout l = new BoxLayout(this, vertical ? BoxLayout.Y_AXIS
				: BoxLayout.X_AXIS);
		this.setLayout(l);
		for (BooleanValue toggle : toggles) {
			this.add(new Toggle(toggle));
		}
	}

}
