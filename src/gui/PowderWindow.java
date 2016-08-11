package gui;

import grid.DubbleGrid;
import grid.FullGrid;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import levels.Level;
import reactivity.ValueListener;
import reactivity.gui.ProgressMeter;
import reactivity.gui.Toggle;
import reactivity.gui.coloringTextField.ValueField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.DoubleValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;
import rendering.ElementRender;
import rendering.GridRender;
import rendering.PressureRender;
import rendering.Render;
import rendering.TemperatureRender;

public class PowderWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public final BooleanValue temperatureToggle;
	public final BooleanValue elementsToggle;
	public final BooleanValue pressureToggle;
	public final BooleanValue renderToggle;

	private final ValueListener<Integer> repainter = new ValueListener<Integer>() {

		@Override
		public void onValueChanged(Value<Integer> source, Integer newValue) {
			repaint();
		}
	};

	public PowderWindow(GUIColorModel cm, FullGrid elements, List<Level> seeds,
			int pixelsPerDot) {

		RenderingJPanel renderP = new RenderingJPanel();

		DubbleGrid temperature = elements.getTemperature();

		Render renderTemp = new GridRender(temperature, new TemperatureRender(
				pixelsPerDot, pixelsPerDot));
		Render renderElements = new ElementRender(elements, pixelsPerDot,
				pixelsPerDot);
		Render renderPressure = new GridRender(elements.getPressure(),
				new PressureRender(pixelsPerDot, pixelsPerDot));

		elementsToggle = renderP.addRenderer(renderElements, "Elements");
		temperatureToggle = renderP.addRenderer(renderTemp, "Temperature");
		pressureToggle = renderP.addRenderer(renderPressure,
				"Structural pressure");

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

		elements.ticks.addListener(repainter);

		TickThread ticker = new TickThread(elements);
		renderToggle = ticker.control;

		new Thread(ticker).start();

		JPanel extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.Y_AXIS));

		JPanel controls = new JPanel();
		controls.setBorder(new TitledBorder("Controls"));
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		Toggle playPause = new Toggle(ticker.control);
		controls.add(playPause);
		controls.add(new ToggleOptions(renderP.getRenderToggles(), true));
		controls.add(new ValueField<>(cm, elements.ticks));
		controls.add(new ValueField<>(cm, elements.time));
		controls.add(new ValueField<>(cm, averageTemperature));
		controls.add(new ValueField<>(cm, elements.fixedColumns));

		JButton tickOnce = new JButton(new AbstractAction("Tick Once") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				elements.evolve();
			}
		});
		controls.add(tickOnce);

		JPanel tickThousandP = new JPanel();
		IntegerValue progresMeas = new IntegerValue(0, "Progress");
		final JButton tickThousand = new JButton();
		AbstractAction a= 
				new AbstractAction("Tick 1000") {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						progresMeas.set(1000);
						tickThousand.setEnabled(false);
						new Thread(new Runnable() {
							@Override
							public void run() {
								elements.evolve(progresMeas);
								progresMeas.set(0);
								tickThousand.setEnabled(true);
							}
						}).start();
					}
				};
				tickThousand.setAction(a);

		tickThousandP.add(tickThousand);
		tickThousandP.add(new ProgressMeter(progresMeas, 0, 1000));

		controls.add(tickThousandP);

		ticker.control.addHardListener(new ValueListener<Boolean>() {
			@Override
			public void onValueChanged(Value<Boolean> source, Boolean newValue) {
				tickOnce.setEnabled(!newValue);
				tickThousand.setEnabled(!newValue);
			}
		});
		ticker.control.throwEvent();

		extras.add(controls);
		if (seeds != null && seeds.size() > 0) {
			JPanel presets = new JPanel();
			presets.setBorder(new TitledBorder("Preset levels"));
			presets.setLayout(new GridLayout(3, 2));
			for (Level seed : seeds) {

				JButton b = new JButton(seed.name());
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						boolean running;
						synchronized (ticker.control) {
							running = ticker.control.get();
							playPause.setEnabled(false);
							ticker.control.set(false);
							try {
								if (running) {
									ticker.control.wait();
								}

							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						new Thread(new Runnable() {

							@Override
							public void run() {
								elements.init(seed);
								ticker.control.set(running);
								playPause.setEnabled(true);
								if (!elements.ticks.set(0)) {
									elements.ticks.throwEvent();
								}
							}
						}).start();
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
