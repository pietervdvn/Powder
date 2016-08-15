package levels;

import simulation.UsefullFullGrid;

public abstract class AbstractLevel {

	public final String name;
	public final double defaultTemp;
	public final String defaultElement;

	public AbstractLevel(String name, String defaultElement, double defaultTemp) {
		this.name = name;
		this.defaultTemp = defaultTemp;
		this.defaultElement = defaultElement;
	}

	public abstract void seed(UsefullFullGrid elements);

}
