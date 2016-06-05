package rendering;

import grid.Grid;

import java.awt.Graphics;

public class GridRender implements Render{
	
	private final Grid grid;
	private final RenderStrategy render;
	
	public GridRender(Grid grid, RenderStrategy strategy) {
		this.grid = grid;
		this.render = strategy;
	}
	
	public void render(Graphics g){
		
		for (int x = 0; x < grid.dotsX; x+=grid.resolutionX) {
			for (int y = 0; y < grid.dotsY; y+=grid.resolutionY) {
				render.render(g, x, y, grid.resolutionX , grid.resolutionY, grid.getValue(x, y));
			}
		}
	}

}
