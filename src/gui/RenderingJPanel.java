package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;
import rendering.Render;

public class RenderingJPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private final List<Render> renderers = new ArrayList<>();
	private final List<BooleanValue> renderToggles = new ArrayList<>();

	public final IntegerValue mouseX = new IntegerValue(-1, "mouse position X");
	public final IntegerValue mouseY = new IntegerValue(-1, "mouse position Y");
	public final BooleanValue mousePressed = new BooleanValue(false, "mouse pressed");

	private final ValueListener<Boolean> repainter = new ValueListener<Boolean>() {

		@Override
		public void onValueChanged(Value<Boolean> source, Boolean newValue) {
			repaint();
		}
	};

	private final int pixelsPerDot;

	public RenderingJPanel(int pixelsPerDot) {
		this.pixelsPerDot = pixelsPerDot;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < renderers.size(); i++) {
			if (renderToggles.get(i).get()) {
				renderers.get(i).render(g);
			}
		}
	}

	public List<BooleanValue> getRenderToggles() {
		return renderToggles;
	}

	public BooleanValue addRenderer(Render r, String name) {
		renderers.add(r);
		BooleanValue rt = new BooleanValue(true, name);
		renderToggles.add(rt);
		rt.addListener(repainter);
		setSize(Math.max(getWidth(), r.getWidth()), Math.max(getHeight(), r.getHeight()));
		setPreferredSize(getSize());

		return rt;
	}

	public void removeRenderer(String name) {
		for (int i = 0; i < renderToggles.size(); i++) {
			if (renderToggles.get(i).getName().equals(name)) {
				removeRenderer(i);
				break;
			}
		}

	}

	public void removeRenderer(Render r) {
		removeRenderer(renderers.indexOf(r));
	}

	public void removeRenderer(int i) {
		renderers.remove(i);
		renderToggles.remove(i);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseX.set(e.getX() / pixelsPerDot);
		mouseY.set(e.getY() / pixelsPerDot);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseX.set(-1);
		mouseY.set(-1);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed.set(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed.set(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX.set(e.getX() / pixelsPerDot);
		mouseY.set(e.getY() % pixelsPerDot);
	}

}
