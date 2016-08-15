package grid2;

import static utils.Utils.lr;
import static utils.Utils.p;

/**
 * The class holding all matrices to work on - and doing all calculations
 *
 */
public class FullGrid {

	public final int dotsX, dotsY;

	/*
	 * What element is on what location? Multirepresentations
	 */
	public final int[][] elements;
	/*
	 * Behaviour corresponding with elements-baseindex
	 */
	public final Element[][] grid;
	// if zero: can't move -> doesn't contribute to static pressure. This is
	// meta
	private final int[][] moveable;
	private final static double MOVEMENT_THRESHOLD = 0.0;
	// both temperature and weight get swapped when elements swap
	public double[][] temperature0;
	public double[][] temperature1;
	private int timeSinceTemperatureUpdate = 0;
	// exchanging heat each frame ~= exchanging twice the heat, every two frames
	private final static int TEMPERATURE_UPDATE_TIME = 5;

	private final double[][] weight;
	public static final double GRAVITY = 9.81;

	/*
	 * Cumulative weight difference per column. If it's big enough, static
	 * pressure get's recalculated for that column
	 */
	private final double[] weightDiff;
	// if a columns weight's diff exceeds this value, the static pressure is
	// recalculated
	private final static double WEIGHT_DIFF_THRESHOLD = 0;

	// downward pressure
	public final double[][] staticPressure;

	// means an external change has happened (e.g. mouse painted something) ->
	// static pressure should be recalculated
	private boolean dirty = false;
	protected final ElementIndexing indexer;
	protected final ReactionCache reactions;

