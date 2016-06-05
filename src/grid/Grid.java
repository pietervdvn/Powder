package grid;

public class Grid {

	public final int dotsX, dotsY;
	public final int resolutionX, resolutionY;

	protected double[][] grid;

	public Grid(int dotsX, int dotsY) {
		this(dotsX, dotsY, 1, 1);
	}

	public Grid(int dotsX, int dotsY, int resolutionX, int resolutionY) {

		if (dotsX % resolutionX != 0) {
			throw new IllegalArgumentException(
					"PixelsX should be a multiple of resolutionX");
		}

		if (dotsY % resolutionY != 0) {
			throw new IllegalArgumentException(
					"PixelsY should be a multiple of resolutionY");
		}

		if (resolutionX <= 0 || resolutionY <= 0) {
			throw new IllegalArgumentException("Resolutions should be > 0");
		}

		this.dotsX = dotsX;
		this.dotsY = dotsY;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;

		grid = new double[dotsX / resolutionX][dotsY / resolutionY];
	}

	public void reset(double seed) {
		fillStretch(0, 0, dotsX, dotsY, seed);
	}

	public void setValue(int x, int y, double t) {
		checkBounds(x, y);
		int realX = x / resolutionX;
		int realY = y / resolutionY;
		grid[realX][realY] = t;
	}

	public double getValue(int x, int y) {
		checkBounds(x, y);
		int realX = x / resolutionX;
		int realY = y / resolutionY;
		return grid[realX][realY];
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
			return getValue(x, y);
		} else {
			return 0;
		}
	}

	protected boolean inBounds(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < grid.length)
				&& (y < grid[0].length);
	}

	protected boolean onRawBorder(int x, int y) {
		return x == 0 || y == 0 || x == (grid.length - 1)
				|| y == (grid[0].length - 1);
	}

	protected boolean onRawCorner(int x, int y){
		return ((x == 0 || x == (grid.length-1)) && (y == 0 || y == (grid[0].length-1)));
	}

	protected void checkBounds(int x, int y) {
		if (x > dotsX) {
			throw new IndexOutOfBoundsException("X out of bounds");
		}
		if (y > dotsY) {
			throw new IndexOutOfBoundsException("Y out of bounds");
		}
		if (x < 0) {
			throw new IndexOutOfBoundsException("X out of bounds: <0");
		}
		if (y < 0) {
			throw new IndexOutOfBoundsException("Y out of bounds: <0");
		}
	}
	
	public double getRawValueZ(int x, int y){
		if(x < 0 || y < 0 || x >= grid.length || y >= grid[0].length){
			return 0;
		}
		return grid[x][y];
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
}
