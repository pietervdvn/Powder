package rendering;

import java.awt.Graphics;

public interface RenderStrategy {
	
	void render(Graphics p, int x, int y, int pixelW, int pixelH, double t);

}
