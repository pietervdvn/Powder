package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import grid.Grid;
import grid2.UsefullFullGrid;
import reactivity.ValueListener;
import reactivity.gui.coloringTextField.ValueField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.DoubleValue;
import reactivity.valueWrappers.Value;

public class StatsPanel extends JPanel {
	private static final long serialVersionUID = -5049433424673188370L;

	public StatsPanel(GUIColorModel cm, UsefullFullGrid elements, Grid temperature) {

		this.setBorder(new TitledBorder("statistics"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		DoubleValue averageTemperature = new DoubleValue(0.0, "Average temperature");
		elements.ticks.addHardListener(new ValueListener<Integer>() {
			@Override
			public void onValueChanged(Value<Integer> source, Integer newValue) {
				if (newValue % 10 == 0) {
					Grid t = temperature;
					double avg = t.sum() / t.surface();
					averageTemperature.set(avg);
				}
			}
		});
		add(new ValueField<>(cm, elements.ticks));
		add(new ValueField<>(cm, elements.neededTime));
		add(new ValueField<>(cm, averageTemperature));
	}

}
