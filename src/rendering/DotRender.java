package rendering;

import java.awt.Color;
import java.awt.Graphics;


public abstract class DotRender implements RenderStrategy{
	
	/**
	 * Number of pixels used to represent a 'dot'
	 */
	private final int width, height;
	
	public DotRender(int dotWidth, int dotHeight) {
		if(dotHeight <= 0 || dotWidth <= 0){
			throw new IllegalArgumentException("Dotheight/width should be > 0");
			
		}
		this.width = dotWidth;
		this.height = dotHeight;
	}

	@Override
	public void render(Graphics g, int x, int y, int dotW, int dotH, double t) {
		// dotW = one 'superDot' of size W
		g.setColor(dotColor(t));
		g.fillRect(x*width, y*width, width*dotW, height*dotH);
	}
	
	public abstract Color dotColor(double t);
	
	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}
	

}
