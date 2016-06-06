package reactivity.gui.coloringTextField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import reactivity.ValueListener;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;

public class ValueField<T> extends JPanel implements ValueListener<T> {
	private static final long serialVersionUID = 1L;
	private final JTextField f;

	public ValueField(GUIColorModel cm, Value<T> v) {
		this.add(new JLabel(v.getName()));
		f = new JTextField();
		f.setEnabled(false);
		f.setColumns(10);
		f.setText(v.get().toString());
		f.setForeground(cm.getForeground(GUIColorModel.DEFAULT));
		v.addListener(this);
		this.add(f);
	}

	@Override
	public void onValueChanged(Value<T> source, T newValue) {
		f.setText(newValue.toString());
	}

}
