import static elements.Elements.*;
import static elements.Elements.SALT;
import static elements.Elements.SAND;
import static elements.Elements.WATER;
import elements.Elements;
import grid.DubbleGrid;
import grid.ElementGrid;

import java.util.Random;

import javax.swing.JFrame;

import rendering.ElementRender;
import rendering.GridRender;
import rendering.RenderingJPanel;
import rendering.TemperatureRender;

public class Main {

	public static void seedR(ElementGrid elements, int w, int h) {
		Random r = new Random();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int i = r.nextInt(Elements.values().length - 1) + 1;
				elements.fillStretch(x, y, 1, 1, Elements.values()[i]);
				elements.getTemperature().setValue(x, y, r.nextInt(600));

			}
		}
	}

	public static void seedS(ElementGrid g, int w, int h) {
		g.fillStretch(0, h-16, w, 16, WATER);
		g.fillStretch(0, 1, w, 16, VAPOR);
		g.fillStretch(0, h-1, w, 1, VAPORIZER);
		g.fillStretch(0, 0, w, 1, CONDENSOR);
		
		g.getTemperature().reset(280);
	}

	public static void seed(ElementGrid elements, int w, int h) {
		DubbleGrid temp = elements.getTemperature();
		elements.fillStretch(0, h - 1, w, 1, BLOCK);
		elements.fillStretch(0, 0, 1, h, BLOCK);
		elements.fillStretch(w - 1, 0, 1, h, BLOCK);

		elements.fillStretch(1, h / 2, w - 2, h / 2, WATER);

		elements.fillStretch(1, 1 + h / 3, 2 * w / 3, (2 * h) / 3,SALT);

	//	elements.fillStretch(3 * w / 4, 0, w / 4, 20, SALT);

		elements.fillStretch(1, 5, 90, 5, SAND);
		elements.fillStretch(1, 10, 10, 5, SAND);

		elements.fillStretch(1, 15, 70, 5, SAND);

		elements.fillStretch(1, 20, 30, 5, SAND);

		elements.fillStretch(1, 25, 50, 5, SAND);

		temp.reset(280);

		for (int x = 0; x < w; x++) {
			// temp.setValue(x, 49, 0);
			// temp.setValue(x, 48, 0);
			// temp.setValue(x, 47, 0);

		}
		for (int y = 0; y < h; y++) {
			// temp.setValue(1, y, 0);
		}

	}

	public static void main(String[] args) {
		try {
			main0(args);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main0(String[] args) throws InterruptedException {

		boolean showTempFrame = true;
		boolean showGridFrame = true;

		int dotsX = 200;
		int dotsY = 50;

		int pixelsTargetX = 1600;
		int pixelsDot = pixelsTargetX / dotsX;

		JFrame frame = new JFrame("Powder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFrame frameT = new JFrame("Powder Temperature");
		frameT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ElementGrid elements = new ElementGrid(dotsX, dotsY);
		DubbleGrid temperature = elements.getTemperature();

		RenderingJPanel p = new RenderingJPanel();
		frame.setSize(dotsX * pixelsDot + 5, dotsY * pixelsDot + 15);
		frameT.setSize(dotsX * pixelsDot + 5, dotsY * pixelsDot + 15);

		TemperatureRender renderTemp = new TemperatureRender(pixelsDot,
				pixelsDot);
		p.addRenderer(new ElementRender(elements, pixelsDot, pixelsDot));
		frame.add(p);

		RenderingJPanel pt = new RenderingJPanel();
		pt.addRenderer(new GridRender(temperature, renderTemp));

		// seedR(elements, dotsX, dotsY);
		seedS(elements, dotsX, dotsY);

		frame.setVisible(showGridFrame);
		frame.repaint();
		frame.repaint();
		frameT.add(pt);
		frameT.setVisible(showTempFrame);
		Thread.sleep(1000);

		for (int i = 0; i <= Integer.MAX_VALUE; i++) {

			long time = System.currentTimeMillis();

			if(i == 1000){
				elements.fillStretch(0, 1, dotsX, dotsY-2, AIR);
			}
			
			elements.evolve();
			p.repaint();
			if (showTempFrame) {
				pt.repaint();
			}
			if (showGridFrame) {
				frame.repaint();
			}
			long now = System.currentTimeMillis();
			System.out.println(i+"AVG temp: "+temperature.sum()/(dotsX*dotsY));
			Thread.sleep(10);
		}
		System.exit(0);

	}
}
