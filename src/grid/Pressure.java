package grid;

public class Pressure extends DubbleGrid{
	
	public Pressure(int dotsX, int dotsY) {
		super(dotsX, dotsY);
	}
	
	public int calculateStructuralPressure(double[][] weights, double threshold, double[] tainted){
		int fixedCols = 0;
		for (int i = 0; i < tainted.length; i++) {
			if (tainted[i] > threshold) {
				tainted[i] = 0.0;
				calculateStructuralPressure(weights, i);
				fixedCols ++;
			}
		}
		return fixedCols;
	}
	
	public void calculateStructuralPressure(double[][] weights){
		for (int x = 0; x < width(); x++) {
			calculateStructuralPressure(weights, x);
		}
	}
	
	public void calculateStructuralPressure(double[][] weights, int x){
		grid[x][0] = weights[x][0];
		for (int y = 1; y < height(); y++) {
			grid[x][y] = grid[x][y-1] + weights[x][y]*ElementsGrid.GRAVITY;
		}
	}
	
}
