package gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.grid.Grid;
import gui.rendering.ElementRender;
import gui.rendering.GridRender;
import gui.rendering.PressureRender;
import gui.rendering.Render;
import gui.rendering.TemperatureRender;
import levels.Levels;
import reactivity.ValueListener;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;
import simulation.Element;
import simulation.UsefullFullGrid;

public class PowderWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private final ValueListener<Integer> repainter = new ValueListener<Integer>() {

		@Override
		public void onValueChanged(Value<Integer> source, Integer newValue) {
			repaint();
		}
	};

	public PowderWindow(GUIColorModel cm, UsefullFullGrid elements, Element[] knownElements, int pixelsPerDot) {

		RenderingJPanel renderP = new RenderingJPanel(pixelsPerDot);

		Grid temperature = new Grid(elements.temperature0);
		Render renderTemp = new GridRender(temperature, new TemperatureRender(pixelsPerDot, pixelsPerDot));
		Render renderElements = new ElementRender(elements, pixelsPerDot, pixelsPerDot);
		Render renderPressure = new GridRender(new Grid(elements.staticPressure), new PressureRender(pixelsPerDot, pixelsPerDot));

		renderP.addRenderer(renderElements, "Elements");
		renderP.addRenderer(renderTemp, "Temperature");
		renderP.addRenderer(renderPressure, "Structural pressure");

		elements.ticks.addListener(repainter);

		TickThread ticker = new TickThread(elements);

		new Thread(ticker).start();

		JPanel extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.Y_AXIS));

		ControlsPanel cp = new ControlsPanel(cm, elements, ticker, renderP.getRenderToggles(), knownElements);

		new Pencil(elements, renderP.mousePressed, renderP.mouseX, renderP.mouseY, cp.selection, elements.ticks, cp.pencilSize);

		extras.add(cp);

		extras.add(new StatsPanel(cm, elements, temperature));

		extras.add(new PresetLevelLoaderControlPanel(elements, ticker, Levels.presets));

		this.add(extras, BorderLayout.WEST);
		this.add(renderP);
		this.pack();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}
}
