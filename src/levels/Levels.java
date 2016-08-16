package levels;

import static utils.Utils.p;

import java.util.Arrays;
import java.util.List;

import simulation.UsefullFullGrid;

public class Levels {

	public final static AbstractLevel clear = new AbstractLevel("clear", "air", 300) {
		@Override
		public void seed(UsefullFullGrid elements) {
			// Pass
		}
	};

	public final static AbstractLevel vapor = new AbstractLevel("Vapor", "air", 475) {

		@Override
		public void seed(UsefullFullGrid g) {
			for (int x = 0; x < g.dotsX; x++) {
				for (int y = 0; y < g.dotsY; y++) {
					g.spawnValue(x, y, p(0.5) ? "VAPOR" : "AIR");
				}
			}
		}
	};
	

	public final static AbstractLevel vine = new AbstractLevel("Vine", "air", 300) {
		@Override
		public void seed(UsefullFullGrid g) {
			g.fillStretch(0, 1, g.dotsX, 10, "WATER");
			g.fillStretch(0, g.dotsY - 1, g.dotsX, 1, "VINE");
		}
	};

	public final static AbstractLevel heatExchanger = new AbstractLevel("heat exchange", "air",280) {
		@Override
		public void seed(UsefullFullGrid g) {
			int w = g.dotsX;
			int h = g.dotsY;

			g.fillStretch(0, h / 2, w, h / 4, "WATER");
			g.fillStretch(0, 1, w, h / 4, "VAPOR");
			g.fillStretch(0, h - 1, w, 1, "VAPORIZER");
			g.fillStretch(0, 0, w, 1, "CONDENSOR");
		}
	};

	public final static AbstractLevel halfHeatExchanger = new AbstractLevel("half heat exchange", "air", 280) {
		@Override
		public void seed(UsefullFullGrid g) {
			int w = g.dotsX;
			int h = g.dotsY;

			g.fillStretch(0, h / 2, w, h / 4, "WATER");
			g.fillStretch(0, 1, w, h / 4,     "VAPOR");
			g.fillStretch(0, h - 1, w / 2, 1, "VAPORIZER");
			g.fillStretch(w / 2, 0, w / 2, 1, "CONDENSOR");
		}
	};

	public final static AbstractLevel beach = new AbstractLevel("beach","air",300) {
		@Override
		public void seed(UsefullFullGrid elements) {
			int w = elements.dotsX;
			int h = elements.dotsY;
			
			elements.fillStretch(0, 0, w, h, "AIR");
			
			elements.fillStretch(0, h - 1, w, 1, "BLOCK");
			elements.fillStretch(0, 0, 1, h,     "BLOCK");
			elements.fillStretch(w - 1, 0, 1, h, "BLOCK");

			elements.fillStretch(1, h / 2, w - 2, h / 2, "WATER");

			elements.fillStretch(7 * w / 8, 0, w / 8, 20, "SALT");

			elements.fillStretch(1, 1 + h / 3, w / 2, (2 * h) / 3, "WET_GROUND");

			elements.fillStretch(1, 10, 10, 5, "WET_GROUND");
			elements.fillStretch(1, 15, 70, 5, "WET_GROUND");
			elements.fillStretch(1, 20, 30, 5, "WET_GROUND");
			elements.fillStretch(1, 25, 50, 5, "WET_GROUND");
			elements.fillStretch(1, 30, 90, 5, "WET_GROUND");

			elements.fillStretch(1, 1, 50, 10, "WATER");
			
		}
	};

	public final static List<AbstractLevel> presets = Arrays.asList(clear, vapor, vine, heatExchanger,
			halfHeatExchanger, beach);

}
