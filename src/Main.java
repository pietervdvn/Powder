import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import grid.FullGrid;
import gui.PowderWindow;
import reactivity.resources.guiColorModel.GUIColorModel;

public class Main {

	public static void seedR(FullGrid elements, int w, int h) {

	}

	public static void main(String[] args) {
		try {
			main0(args);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main0(String[] args) throws InterruptedException {

		int dotsX = 400;
		int dotsY = 100;

		int pixelsTargetX = 1200;
		int pixelsDot = pixelsTargetX / dotsX;

		FullGrid elements = new FullGrid(dotsX, dotsY);

		GUIColorModel cm = new GUIColorModel();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		PowderWindow w = new PowderWindow(cm, elements, pixelsDot);

	}
}
