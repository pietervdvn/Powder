package gui;

import grid.DubbleGrid;
import grid.ElementGrid;
import grid.ElementGridSeed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import reactivity.ValueListener;
import reactivity.gui.coloringTextField.ValueField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;
import rendering.ElementRender;
import rendering.GridRender;
import rendering.Render;
import rendering.TemperatureRender;

public class PowderWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final ValueListener<Integer> repainter = new ValueListener<Integer>() {

		@Override
		public void onValueChanged(Value<Integer> source, Integer newValue) {
			repaint();
		}
	};

	public PowderWindow(GUIColorModel cm, ElementGrid elements,
			List<ElementGridSeed> seeds, int pixelsPerDot) {

		RenderingJPanel renderP = new RenderingJPanel();

		DubbleGrid temperature = elements.getTemperature();

		Render renderTemp = new GridRender(temperature, new TemperatureRender(
				pixelsPerDot, pixelsPerDot));
		Render renderElements = new ElementRender(elements, pixelsPerDot,
				pixelsPerDot);
		renderP.addRenderer(renderTemp, "Temperature");
		renderP.addRenderer(renderElements, "Elements");

		elements.ticks.addListener(repainter);

		JPanel extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.Y_AXIS));

		JPanel controls = new JPanel();
		controls.setBorder(new TitledBorder("Controls"));
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.add(new ToggleOptions(renderP.getRenderToggles(), true));
		controls.add(new ValueField<>(cm, elements.ticks));
		controls.add(new ValueField<>(cm, elements.time));
		extras.add(controls);
		if (seeds != null && seeds.size() > 0) {
			JPanel presets = new JPanel();
			presets.setBorder(new TitledBorder("Preset levels"));
			presets.setLayout(new BoxLayout(presets, BoxLayout.Y_AXIS));
			for (ElementGridSeed seed : seeds) {
				
				JButton b = new JButton(seed.name());
				b.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						seed.seed(elements);
					}
				});
				presets.add(b);
			}
			extras.add(presets);
		}
		this.add(extras, BorderLayout.WEST);
		this.add(renderP);
		this.pack();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}
}
