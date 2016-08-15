package grid;

import java.awt.Color;

import elements.Element;
import elements.Elements;
import levels.AbstractLevel;
import reactivity.valueWrappers.IntegerValue;

public class FullGrid extends AbstractGrid<Element> {

	private final Temperature temperature;
	private final ElementsGrid elements;
	private final Weights weights;

	// structural pressure, induced by gravity
	private final Pressure basePressure;
	// keeps track of what basepressure columns should be recalculated
	private final double[] taintedColumns;
	// keeps track of what elements don't move - and thus don't contribute to structural pressure
	private final int[][] moveable;

	// pressure caused by explosions, vaporization, ...
	private final Pressure incidentalPressure;

	public final IntegerValue ticks = new IntegerValue(0, "Passed ticks");
	public final IntegerValue time = new IntegerValue(0, "Needed time (ms)");
	public final IntegerValue fixedColumns = new IntegerValue(0,
			"Columns of which pressure changed");

	public FullGrid(int dotsX, int dotsY) {
		super(dotsX, dotsY);
		this.weights = new Weights(dotsX, dotsY);
		this.elements = new grid.ElementsGrid(dotsX, dotsY);
		this.temperature = new Temperature(dotsX, dotsY);
		this.basePressure = new Pressure(dotsX, dotsY);
		this.incidentalPressure = new Pressure(dotsX, dotsY);
		this.taintedColumns = new double[dotsX];
		this.moveable = new int[dotsX][dotsY];
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				moveable[x][y] = 1;
			}
		}
	}

	public void init(AbstractLevel l) {
		reset(Elements.AIR.behaviour);
		getTemperature().reset(275);
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				moveable[x][y] = 1;
			}
		}
		l.seed(elements, temperature);

		weights.calcWeights(temperature.getRaw(), elements.getRaw());
		basePressure.calculateStructuralPressure(moveable, weights.getRaw());
		for (int i = 0; i < taintedColumns.length; i++) {
			taintedColumns[i] = 0.0;
		}
		ticks.set(0);
	}

	public void evolve() {
		long start = System.currentTimeMillis();
		tick();
		long stop = System.currentTimeMillis();
		this.time.set((int) (stop - start));
		this.ticks.set(1 + ticks.get());
	}

	public void evolve(IntegerValue numberOfTicks) {
		long start = System.currentTimeMillis();
		int max = numberOfTicks.get();
		for (int i = 0; i < max; i++) {
			numberOfTicks.set(i);
			tick();
		}
		long stop = System.currentTimeMillis();
		this.time.set((int) (stop - start) / max);
		this.ticks.set(max + ticks.get());
	}

	/*
	 * simple tick, no measurements or external updates
	 */
	private void tick() {
		temperature.exchangeHeat(elements.getRaw());
		weights.calcWeights(temperature.getRaw(), elements.getRaw());
		elements.changeState(temperature.getRaw(), basePressure.getRaw());
		elements.interact(temperature.getRaw());
		elements.moveAround(this, weights.getRaw());

		int fixedCols = basePressure.calculateStructuralPressure(moveable, 
				weights.getRaw(), 10.0, taintedColumns);
		this.fixedColumns.set(fixedCols);


	}

	public void swap(int x, int y, int x0, int y0) {
		temperature.swap(x, y, x0, y0);
		elements.swap(x, y, x0, y0);
		weights.swap(x, y, x0, y0);

		incidentalPressure.swap(x, y, x0, y0);

		double wDiff = Math.abs(weights.grid[x][y] - weights.grid[x0][y0]);
		taintedColumns[x] += wDiff;
		taintedColumns[x0] += wDiff;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	@Override
	public void setValue(int x, int y, Element t) {
		elements.setValue(x, y, t);
		moveable[x][y] = t.canBeMoved() ? 1 : 0;
	}

	@Override
	public Element getValue(int x, int y) {
		return elements.get(x, y);
	}

	public void put(int x, int y, Elements t) {
		put(x, y, t.behaviour);
	}

	public void fillStretch(int x, int y, int w, int h, Elements value) {
		fillStretch(x, y, w, h, value.behaviour);
	}

	public Color getColor(int x, int y) {
		return elements.getColor(x, y);
	}

	public ElementsGrid getElements() {
		return elements;
	}

	public Pressure getPressure() {
		return basePressure;
	}

}
