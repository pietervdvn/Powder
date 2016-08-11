package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import grid.DubbleGrid;
import grid.FullGrid;
import reactivity.ValueListener;
import reactivity.gui.coloringTextField.ValueField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.DoubleValue;
import reactivity.valueWrappers.Value;

public class StatsPanel extends JPanel {
	private static final long serialVersionUID = -5049433424673188370L;

	public StatsPanel(GUIColorModel cm, FullGrid elements) {
		
		this.setBorder(new TitledBorder("statistics"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		DoubleValue averageTemperature = new DoubleValue(elements
				.getTemperature().sum() / elements.getTemperature().surface(),
				"Average temperature");
		elements.ticks.addHardListener(new ValueListener<Integer>() {
			@Override
			public void onValueChanged(Value<Integer> source, Integer newValue) {
				if (newValue % 10 == 0) {
					DubbleGrid t = elements.getTemperature();
					double sum = t.sum() / t.surface();
					averageTemperature.set(sum);
				}
			}
		});
		add(new ValueField<>(cm, elements.ticks));
		add(new ValueField<>(cm, elements.time));
		add(new ValueField<>(cm, averageTemperature));
		add(new ValueField<>(cm, elements.fixedColumns));
	}

}
