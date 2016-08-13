package grid;

public abstract class AbstractGrid<T> {

	public final int dotsX, dotsY;

	public AbstractGrid(int dotsX, int dotsY) {

		if (dotsX <= 0 || dotsY <= 0) {
			throw new IllegalArgumentException("Sizes should be > 0");
		}

		this.dotsX = dotsX;
		this.dotsY = dotsY;
	}

	public void reset(T seed) {
		fillStretch(0, 0, dotsX, dotsY, seed);
	}

	public abstract void setValue(int x, int y, T t);

	public abstract T getValue(int x, int y);

	public void put(int x, int y, T t) {
		if(x <0 || y < 0 || x >= dotsX || y >= dotsY){
			return;
		}
		setValue(x, y, t);
	}

	public T get(int x, int y) {
		return getValue(x, y);
	}

	public void fillStretch(int x, int y, int w, int h, T value) {
		if (w == 0 || h == 0) {
			throw new IllegalArgumentException("Empty fillstretch");
		}
		for (int xi = x; xi < x + w; xi++) {
			for (int yi = y; yi < y + h; yi++) {
				setValue(xi, yi, value);
			}
		}
	}

	public void swap(int x, int y, int x0, int y0) {
		T h = getValue(x, y);
		setValue(x, y, getValue(x0, y0));
		setValue(x0, y0, h);
	}

	public int width() {
		return dotsX;
	}

	public int height() {
		return dotsY;
	}

	protected void checkBounds(int x, int y) {
		if (x > dotsX) {
			throw new IndexOutOfBoundsException("X out of bounds");
		}
		if (y > dotsY) {
			throw new IndexOutOfBoundsException("Y out of bounds");
		}
		if (x < 0) {
			throw new IndexOutOfBoundsException("X out of bounds: <0");
		}
		if (y < 0) {
			throw new IndexOutOfBoundsException("Y out of bounds: <0");
		}
	}

	protected boolean inBounds(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < dotsX) && (y < dotsY);
	}

	protected boolean onBorder(int x, int y) {
		return x == 0 || y == 0 || x == (dotsX - 1) || y == (dotsY - 1);
	}

	protected boolean onCorner(int x, int y) {
		return ((x == 0 || x == (dotsX - 1)) && (y == 0 || y == (dotsY - 1)));
	}

}
