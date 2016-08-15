package grid2;

/**
 * The class holding all matrices to work on.
 *
 */
public class FullGrid {

	public final int dotsX, dotsY;

	/*
	 * What element is on what location? Multirepresentations
	 */
	public final int[][] elements;
	// if zero: can't move -> doesn't contribute to static pressure. This is
	// meta
	private final int[][] moveable;
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
	private final static double WEIGHT_DIFF_THRESHOLD = 25;

	// downward pressure
	public final double[][] staticPressure;

	// means an external change has happened (e.g. mouse painted something) ->
	// static pressure should be recalculated
	private boolean dirty = false;
	private final ElementIndexing indexer;

	public FullGrid(int dotsX, int dotsY, ElementIndexing indexer) {
		this.dotsX = dotsX;
		this.dotsY = dotsY;
		this.indexer = indexer;

		elements = new int[dotsX][dotsY];
		moveable = new int[dotsX][dotsY];
		temperature0 = new double[dotsX][dotsY];
		temperature1 = new double[dotsX][dotsY];

		weight = new double[dotsX][dotsY];
		weightDiff = new double[dotsX];
		staticPressure = new double[dotsX][dotsY];
	}

	// Clean evolve of all the grids (thus no interfacing with the outside
	// world)
	public void evolve() {

		// temperature might only update once in a while. If the diff is big
		// enough, we also update the weights
		boolean temperatureUpdateDiff = exchangeHeat();
		
		if(dirty){
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
		moveAround();

		// calculate static pressure if needed
		calculateStaticPressure();

	}

	private boolean exchangeHeat() {
		if (timeSinceTemperatureUpdate < TEMPERATURE_UPDATE_TIME) {
			timeSinceTemperatureUpdate++;
			return false;
		}
		timeSinceTemperatureUpdate = 0;
		// TODO Auto-generated method stub
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

	}

	private void interact() {
		// TODO Auto-generated method stub

	}

	private void moveAround() {
		/*
		 * // TODO Auto-generated method stub int xStart = 0; int xInd = +1;
		 * 
		 * if (p(0.5)) { xStart = dotsX - 1; xInd = -1; } int lastY = dotsY - 1;
		 * int lastX = dotsX - 1;
		 * 
		 * for (int x = xStart; x >= 0 && x <= lastX; x += xInd) { for (int y =
		 * 0; y <= lastY; y++) { if (!moveable[x][y]) { continue; } double
		 * moveability = index[x][y].getMovabiltiy();
		 * 
		 * // move down due to gravity if (y + 1 <= lastY && grid[x][y +
		 * 1].canBeMoved()) { // force pulling down double powDown =
		 * weights[x][y] - weights[x][y + 1]; powDown *= GRAVITY; if (powDown >
		 * 0 && grid[x][y + 1].canBeMoved()) { swap(x, y, x, y + 1); // we
		 * continue with the element that has come into // (x,y)'s place
		 * moveability = grid[x][y].getMovabiltiy(); } }
		 * 
		 * // sidewards force, negative = left double powLR =
		 * pressure.getValueZ(x + 1, y) - pressure.getValueZ(x - 1, y); powLR +=
		 * moveability * offset(); int xD = +1; if (powLR < 0) { xD = -1; }
		 * 
		 * // lets not move too much, bounds check if (Math.abs(powLR) < 0.1 ||
		 * x + xD < 0 || x + xD > lastX || !grid[x + xD][y].canBeMoved()) {
		 * continue; } if (p(moveability)) { fg.swap(x, y, x + xD, y); }
		 * 
		 * } }
		 */
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
		if (x < 0 || x >= dotsX || y < 0 || y >= dotsY) {
			return;
		}
		dirty = true;
		elements[x][y] = e.id;
		moveable[x][y] = e.fixed == 0 ? 1 : 0;
		weight[x][y] = e.weight(temperature0[x][y]);
	}

}
