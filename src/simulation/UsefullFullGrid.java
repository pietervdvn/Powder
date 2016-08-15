package simulation;

import java.awt.Color;

import levels.AbstractLevel;
import reactivity.valueWrappers.IntegerValue;

/**
 * Wrapper around full grid, to make it usefull with the outer world
 * 
 * @author pietervdvn
 *
 */
public class UsefullFullGrid extends Simulation {

	public final IntegerValue ticks = new IntegerValue(0, "Clock");
	public final IntegerValue neededTime = new IntegerValue(0, "needed time for latest update (ms)");

	public UsefullFullGrid(ElementIndexing knownElements, DecayCache dc, ReactionCache reactions, int dotsX,
			int dotsY) {
		super(dotsX, dotsY, knownElements, dc, reactions);
	}

	/**
	 * Perform step update, increment counter
	 */
	public void tick() {
		// TODO Auto-generated method stub

		long start = System.currentTimeMillis();

		evolve();

		long stop = System.currentTimeMillis();

		neededTime.set((int) (stop - start));
		ticks.set(ticks.get() + 1);
	}

	public void tick(IntegerValue progressMeas) {

		int total = progressMeas.get();

		long start = System.currentTimeMillis();

		for (int i = 1; i < total; i++) {
			evolve();
			progressMeas.set(i);

		}
		long stop = System.currentTimeMillis();

		neededTime.set((int) (stop - start));
		ticks.set(ticks.get() + total);
	}

	public void init(AbstractLevel l) {
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				temperature[x][y] = l.defaultTemp;
				spawnValue(x, y, l.defaultElement);
			}
		}
		l.seed(this);
		forceCleanCalculate();
		if (ticks.get() == 0) {
			ticks.throwEvent();
		}
		ticks.set(0);
	}

	public void reset(Element seed) {
		fillStretch(0, 0, dotsX, dotsY, seed);
	}

	public void fillStretch(int x, int y, int w, int h, String element) {
		Element e = indexer.fromName(element);
		fillStretch(x, y, w, h, e);
	}

	public void spawnValue(int x, int y, String element) {
		Element e = indexer.fromName(element);
		spawnValue(x, y, e);
	}

	public void fillStretch(int x, int y, int w, int h, Element value) {
		if (w == 0 || h == 0) {
			throw new IllegalArgumentException("Empty fillstretch");
		}
		for (int xi = x; xi < x + w; xi++) {
			for (int yi = y; yi < y + h; yi++) {
				spawnValue(xi, yi, value);
			}
		}
	}

	public int width() {
		return dotsX;
	}

	public int height() {
		return dotsY;
	}

	public Color getColor(int x, int y) {
		return grid[x][y].color;
	}
}
