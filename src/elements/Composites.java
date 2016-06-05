package elements;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

public class Composites {

	public final static Composites all = new Composites();

	public final int nrOfElements = Elements.values().length;
	private final Element[] composites = new Element[2 * nrOfElements
			* nrOfElements];

	private List<CompositeFixer> fixers = new ArrayList<>();

	private Composites() {
		int h = composites.length / 2;
		for (int i = 0; i < h; i++) {
			int cI = i / nrOfElements;
			int oI = i % nrOfElements;
			Elements carrier = Elements.values()[cI];
			Elements original = Elements.values()[oI];
			composites[i] = new Composite(this, carrier, original);
		}
		for (int i = h; i < composites.length; i++) {
			int oI = (i - h) / nrOfElements;
			int cI = (i - h) % nrOfElements;
			Elements carrier = Elements.values()[cI];
			Elements original = Elements.values()[oI];
			composites[i] = new Composite(this, carrier, original);
		}
	}

	protected void fix() {
		for (CompositeFixer fix : fixers) {
			fix.fix();
		}
		fixers = null;
		for (int i = 0; i < composites.length; i++) {
			System.out.println("Composite["+i+"] "+composites[i]);
		}
		
	}

	public Element getComposite(Element carrier, Element original) {
		return getComposite(carrier.representation,
				original.representation.ordinal());
	}

	public static Element get(Elements carrier, Elements originalA,
			Elements originalB) {
		if (Utils.p(0.5)) {
			return get(carrier, originalA);
		} else {
			return get(carrier, originalB);
		}
	}

	public static Element get(Elements carrier, Elements original) {
		return all.getComposite(carrier, original.ordinal());
	}
	
	
	
	private Element getComposite(Elements carrier, int original) {
		int cI = carrier.ordinal();
		int i = cI * nrOfElements + original;
		return composites[i];
	}

	protected void addFix(Composite toFix, boolean heated, Elements carrier,
			Elements carried) {
		fixers.add(new CompositeFixer(toFix, heated, carrier, carried));
	}

	public final class CompositeFixer {

		private final boolean heated;
		private final Composite toFix;
		private final Elements carrier;
		private final Elements carried;

		public CompositeFixer(Composite toFix, boolean heated,
				Elements carrier, Elements carried) {
			this.toFix = toFix;
			this.heated = heated;
			this.carrier = carrier;
			this.carried = carried;
		}

		public void fix() {
			Element carriedC = getComposite(carrier, carried.ordinal());
			if (heated) {
				toFix.heatedState = carriedC;
			} else {
				toFix.cooledState = carriedC;
			}
			
		}

	}

}
