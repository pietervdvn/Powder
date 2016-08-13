package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import grid.DubbleGrid;
import grid.FullGrid;
import levels.Level;
import levels.Levels;
import reactivity.ValueListener;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;
import rendering.ElementRender;
import rendering.GridRender;
import rendering.PressureRender;
import rendering.Render;
import rendering.TemperatureRender;

public class PowderWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final Pencil p;
	
	private final ValueListener<Integer> repainter = new ValueListener<Integer>() {

		@Override
		public void onValueChanged(Value<Integer> source, Integer newValue) {
			repaint();
		}
	};

	public PowderWindow(GUIColorModel cm, FullGrid elements, int pixelsPerDot) {

		RenderingJPanel renderP = new RenderingJPanel(pixelsPerDot);

		DubbleGrid temperature = elements.getTemperature();
		Render renderTemp = new GridRender(temperature, new TemperatureRender(pixelsPerDot, pixelsPerDot));
		Render renderElements = new ElementRender(elements, pixelsPerDot, pixelsPerDot);
		Render renderPressure = new GridRender(elements.getPressure(), new PressureRender(pixelsPerDot, pixelsPerDot));

		renderP.addRenderer(renderElements, "Elements");
		renderP.addRenderer(renderTemp, "Temperature");
		renderP.addRenderer(renderPressure, "Structural pressure");

		elements.ticks.addListener(repainter);

		TickThread ticker = new TickThread(elements);

		new Thread(ticker).start();

		JPanel extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.Y_AXIS));

		ControlsPanel cp = new ControlsPanel(cm, elements, ticker, renderP.getRenderToggles());

		p = new Pencil(elements, renderP.mousePressed, renderP.mouseX, renderP.mouseY, cp.selection, elements.ticks, cp.pencilSize);

		extras.add(cp);

		extras.add(new StatsPanel(cm, elements));

		extras.add(new PresetLevelLoaderControlPanel(elements, ticker, Levels.presets));

		this.add(extras, BorderLayout.WEST);
		this.add(renderP);
		this.pack();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}
}
