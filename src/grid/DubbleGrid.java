package grid;

public class DubbleGrid extends Grid{

	private double[][] workingGrid;

	public DubbleGrid(int dotsX, int dotsY) {
		super(dotsX, dotsY);
		this.workingGrid = new double[grid.length][grid[0].length];
	}
	
	public DubbleGrid(double[][] grid0, double[][] grid1) {
		super(grid0);
		this.workingGrid = grid1;
	}
	
	public void swap(){
		double[][] helper = grid;
		grid = workingGrid;
		workingGrid = helper;
	}
	
	
	public void reset(double seed) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				workingGrid[i][j] = seed;
				grid[i][j] = seed;
			}
		}
	}

	@Override
	public void reset(Double seed) {
		reset((double) seed); // yeah, it's subtle
	}
	
	public void setWorkingValue(int x, int y, double t) {
		checkBounds(x, y);
		workingGrid[x][y] = t;
	}

	public double getWorkingValue(int x, int y) {
		checkBounds(x, y);
		return workingGrid[x][y];
	}

	public double getWorkingValueZ(int x, int y) {
		if (inBounds(x, y)) {
			return getWorkingValue(x, y);
		} else {
			return 0;
		}
	}

}
