package grid;

import static elements.Elements.AIR;
import static utils.Utils.lr;
import static utils.Utils.p;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import reactivity.valueWrappers.IntegerValue;
import elements.Element;
import elements.Elements;
import elements.Reactions;

public class ElementGrid {

	private final DubbleGrid temperature;
	private Element[][] grid;

	private final double gravity = -9.81;
	
	public final IntegerValue ticks = new IntegerValue(0, "Passed ticks");
	public final IntegerValue time	= new IntegerValue(0, "Needed time (ms)");
	
	public ElementGrid(int dotsX, int dotsY) {
		this.temperature = new DubbleGrid(dotsX, dotsY);
		weights = new double[dotsX][dotsY];
		grid = new Element[dotsX][dotsY];
		// target = new Element[dotsX][dotsY];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				// target[x][y] = AIR.behaviour;
				grid[x][y] = AIR.behaviour;
			}
		}

	}

	private final double[][] weights;

	// private Element[][] target;

	public void evolve() {
		long start = System.currentTimeMillis();
		exchangeHeat();
		calcWeightsAndChangeState();
		moveAround();
		interact();
		long stop = System.currentTimeMillis();
		this.time.set((int) (stop - start));
		this.ticks.set(1+ticks.get());
	}

	public void interact() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {

				int x0 = x + lr();
				int y0 = y + lr();

				if (!inBounds(x0, y0)) {
					continue;
				}

				int reactN = grid[x][y].i * Reactions.nrOfElements
						+ grid[x0][y0].i;

				int i = Reactions.reactionIndex[reactN];
				if (i == -1) {
					continue;
				}

				if (p(Reactions.prob[i])) {
					grid[x][y] = Reactions.givesA[i];
					grid[x0][y0] = Reactions.givesB[i];
					temperature.change(x, y, Reactions.enthalpyA[i]);
					temperature.change(x0, y0, Reactions.enthalpyB[i]);
				}

			}
		}
	}

	public void exchangeHeat() {

		int xStart = 0;
		int xInc = +1;

		int yStart = 0;
		int yInc = +1;

		if (p(0.5)) {
			xStart = grid.length - 1;
			xInc = -1;
		}
		if (p(0.5)) {
			yStart = grid[0].length - 1;
			yInc = -1;
		}

		for (int x = xStart; x >= 0 && x < grid.length; x += xInc) {
			for (int y = yStart; y >= 0 && y < grid[0].length; y += yInc) {

				int yi = p(0.5) ? 0 : lr();
				int xi = p(0.5) ? 0 : lr();

				if (!temperature.inBounds(x + xi, y + yi)) {
					continue;
				}
				Element e = grid[x][y];
				Element e0 = grid[x + xi][y + yi];

				double avg = 1 - (1 - e.getHeatInertia())
						* (1 - e0.getHeatInertia());
				if (e.getRepresentation() == e0.getRepresentation()) {
					avg = e.getHeatExchangeSame();
				}

				double diff = temperature.getValue(x + xi, y + yi)
						- temperature.getValue(x, y);
				;
				diff = diff * avg;

				temperature.change(x, y, diff);
				temperature.change(x + xi, y + yi, -diff);

			}
		}

	}

	public Map<Element, Integer> buildHistogram() {

		Map<Element, Integer> hist = new HashMap<>();
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {

				int count = hist.getOrDefault(grid[x][y], 0);
				hist.put(grid[x][y], count + 1);
			}
		}
		return hist;
	}

	public void calcWeightsAndChangeState() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				weights[x][y] = grid[x][y].weight(temperature.getValue(x, y));

				Element e = grid[x][y];
				double t = temperature.getValue(x, y);
				if (p(0.5)) {
					if (p(e.pVapor(t))) {
						grid[x][y] = e.getHeatedState();
						temperature.change(x, y, -e.getEnthalpyHot());
					}
				} else {
					if (p(e.pCondens(t))) {
						grid[x][y] = e.getCooledState();
						temperature.change(x, y, e.getEnthalpyCold());
					}
				}
				if (grid[x][y] == null) {
					throw new NullPointerException("FOUND THE BASTARD: " + e
							+ " t: " + t);
				}

			}
		}
	}

	public void moveAround() {
		int xStart = 0;
		int xInd = +1;

		if (p(0.5)) {
			xStart = grid.length - 1;
			xInd = -1;
		}
		int lastY = grid[0].length - 1;
		int lastX = grid.length - 1;
		for (int x = xStart; x >= 00 && x < grid.length; x += xInd) {
			for (int y = 0; y < lastY; y++) {
				if (!grid[x][y].canBeMoved()) {
					continue; // can not be moved
				}

				if (y != lastY) {
					double downPower = gravity * weights[x][y];
					int downMovement = lr(0.75);

					if (x + downMovement >= 0 && x + downMovement <= lastX) {
						double downPowerOther = gravity
								* weights[x + downMovement][y + 1];
						if (downPowerOther < downPower) {
							double moveability = Math.max(grid[x][y]
									.getMovabiltiy(),
									grid[x + downMovement][y + 1]
											.getMovabiltiy());
							if (p(moveability)) {
								swap(x, y, x + downMovement, y + 1);
							}
						}

					}

				}

				if (p(grid[x][y].getMovabiltiy())) {
					int xi = lr();
					if (xi + x >= 0 && x + xi <= lastX
							&& p(grid[x + xi][y].getMovabiltiy())) {
						swap(x, y, x + xi, y);
					}
				}
			}
		}

	}

	public void swap(int x, int y, int x0, int y0) {
		double th = temperature.getValue(x, y);
		temperature.setValue(x, y, temperature.getValue(x0, y0));
		temperature.setValue(x0, y0, th);

		Element h = grid[x][y];
		grid[x][y] = grid[x0][y0];
		grid[x0][y0] = h;
	}

	public void fillStretch(int x, int y, int w, int h, Elements e) {
		fillStretch(x, y, w, h, e.behaviour);
	}

	public void fillStretch(int x, int y, int w, int h, Element e) {
		if(w == 0 || h == 0){
			throw new IllegalArgumentException("Empty fillstretch");
		}
		for (int xi = 0; xi < w; xi++) {
			for (int yi = 0; yi < h; yi++) {
				grid[x + xi][y + yi] = e;
			}
		}
	}

	public void put(int x, int y, Elements h) {
		put(x, y, h.behaviour);
	}

	public void put(int x, int y, Element h) {
		grid[x][y] = h;
	}

	protected boolean inBounds(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < grid.length)
				&& (y < grid[0].length);
	}

	protected boolean onBorder(int x, int y) {
		return x == 0 || y == 0 || x == (grid.length - 1)
				|| y == (grid[0].length - 1);
	}

	protected boolean onCorner(int x, int y) {
		return ((x == 0 || x == (grid.length - 1)) && (y == 0 || y == (grid[0].length - 1)));
	}

	public Color getColor(int x, int y) {
		return grid[x][y].color();
	}

	public int width() {
		return grid.length;
	}

	public int height() {
		return grid[0].length;
	}

	public DubbleGrid getTemperature() {
		return temperature;
	}

	public Element get(int x, int y) {
		return grid[x][y];
	}

}
