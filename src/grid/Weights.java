package grid;

import elements.Element;

public class Weights extends Grid {

	public Weights(int dotsX, int dotsY) {
		super(dotsX, dotsY);
	}

	public void calcWeights(final double[][] temperature,
			final Element[][] elements) {
		for (int x = 0; x < width(); x++) {
			for (int y = 0; y < height(); y++) {
				this.grid[x][y] = elements[x][y].weight(temperature[x][y]);
				if(Double.isNaN(this.grid[x][y])){
					this.grid[x][y] = 0.0;
				}
			}
		}
	}

}
