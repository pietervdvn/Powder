import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import grid.FullGrid;
import grid2.Element;
import grid2.ParseElements;
import grid2.UsefullFullGrid;
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

	public static void main0(String[] args) throws InterruptedException, IOException {

		System.out.println("Parsing"); // TODO remove sysout
		List<String> fromFile = Files.readAllLines(Paths.get("elements.csv"));

		Element[] els = ParseElements.parseElements(fromFile);
		
		Map<String, Element> name2element = new HashMap<>();
		for (Element element : els) {
			name2element.put(element.name.toUpperCase(), element);
		}
		

		int dotsX = 400;
		int dotsY = 100;

		int pixelsTargetX = 1200;
		int pixelsDot = pixelsTargetX / dotsX;

		UsefullFullGrid elements = new UsefullFullGrid(name2element, els, dotsX, dotsY);

		GUIColorModel cm = new GUIColorModel();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		PowderWindow w = new PowderWindow(cm, elements, els, pixelsDot);

	}
}