	public FullGrid(int dotsX, int dotsY, ElementIndexing indexer, ReactionCache reactions) {
		this.dotsX = dotsX;
		this.dotsY = dotsY;
		this.indexer = indexer;
		this.reactions = reactions;

		elements = new int[dotsX][dotsY];
		grid = new Element[dotsX][dotsY];
		moveable = new int[dotsX][dotsY];
		temperature0 = new double[dotsX][dotsY];
		temperature1 = new double[dotsX][dotsY];

		weight = new double[dotsX][dotsY];
		weightDiff = new double[dotsX];
		staticPressure = new double[dotsX][dotsY];

		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				setValue(x, y, indexer.getBaseElement(1));
			}
		}

	}

	// Clean evolve of all the grids (thus no interfacing with the outside
	// world)
	public void evolve() {

		// temperature might only update once in a while. If the diff is big
		// enough, we also update the weights
		boolean temperatureUpdateDiff = exchangeHeat();

		if (dirty) {
			forceCleanCalculate();
			dirty = false;
		}

		if (temperatureUpdateDiff) {
			calculateWeightMatrix();
		}

		/*
		 * Based on temperature TODO include pressure
		 */
		changeStates();

		// chemical reactions
		interact();

		// move particles
		moveAroundPerGravity();

		// calculate static pressure if needed
		calculateStaticPressure();
		moveAroundPerPressure();

	}

	private boolean exchangeHeat() {
		timeSinceTemperatureUpdate++;
		if (timeSinceTemperatureUpdate < TEMPERATURE_UPDATE_TIME) {
			return false;
		}
		// if we lag 5 update ticks, temperature exchange should be 5 times as
		// strong
		int pFactor = timeSinceTemperatureUpdate;

		timeSinceTemperatureUpdate = 0;

		// randomization of the calculation orders - left right vs right left

		int xStart = 0;
		int xInc = +1;

		int yStart = 0;
		int yInc = +1;

		if (p(0.5)) {
			xStart = dotsX - 1;
			xInc = -1;
		}
		if (p(0.5)) {
			yStart = dotsY - 1;
			yInc = -1;
		}

		for (int x = xStart; x >= 0 && x < dotsX; x += xInc) {
			for (int y = yStart; y >= 0 && y < dotsY; y += yInc) {

				int yi = p(0.5) ? 0 : lr();
				int xi = p(0.5) ? 0 : lr();

				if (!inBounds(x + xi, y + yi)) {
					continue;
				}

				Element e = grid[x][y];
				Element e0 = grid[x + xi][y + yi];

				double avg = 1 - (1 - e.heatExchange) * (1 - e0.heatExchange);

				if (e.id == e0.id) {
					avg = e.heatExchangeSame;
				}
				avg *= pFactor;

				while (avg >= 0.5) {
					double diff = (temperature0[x + xi][y + yi] - temperature0[x][y]) / 2;
					temperature0[x][y] += diff;
					temperature0[x + xi][y + yi] -= diff;
					avg -= 0.5;
				}

				double diff = temperature0[x + xi][y + yi] - temperature0[x][y];
				diff = diff * avg;

				temperature0[x][y] += diff;
				temperature0[x + xi][y + yi] -= diff;

			}
		}

		return true;
	}

	private void calculateWeightMatrix() {
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				Element e = indexer.getBaseElement(elements[x][y]);
				double t = e.weight(temperature0[x][y]);
				weightDiff[x] += Math.abs(weight[x][y] - t);
				weight[x][y] = t;
			}
		}

	}

	private void changeStates() {
		// TODO Auto-generated method stub
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				Element e = grid[x][y];
				if (e.vaporPoint <= temperature0[x][y] && e.heatedState > 0) {
					setValue(x, y, e.heatedState);
					temperature0[x][y] -= e.enthalpyHot * e.densityAtHighest;
				}
				if (e.condensPoint >= temperature0[x][y] && e.cooledState > 0) {
					setValue(x, y, e.cooledState);
					temperature0[x][y] += e.enthalpyCold;
				}

			}
		}

	}

	private void interact() {
		for (int x0 = 0; x0 < dotsX; x0++) {
			for (int y0 = 0; y0 < dotsY; y0++) {

				int x1 = x0 + lr();
				int y1 = y0 + lr();

				if (!inBounds(x1, y1)) {
					continue;
				}

				Reaction r = reactions.getReactionFor(elements[x0][y0], elements[x1][y1]);
				if (r == null) {
					continue;
				}
				if (!p(r.prob)) {
					continue;
				}
				setValue(x0, y0, r.result0);
				setValue(x1, y1, r.result1);

			}
		}

	}

	private void moveAroundPerGravity() {

		int xStart = 0;
		int xInd = +1;

		if (p(0.5)) {
			xStart = dotsX - 1;
			xInd = -1;
		}
		int lastY = dotsY - 1;
		int lastX = dotsX - 1;

		for (int x = xStart; x >= 0 && x <= lastX; x += xInd) {
			for (int y = lastY; y >= 0; y--) {
				if (moveable[x][y] == 0) {
					continue;
				}

				if (y == lastY) {
					continue;
				}

				// move down due to gravity
				if (y + 1 <= lastY) {
					boolean elsCanBeMoved = 0 == (grid[x][y].fixed + grid[x][y + 1].fixed);
					if (!elsCanBeMoved) {
						continue;
					}

					double moveability = grid[x][y].movability * grid[x][y + 1].movability;
					if (elements[x][y] == elements[x][y + 1]) {
						moveability = grid[x][y].movabilitySame;
					}

					// force pulling down
					double powDown = weight[x][y] - weight[x][y + 1];
					powDown *= GRAVITY;

					if (powDown > 0 && p(moveability)) {
						swap(x, y, x, y + 1);
					}
				}

			}
		}
	}

	private void moveAroundPerPressure() {
		int xStart = 1;
		int xInd = +1;

		if (p(0.5)) {
			xStart = dotsX - 2;
			xInd = -1;
		}

		for (int x = xStart; x < dotsX-1 && x > 1; x += xInd) {
			for (int y = 1; y < dotsY-1; y++) {
					
				int x0 =  x +lr();
				
				// negative = move to the left
				double powLR = Math.abs(staticPressure[x][y] - staticPressure[x0][y]) ;
				
				if(powLR < MOVEMENT_THRESHOLD){
					continue;
				}
				
				double moveability = grid[x][y].movability * grid[x0][y].movability;
				if(elements[x][y] == elements[x0][y]){
					moveability = grid[x][y].movability;
				}

				if(p(powLR*moveability)){
					swap(x,y,x0,y);
				}
			}
		}

	}

	private void calculateStaticPressure() {
		for (int x = 0; x < dotsX; x++) {
			if (weightDiff[x] < WEIGHT_DIFF_THRESHOLD) {
				continue;
			}
			weightDiff[x] = 0;

			staticPressure[x][0] = weight[x][0];
			for (int y = 1; y < dotsY; y++) {
				staticPressure[x][y] = moveable[x][y] * staticPressure[x][y - 1] + weight[x][y] * GRAVITY;
			}
		}
	}

	public void swap(int x, int y, int x0, int y0) {
		swapMatrix(grid, x, y, x0, y0);
		swapMatrix(temperature0, x, y, x0, y0);
		swapMatrix(temperature1, x, y, x0, y0);

		swapMatrix(elements, x, y, x0, y0);
		swapMatrix(weight, x, y, x0, y0);
		if (x != x0) {
			double wd = Math.abs(weight[x][y] - weight[x0][y0]);
			weightDiff[x] = wd;
			weightDiff[x0] = wd;
		}

		swapMatrix(moveable, x, y, x0, y0);
	}

	private static void swapMatrix(double[][] matrix, int x, int y, int x0, int y0) {
		double h = matrix[x][y];
		matrix[x][y] = matrix[x0][y0];
		matrix[x0][y0] = h;
	}

	private static void swapMatrix(int[][] matrix, int x, int y, int x0, int y0) {
		int h = matrix[x][y];
		matrix[x][y] = matrix[x0][y0];
		matrix[x0][y0] = h;
	}

	private static <T> void swapMatrix(T[][] matrix, int x, int y, int x0, int y0) {
		T h = matrix[x][y];
		matrix[x][y] = matrix[x0][y0];
		matrix[x0][y0] = h;
	}

	/*
	 * Makes sure pressures are correct - e.g. after loading a level
	 */
	public void forceCleanCalculate() {
		// force clean calculation
		timeSinceTemperatureUpdate = TEMPERATURE_UPDATE_TIME;
		for (int i = 0; i < weightDiff.length; i++) {
			weightDiff[i] = WEIGHT_DIFF_THRESHOLD + 1;
		}
		dirty = false;
		calculateWeightMatrix();
		calculateStaticPressure();
	}

	/**
	 * External call. Might dirty the environment
	 * 
	 * @param x
	 * @param y
	 * @param e
	 */
	public void setValue(int x, int y, Element e) {
		if (e == null) {
			throw new NullPointerException("The passed in element should never be null");
		}
		if (!inBounds(x, y)) {
			return;
		}
		dirty = true;
		grid[x][y] = e;
		elements[x][y] = e.id;
		moveable[x][y] = e.fixed == 0 ? 1 : 0;
		weight[x][y] = e.weight(temperature0[x][y]);
	}

	public void setValue(int x, int y, int eId) {
		setValue(x, y, indexer.getBaseElement(eId));
	}

	public void spawnValue(int x, int y, Element e) {
		if (!inBounds(x, y)) {
			return;
		}
		if (e.spawnTemperature != -1) {
			temperature0[x][y] = e.spawnTemperature;
			temperature1[x][y] = e.spawnTemperature;

		}
		setValue(x, y, e);

	}

	protected boolean inBounds(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < dotsX) && (y < dotsY);
	}

}
