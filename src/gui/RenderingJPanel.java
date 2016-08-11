package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.Value;
import rendering.Render;

public class RenderingJPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private final List<Render> renderers = new ArrayList<>();
	private final List<BooleanValue> renderToggles = new ArrayList<>();
	
	private final ValueListener<Boolean> repainter = new ValueListener<Boolean>() {
		
		@Override
		public void onValueChanged(Value<Boolean> source, Boolean newValue) {
			repaint();
		}
	};

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < renderers.size(); i++) {
			if(renderToggles.get(i).get()){
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
		setSize(Math.max(getWidth(), r.getWidth()), 
				Math.max(getHeight(), r.getHeight()));
		setPreferredSize(getSize());
		
		return rt;
	}

	public void removeRenderer(String name) {
		for (int i = 0; i < renderToggles.size(); i++) {
			if(renderToggles.get(i).getName().equals(name)){
				removeRenderer(i);
				break;
			}
		}

	}

	public void removeRenderer(Render r) {
		removeRenderer( renderers.indexOf(r));
	}

	public void removeRenderer(int i) {
		renderers.remove(i);
		renderToggles.remove(i);
	}
	
}
