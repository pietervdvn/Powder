import grid.ElementGrid;
import grid.ElementGridSeed;
import gui.PowderWindow;

import java.util.ArrayList;
import java.util.List;

import reactivity.resources.guiColorModel.GUIColorModel;

public class Main {

	public static void seedR(ElementGrid elements, int w, int h) {

	}

	public static void main(String[] args) {
		try {
			main0(args);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main0(String[] args) throws InterruptedException {

		int dotsX = 400;
		int dotsY = 100;

		int pixelsTargetX = 1600;
		int pixelsDot = pixelsTargetX / dotsX;

		ElementGrid elements = new ElementGrid(dotsX, dotsY);

		GUIColorModel cm = new GUIColorModel();

		List<ElementGridSeed> seeds = new ArrayList<>();
		seeds.add(Levels.beach);
		seeds.add(Levels.heatExchanger);
		seeds.add(Levels.magma);
		seeds.add(Levels.randomness);
		
		Levels.magma.seed(elements);

		new PowderWindow(cm, elements, seeds, pixelsDot);

		Thread.sleep(1000);

		while(true){
			elements.evolve();
			Thread.sleep(10);
			elements.getTemperature().setValue(dotsX/2, dotsY-1, 10000);
		}

	}
}
