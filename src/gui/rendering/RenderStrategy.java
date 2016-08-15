package gui.rendering;

import java.awt.Graphics;

public interface RenderStrategy {
	
	void render(Graphics p, int x, int y, double t);
	int width();
	int height();

}
