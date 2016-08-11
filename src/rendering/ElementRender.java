package rendering;

import grid.FullGrid;

import java.awt.Graphics;

public class ElementRender implements Render {
	
	private final FullGrid eg;
	private final int pixelsX, pixelsY;
	
	public ElementRender(FullGrid eg, int pixelsX, int pixelsY) {
		this.eg = eg;
		this.pixelsX = pixelsX;
		this.pixelsY = pixelsY;
		
	}

	@Override
	public void render(Graphics g) {
		for (int x = 0; x < eg.width(); x++) {
			for (int y = 0; y < eg.height(); y++) {
				g.setColor(eg.getColor(x, y));
				g.fillRect(x*pixelsX, y*pixelsY, pixelsX, pixelsY);
			}
		}
	}

	@Override
	public int getWidth() {
		return pixelsX*eg.width();
	}

	@Override
	public int getHeight() {
		return pixelsY*eg.height();
	}

}
