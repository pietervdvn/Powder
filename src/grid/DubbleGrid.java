package grid;

public class DubbleGrid extends Grid{
	
	private double[][] workingGrid;

	public DubbleGrid(int dotsX, int dotsY) {
		this(dotsX, dotsY,1,1);
	}

	public DubbleGrid(int dotsX, int dotsY, int resolutionX, int resolutionY) {
		super(dotsX, dotsY, resolutionX, resolutionY);
		this.workingGrid = new double[grid.length][grid[0].length];
	}
	
	public void swap(){
		double[][] helper = grid;
		grid = workingGrid;
		workingGrid = helper;
	}
	
	@Override
	public void reset(double seed) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				workingGrid[i][j] = seed;
				grid[i][j] = seed;
			}
		}
	}
	
	public void setWorkingValue(int x, int y, double t) {
		checkBounds(x, y);
		int realX = x / resolutionX;
		int realY = y / resolutionY;
		workingGrid[realX][realY] = t;
	}

	public double getWorkingValue(int x, int y) {
		checkBounds(x, y);
		int realX = x / resolutionX;
		int realY = y / resolutionY;
		return workingGrid[realX][realY];
	}

	public void setRawWorkingValue(int x, int y, double t) {
		workingGrid[x][y] = t;
	}

	public double getRawWorkingValue(int x, int y) {
		return workingGrid[x][y];
	}
	
	public double getWorkingValueZ(int x, int y) {
		if (inBounds(x, y)) {
			return getValue(x, y);
		} else {
			return 0;
		}
	}

	public void change(int x, int y, double d) {
		grid[x][y] += d;
	}

	
}
