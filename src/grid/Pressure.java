package grid;

public class Pressure extends DubbleGrid{
	
	public Pressure(int dotsX, int dotsY) {
		super(dotsX, dotsY);
	}
	
	public int calculateStructuralPressure(int[][] moveable, double[][] weights, double threshold, double[] tainted){
		int fixedCols = 0;
		for (int i = 0; i < tainted.length; i++) {
			if (tainted[i] > threshold) {
				tainted[i] = 0.0;
				calculateStructuralPressure(moveable, weights, i);
				fixedCols ++;
			}
		}
		return fixedCols;
	}
	
	public void calculateStructuralPressure(int[][] moveable, double[][] weights){
		for (int x = 0; x < width(); x++) {
			calculateStructuralPressure(moveable, weights, x);
		}
	}
	
	public void calculateStructuralPressure(int[][] moveable, double[][] weights, int x){
		grid[x][0] = weights[x][0];
		for (int y = 1; y < height(); y++) {
			
			grid[x][y] = moveable[x][y-1] * grid[x][y-1] + weights[x][y]*ElementsGrid.GRAVITY;
		}
	}
	
}
