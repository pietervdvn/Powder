package grid;

public class Grid extends AbstractGrid<Double>{

	
	protected double[][] grid;

	public Grid(int dotsX, int dotsY) {
		super(dotsX, dotsY);
		grid = new double[dotsX][dotsY ];
	}

	public void setValue(int x, int y, double t) {
		checkBounds(x, y);
		grid[x][y] = t;
	}
	
	public void setValue(int x, int y, Double t) {
		checkBounds(x, y);
		grid[x][y] = t;
	}

	public Double getValue(int x, int y) {
		checkBounds(x, y);
		return grid[x][y];
	}
	
	public void fillStretch(int x, int y, int w, int h, double value){
		for (int xi = x; xi < x + w; xi++) {
			for (int yi = y; yi < y + h; yi++) {
				grid[xi][yi] = value;
			}
		}
	}

	/**
	 * Get's the value, or zero if out of bounds
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double getValueZ(int x, int y) {
		if (inBounds(x, y)) {
			return grid[x][y];
		} else {
			return 0;
		}
	}

	public double sum(){
		double sum = 0;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				sum += grid[x][y];
			}
		}
		return sum;
	}
	
	public void change(int x, int y, double d) {
		grid[x][y] += d;
	}
	
	public double[][] getRaw(){
		return grid;
	}

}
