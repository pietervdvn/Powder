package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import gui.grid.Grid;
import reactivity.ValueListener;
import reactivity.gui.coloringTextField.ValueField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.DoubleValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;
import simulation.UsefullFullGrid;

public class StatsPanel extends JPanel {
	private static final long serialVersionUID = -5049433424673188370L;

	public StatsPanel(GUIColorModel cm, UsefullFullGrid elements, Grid temperature, Value<Integer> mouseX, Value<Integer> mouseY) {

		this.setBorder(new TitledBorder("statistics"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		DoubleValue averageTemperature = new DoubleValue(0.0, "Average temperature");
		elements.ticks.addHardListener(new ValueListener<Integer>() {
			@Override
			public void onValueChanged(Value<Integer> source, Integer newValue) {
				if (newValue % 10 == 0) {
					Grid t = temperature;
					double avg = t.sum() / t.surface();
					avg = Math.round(avg);
					averageTemperature.set(avg);
				}
			}
		});
		
		IntegerValue mouseTemperature = new IntegerValue(0, "Temperature under the mouse");
		ValueListener<Integer> vl = new ValueListener<Integer>() {
			@Override
			public void onValueChanged(Value<Integer> source, Integer newValue) {
				int x = mouseX.get();
				int y = mouseY.get();
				if(x < 0 || y < 0 || temperature.dotsX <= x || temperature.dotsY <= y){
					return;
				}
				mouseTemperature.set((int) Math.round(temperature.get(x, y).doubleValue()));
			}
		};
		mouseX.addHardListener(vl);
		mouseY.addHardListener(vl);
		
		
		
		add(new ValueField<>(cm, elements.ticks));
		add(new ValueField<>(cm, elements.neededTime));
		add(new ValueField<>(cm, averageTemperature));
		add(new ValueField<>(cm, mouseTemperature));
	}

}
