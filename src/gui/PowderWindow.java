package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

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
import reactivity.valueWrappers.BooleanValue;
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

		RenderingJPanel renderP1 = new RenderingJPanel(pixelsPerDot);
		RenderingJPanel renderP2 = new RenderingJPanel(pixelsPerDot);
		RenderingJPanel renderP3 = new RenderingJPanel(pixelsPerDot);

		Grid temperature = new Grid(elements.temperature);
		Render renderTemp = new GridRender(temperature, new TemperatureRender(pixelsPerDot, pixelsPerDot));
		Render renderElements = new ElementRender(elements, pixelsPerDot, pixelsPerDot);
		Render renderPressure = new GridRender(new Grid(elements.staticPressure),
				new PressureRender(pixelsPerDot, pixelsPerDot));

		renderP1.addRenderer(renderElements, "Elements");
		renderP2.addRenderer(renderTemp, "Temperature");
		renderP3.addRenderer(renderPressure, "Structural pressure");

		elements.ticks.addListener(repainter);

		TickThread ticker = new TickThread(elements);

		new Thread(ticker).start();

		JPanel extras = new JPanel();
		extras.setLayout(new BoxLayout(extras, BoxLayout.Y_AXIS));

		List<BooleanValue> toggles = new ArrayList<>();
		toggles.addAll(renderP1.getRenderToggles());
		toggles.addAll(renderP2.getRenderToggles());
		toggles.addAll(renderP3.getRenderToggles());

		ControlsPanel cp = new ControlsPanel(cm, elements, ticker, toggles, knownElements);

		new Pencil(elements, renderP1.mousePressed, renderP1.mouseX, renderP1.mouseY, cp.selection, elements.ticks,
				cp.pencilSize);
		new Pencil(elements, renderP2.mousePressed, renderP2.mouseX, renderP2.mouseY, cp.selection, elements.ticks,
				cp.pencilSize);
		new Pencil(elements, renderP3.mousePressed, renderP3.mouseX, renderP3.mouseY, cp.selection, elements.ticks,
				cp.pencilSize);

		extras.add(cp);

		extras.add(new StatsPanel(cm, elements, temperature, renderP1.mouseX, renderP1.mouseY));

		extras.add(new PresetLevelLoaderControlPanel(elements, ticker, Levels.presets));

		this.add(extras, BorderLayout.WEST);

		JPanel renders = new JPanel();
		renders.setLayout(new FlowLayout());
		renders.add(renderP1);
		renders.add(renderP2);
		renders.add(renderP3);
		this.add(renders);
		
		this.pack();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}
}
