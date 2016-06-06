import static elements.Elements.BLOCK;
import static elements.Elements.CONDENSOR;
import static elements.Elements.SALT;
import static elements.Elements.SAND;
import static elements.Elements.VAPOR;
import static elements.Elements.VAPORIZER;
import static elements.Elements.WATER;
import elements.Elements;
import grid.DubbleGrid;
import grid.ElementGrid;
import grid.ElementGridSeed;

import java.util.Random;
import static utils.Utils.*;
public class Levels {

	public final static ElementGridSeed magma = new ElementGridSeed() {
		
		@Override
		public void seed(ElementGrid g) {
			int w = g.width();
			int h = g.height();
			for (int x = 0; x < w; x++) {
				for (int y = h/2; y < h; y++) {
					g.put(x, y, p(0.5) ? Elements.MAGMA : Elements.MAGMA_0);
				}
			}
			g.getTemperature().reset(400);
		}
		
		@Override
		public String name() {
			return "Magma";
		}
	};
	
	
	
	public final static ElementGridSeed randomness = new ElementGridSeed() {

		@Override
		public void seed(ElementGrid g) {
			Random r = new Random();
			int w = g.width();
			int h = g.height();

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int i = r.nextInt(Elements.values().length - 1) + 1;
					g.fillStretch(x, y, 1, 1, Elements.values()[i]);
					g.getTemperature().setValue(x, y, r.nextInt(600));

				}
			}
		}

		@Override
		public String name() {
			return "Randomness!";
		}
	};

	public final static ElementGridSeed heatExchanger = new ElementGridSeed() {

		@Override
		public void seed(ElementGrid g) {
			int w = g.width();
			int h = g.height();

			g.fillStretch(0, h / 2, w, h / 2, WATER);
			g.fillStretch(0, 1, w, h / 2, VAPOR);
			g.fillStretch(0, h - 1, w, 1, VAPORIZER);
			g.fillStretch(0, 0, w, 1, CONDENSOR);

			g.getTemperature().reset(280);
		}

		@Override
		public String name() {
			return "Heat exchange";
		}
	};

	public final static ElementGridSeed beach = new ElementGridSeed() {

		@Override
		public void seed(ElementGrid elements) {
			int w = elements.width();
			int h = elements.height();
			DubbleGrid temp = elements.getTemperature();
			elements.fillStretch(0, h - 1, w, 1, BLOCK);
			elements.fillStretch(0, 0, 1, h, BLOCK);
			elements.fillStretch(w - 1, 0, 1, h, BLOCK);

			elements.fillStretch(1, h / 2, w - 2, h / 2, WATER);


			elements.fillStretch(7 * w / 8, 0, w / 8, 20, SALT);

			elements.fillStretch(1, 1 + h / 3, 2 * w / 3, (2 * h) / 3, SAND);
			elements.fillStretch(1, 5, 90, 5, SAND);
			elements.fillStretch(1, 10, 10, 5, SAND);

			elements.fillStretch(1, 15, 70, 5, SAND);

			elements.fillStretch(1, 20, 30, 5, SAND);

			elements.fillStretch(1, 25, 50, 5, SAND);

			temp.reset(280);
		}

		@Override
		public String name() {
			return "Beach";
		}
	};

}
