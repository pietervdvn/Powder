package grid;

import static elements.Elements.AIR;
import static utils.Utils.lr;
import static utils.Utils.offset;
import static utils.Utils.p;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import elements.Element;
import elements.Elements;
import elements.Reactions;

public class ElementsGrid extends AbstractGrid<Element> {

	private final Element[][] grid;

	public static final double GRAVITY = 9.81;

	public ElementsGrid(int dotsX, int dotsY) {
		super(dotsX, dotsY);
		grid = new Element[dotsX][dotsY];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				grid[x][y] = AIR.behaviour;
			}
		}
	}

	public void moveAround(FullGrid fg, double[][] weights) {
		int xStart = 0;
		int xInd = +1;

		if (p(0.5)) {
			xStart = grid.length - 1;
			xInd = -1;
		}
		int lastY = grid[0].length - 1;
		int lastX = grid.length - 1;

		Pressure pressure = fg.getPressure();

		for (int x = xStart; x >= 0 && x <= lastX; x += xInd) {
			for (int y = 0; y <= lastY; y++) {
				if (!grid[x][y].canBeMoved()) {
					continue;
				}
				double moveability = grid[x][y].getMovabiltiy();

				// move down due to gravity
				if (y + 1 <= lastY && grid[x][y + 1].canBeMoved()) {
					// force pulling down
					double powDown = weights[x][y] - weights[x][y + 1];
					powDown *= GRAVITY;
					if (powDown > 0 && grid[x][y + 1].canBeMoved()) {
						swap(x, y, x, y + 1);
						// we continue with the element that has come into
						// (x,y)'s place
						moveability = grid[x][y].getMovabiltiy();
					}
				}

				// sidewards force, negative = left
				double powLR = pressure.getValueZ(x + 1, y)
						- pressure.getValueZ(x - 1, y);
				powLR += moveability * offset();
				int xD = +1;
				if (powLR < 0) {
					xD = -1;
				}

				// lets not move too much, bounds check
				if (Math.abs(powLR) < 0.1 || x + xD < 0 || x + xD > lastX
						|| !grid[x + xD][y].canBeMoved()) {
					continue;
				}
				if (p(moveability)) {
					fg.swap(x, y, x + xD, y);
				}

			}
		}
	}

	public void changeState(double[][] temperature, double[][] pressure) {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {

				Element e = grid[x][y];
				double t = temperature[x][y];
				if (p(0.5)) {
					if (p(e.pVapor(t))) {
						grid[x][y] = e.getHeatedState();
						temperature[x][y] += -e.getEnthalpyHot();
						// pressure[x][y] += e.getPressureHot();
					}
				} else {
					if (p(e.pCondens(t))) {
						grid[x][y] = e.getCooledState();
						temperature[x][y] += e.getEnthalpyCold();
						// pressure[x][y] += -e.getPressureCold();
					}
				}
				if (grid[x][y] == null) {
					throw new NullPointerException("FOUND THE BASTARD: " + e
							+ " t: " + t);
				}
			}
		}
	}

	public void interact(double[][] temperature) {
		int xStart = 0;
		int xInd = +1;

		if (p(0.5)) {
			xStart = grid.length - 1;
			xInd = -1;
		}
		int lastY = grid[0].length - 1;
		int lastX = grid.length - 1;

		for (int x = xStart; x >= 0 && x <= lastX; x += xInd) {
			for (int y = 0; y <= lastY; y++) {
				int x0 = x + lr(0.5);
				int y0 = y + lr(0.5);

				if (!inBounds(x0, y0)) {
					continue;
				}

				int reactN = grid[x][y].integerRepresentation * Reactions.nrOfElements
						+ grid[x0][y0].integerRepresentation;

				int i = Reactions.reactionIndex[reactN];
				if (i == -1) {
					continue;
				}

				if (p(Reactions.prob[i])) {
					grid[x][y] = Reactions.givesA[i];
					grid[x0][y0] = Reactions.givesB[i];
					temperature[x][y] += Reactions.enthalpyA[i];
					temperature[x0][y0] += Reactions.enthalpyB[i];
				}

			}
		}
	}

	public Map<Element, Integer> buildHistogram() {
		Map<Element, Integer> hist = new HashMap<>();
		for (int x = 0; x < width(); x++) {
			for (int y = 0; y < height(); y++) {

				int count = hist.getOrDefault(grid[x][y], 0);
				hist.put(grid[x][y], count + 1);
			}
		}
		return hist;
	}

	public Element get(int x, int y) {
		return grid[x][y];
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

	protected Element[][] getRaw() {
		return grid;
	}

	@Override
	public void setValue(int x, int y, Element t) {
		checkBounds(x, y);
		grid[x][y] = t;
	}

	@Override
	public Element getValue(int x, int y) {
		checkBounds(x, y);
		return grid[x][y];
	}

	public void put(int x, int y, Elements t) {
		put(x, y, t.behaviour);
	}

	public void fillStretch(int x, int y, int w, int h, Elements block) {
		fillStretch(x, y, w, h, block.behaviour);
	}
}
