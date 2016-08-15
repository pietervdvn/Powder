package grid2;

import java.awt.Color;
import java.util.Map;

import levels.AbstractLevel;
import reactivity.valueWrappers.IntegerValue;

/**
 * Wrapper around full grid, to make it usefull with the outer world
 * 
 * @author pietervdvn
 *
 */
public class UsefullFullGrid extends FullGrid {

	public final IntegerValue ticks = new IntegerValue(0, "Clock");
	public final IntegerValue timeout = new IntegerValue(15, "Timout between ticks (ms)");
	public final IntegerValue updateTime = new IntegerValue(0, "time for latest update (ms)");

	private final Map<String, Element> name2elements;
	private final Element[] id2Elements;


	public UsefullFullGrid(Map<String, Element> elements, Element[] idElements, int dotsX, int dotsY) {
		super(dotsX, dotsY);
		this.name2elements = elements;
		this.id2Elements = idElements;
	}

	/**
	 * Perform step update, increment counter
	 */
	public void tick() {
		// TODO Auto-generated method stub
		
		long start = System.currentTimeMillis();
		
		
		long stop = System.currentTimeMillis();
		
		updateTime.set((int) (stop - start));
		ticks.set(ticks.get() + 1);
		try {
			Thread.sleep(timeout.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick(IntegerValue progressMeas){
		// TODO
	}

	public void init(AbstractLevel l) {
		for (int x = 0; x < dotsX; x++) {
			for (int y = 0; y < dotsY; y++) {
				temperature0[x][y] = l.defaultTemp;
				temperature1[x][y] = l.defaultTemp;
				setValue(x, y, l.defaultElement);
			}
		}
		l.seed(this);
		ticks.set(0);
	}

	public void reset(Element seed) {
		fillStretch(0, 0, dotsX, dotsY, seed);
	}

	public void fillStretch(int x, int y, int w, int h, String element) {
		Element e = name2elements.get(element.toUpperCase());
		fillStretch(x, y, w, h, e);
	}

	public void setValue(int x, int y, String element) {
		Element e = name2elements.get(element.toUpperCase());
		setValue(x, y, e);
	}

	public void fillStretch(int x, int y, int w, int h, Element value) {
		if (w == 0 || h == 0) {
			throw new IllegalArgumentException("Empty fillstretch");
		}
		for (int xi = x; xi < x + w; xi++) {
			for (int yi = y; yi < y + h; yi++) {
				setValue(xi, yi, value);
			}
		}
	}

	public int width() {
		return dotsX;
	}

	public int height() {
		return dotsY;
	}

	public Color getColor(int x, int y) {
		int id = Math.max(0, elements[x][y]-1);
		Element e = id2Elements[id];
		return e.color;
	}
}
