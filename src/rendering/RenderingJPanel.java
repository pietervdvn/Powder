package rendering;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class RenderingJPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final List<Render> renderers = new ArrayList<>();

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (Render gridRender : renderers) {
			gridRender.render(g);
		}
	}

	public void addRenderer(Render r) {
		renderers.add(r);
	}

	public void removeRenderer(Render r) {
		renderers.remove(r);
	}

}
