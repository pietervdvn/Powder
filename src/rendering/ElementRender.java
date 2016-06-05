package rendering;

import grid.ElementGrid;

import java.awt.Graphics;

public class ElementRender implements Render {
	
	private final ElementGrid eg;
	private final int pixelsX, pixelsY;
	
	public ElementRender(ElementGrid eg, int pixelsX, int pixelsY) {
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

}
