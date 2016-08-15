import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import grid.FullGrid;
import grid2.Element;
import grid2.ElementIndexing;
import grid2.ParseElements;
import grid2.Reaction;
import grid2.ReactionCache;
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

		List<String> fromFile = Files.readAllLines(Paths.get("elements.csv"));

		Element[] els = ParseElements.parseElements(fromFile);

		for (Element e : els) {
			System.out.println(e); // TODO remove sysout
		}
		fromFile = Files.readAllLines(Paths.get("reactions2.csv"));

		ElementIndexing indexer = new ElementIndexing(els);
		Reaction[] loadedReactions = Reaction.fromCSVs(indexer, fromFile);
		for (Reaction reaction : loadedReactions) {
			System.out.println(reaction); // TODO remove sysout
		}
		ReactionCache reactions = new ReactionCache(indexer, loadedReactions);

		int dotsX = 400;
		int dotsY = 100;

		int pixelsTargetX = 1200;
		int pixelsDot = pixelsTargetX / dotsX;

		UsefullFullGrid elements = new UsefullFullGrid(indexer, reactions, dotsX, dotsY);

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
