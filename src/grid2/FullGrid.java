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
	// if zero: can't move -> doesn't contribute to static pressure
	private final int[][] moveable;
	// both temperature and weight get swapped when elements swap
	public double[][] temperature0;
	public double[][] temperature1;

	private final double[][] weight;

	// downward pressure
	public final double[][] staticPressure;
	
	// means an external change has happened (e.g. mouse painted something) -> static pressure should be recalculated
	private boolean dirty = false;

	public FullGrid(int dotsX, int dotsY) {
		this.dotsX = dotsX;
		this.dotsY = dotsY;
		elements = new int[dotsX][dotsY];
		moveable = new int[dotsX][dotsY];
		temperature0 = new double[dotsX][dotsY];
		temperature1 = new double[dotsX][dotsY];

		weight = new double[dotsX][dotsY];
		staticPressure = new double[dotsX][dotsY];
	}
	
	
	/**
	 * External call
	 * @param x
	 * @param y
	 * @param e
	 */
	public void setValue(int x, int y, Element e) {
		dirty = true;
		elements[x][y] = e.id;
		moveable[x][y] = e.fixed == 0 ? 1 : 0;
		weight[x][y] = e.weight(temperature0[x][y]);
	}
	
	

}
