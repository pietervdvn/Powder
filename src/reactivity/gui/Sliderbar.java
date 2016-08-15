package reactivity.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import reactivity.ValueListener;
import reactivity.gui.coloringTextField.IntegerValueTextField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;

public class Sliderbar extends JPanel implements ValueListener<Integer>{
	private static final long serialVersionUID = 1L;

	private final JSlider slider;
	
	public Sliderbar(GUIColorModel cm, Value<Integer> toModify, int min, int max) {
		slider = new JSlider(min, max, toModify.get());
		add(new JLabel(toModify.getName() + ": "));
		add(new IntegerValueTextField(cm, toModify, min, max));
		add(slider);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				toModify.set(slider.getValue());
			}
		});
	}

	@Override
	public void onValueChanged(Value<Integer> source, Integer newValue) {
		slider.setValue(newValue);
	}

}
