package levels;

import grid.ElementsGrid;
import grid.Temperature;


public interface Level {

	void seed(ElementsGrid elGrid, Temperature temperature);

	String name();

}
