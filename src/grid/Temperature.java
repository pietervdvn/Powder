package grid;

import static utils.Utils.lr;
import static utils.Utils.p;
import elements.Element;

public class Temperature extends DubbleGrid{

	public Temperature(int dotsX, int dotsY) {
		super(dotsX, dotsY);
	}
	
	public void exchangeHeat(Element[][] elements) {

		int xStart = 0;
		int xInc = +1;

		int yStart = 0;
		int yInc = +1;

		if (p(0.5)) {
			xStart = width() - 1;
			xInc = -1;
		}
		if (p(0.5)) {
			yStart = height() - 1;
			yInc = -1;
		}

		for (int x = xStart; x >= 0 && x < width(); x += xInc) {
			for (int y = yStart; y >= 0 && y < height(); y += yInc) {

				int yi = p(0.5) ? 0 : lr();
				int xi = p(0.5) ? 0 : lr();

				if (!inBounds(x + xi, y + yi)) {
					continue;
				}
				
				
				Element e = elements[x][y];
				Element e0 = elements[x + xi][y + yi];

				double avg = 1 - (1 - e.getHeatInertia())
						* (1 - e0.getHeatInertia());
				if (e.getRepresentation() == e0.getRepresentation()) {
					avg = e.getHeatExchangeSame();
				}

				double diff = this.getValue(x + xi, y + yi)
						- this.getValue(x, y);
				;
				diff = diff * avg;

				this.change(x, y, diff);
				this.change(x + xi, y + yi, -diff);

			}
		}

	}
}
