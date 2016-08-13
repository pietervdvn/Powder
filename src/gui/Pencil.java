package gui;

import elements.Element;
import grid.FullGrid;
import reactivity.ValueListener;
import reactivity.valueWrappers.Value;

@SuppressWarnings("rawtypes")
public class Pencil implements ValueListener {

	private final FullGrid fg;
	private final Value<Integer> mouseX, mouseY;
	private final Value<Element> selection;
	private final Value<Boolean> mousePressed;
	private final Value<Integer> pencilSize;

	@SuppressWarnings("unchecked")
	public Pencil(FullGrid fg, Value<Boolean> mousePressed, Value<Integer> mouseX, Value<Integer> mouseY,
			Value<Element> selection, Value<Integer> ticker, Value<Integer> pencilSize) {
		this.fg = fg;
		this.mousePressed = mousePressed;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.selection = selection;
		this.pencilSize = pencilSize;
		mousePressed.addListener(this);
		mouseX.addListener(this);
		mouseY.addListener(this);
		ticker.addListener(this);

	}
	
	private boolean recursiveCall = false;
	private void drawIfNeeded() {
		if (mouseX.get() < 0 || mouseY.get() < 0) {
			return;
		}
		if (mouseX.get() >= fg.dotsX || mouseY.get() >= fg.dotsY) {
			return;
		}
		if (mousePressed.get()) {
			int size = pencilSize.get();
			for (int x = -size; x <= size; x++) {
				for (int y = -size; y <= size; y++) {
					if (Math.sqrt(x * x + y * y) <= size) {
						fg.put(mouseX.get() + x, mouseY.get() + y, selection.get());
					}
				}
			}
			if(!recursiveCall){
				recursiveCall = true;
				fg.ticks.throwEvent();
				recursiveCall = false;
			}
			
		}
	}

	@Override
	public void onValueChanged(Value source, Object newValue) {
		drawIfNeeded();
	}

}
