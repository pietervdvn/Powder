package levels;

import java.util.Arrays;
import java.util.List;

import static elements.Elements.*;
import grid.ElementsGrid;
import grid.Temperature;

import static utils.Utils.*;

public class Levels {

	public final static Level magma = new Level() {

		@Override
		public void seed(ElementsGrid g, Temperature temperature) {
			int w = g.width();
			int h = g.height();
			for (int x = 0; x < w; x++) {
				for (int y = h / 2; y < h; y++) {
					g.put(x, y, p(0.5) ? MAGMA : MAGMA_0);
				}
			}
			temperature.reset(400);
		}

		@Override
		public String name() {
			return "Magma";
		}
	};

	public final static Level vapor = new Level() {

		@Override
		public void seed(ElementsGrid g, Temperature temperature) {
			for (int x = 0; x < g.width(); x++) {
				for (int y = 0; y < g.height(); y++) {
					g.put(x, y, p(0.5) ? VAPOR : AIR);
				}
			}
			temperature.reset(475);
		}

		@Override
		public String name() {
			return "Vapor";
		}
	};

	public final static Level vine = new Level() {

		@Override
		public void seed(ElementsGrid g, Temperature temperature) {
			g.fillStretch(0, 1, g.width(), 10, WATER);
			g.fillStretch(0, 0, g.width(), 1, RAIN_MAKER);
			g.fillStretch(0, g.height() / 2, g.width() / 3, 1, BLOCK);

			g.fillStretch(0, g.height() - 1, g.width(), 1, VINE);
			temperature.reset(300);
		}

		@Override
		public String name() {
			return "Vine";
		}
	};

	public final static Level heatExchanger = new Level() {

		@Override
		public void seed(ElementsGrid g, Temperature temperature) {
			int w = g.width();
			int h = g.height();

			g.fillStretch(0, h / 2, w, h / 4, WATER);
			g.fillStretch(0, 1, w, h / 4, VAPOR);
			g.fillStretch(0, h - 1, w, 1, VAPORIZER);
			g.fillStretch(0, 0, w, 1, CONDENSOR);

			temperature.reset(280);
		}

		@Override
		public String name() {
			return "Heat exchange";
		}
	};

	public final static Level halfHeatExchanger = new Level() {

		@Override
		public void seed(ElementsGrid g, Temperature temperature) {
			int w = g.width();
			int h = g.height();

			g.fillStretch(0, h / 2, w, h / 4, WATER);
			g.fillStretch(0, 1, w, h / 4, VAPOR);
			g.fillStretch(0, h - 1, w / 2, 1, VAPORIZER);
			g.fillStretch(w / 2, 0, w / 2, 1, CONDENSOR);

			temperature.reset(280);
		}

		@Override
		public String name() {
			return "Half heat exchange";
		}
	};

	public final static Level beach = new Level() {

		@Override
		public void seed(ElementsGrid elements, Temperature temperature) {
			int w = elements.width();
			int h = elements.height();
			elements.fillStretch(0, h - 1, w, 1, BLOCK);
			elements.fillStretch(0, 0, 1, h, BLOCK);
			elements.fillStretch(w - 1, 0, 1, h, BLOCK);

			elements.fillStretch(1, h / 2, w - 2, h / 2, WATER);

			elements.fillStretch(7 * w / 8, 0, w / 8, 20, SALT);

			elements.fillStretch(1, 1 + h / 3, w / 2, (2 * h) / 3, SAND);

			elements.fillStretch(1, 10, 10, 5, SAND);
			elements.fillStretch(1, 15, 70, 5, SAND);
			elements.fillStretch(1, 20, 30, 5, SAND);
			elements.fillStretch(1, 25, 50, 5, SAND);
			elements.fillStretch(1, 30, 90, 5, SAND);

			elements.fillStretch(1, 1, 50, 10, WATER);
			elements.fillStretch(0, 0, 1, h, CONDENSOR);

			temperature.reset(300);
		}

		@Override
		public String name() {
			return "Beach";
		}
	};

	public final static List<Level> presets = Arrays.asList(magma, vapor, vine, heatExchanger, halfHeatExchanger,
			beach);

}
