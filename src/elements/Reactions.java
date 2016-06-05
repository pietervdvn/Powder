package elements;

import static elements.Elements.*;

public class Reactions {

	private final static int knownReactions = 6;

	public final static Elements[] elementA = new Elements[knownReactions*2];
	public final static Elements[] elementB = new Elements[knownReactions*2];
	public final static double[] prob = new double[knownReactions*2];
	public final static Element[] givesA = new Element[knownReactions*2];
	public final static Element[] givesB = new Element[knownReactions*2];

	public final static double[] enthalpyA = new double[knownReactions*2];
	public final static double[] enthalpyB = new double[knownReactions*2];

	public final static int nrOfElements = Elements.values().length;
	public final static int[] reactionIndex = new int[nrOfElements
			* nrOfElements * 2];
	static {
		for (int i = 0; i < reactionIndex.length; i++) {
			reactionIndex[i] = -1;
		}
	}

	static {
		addReaction(WATER, SAND, 1.0, WET_SAND, WET_SAND, 0,0);
		addReaction(WATER, SALT, 1.0, Composites.get(SALT_WATER, SALT),
				Composites.get(SALT_WATER, WATER), 0,0);
		addReaction(SALT, ICE, 1.0, SALT, WATER, 0,0);
		addReaction(WATER, AIR, 0.05, VAPOR, AIR, -2.257, 0);
		addReaction(WATER, VAPORIZER, 1.0, VAPOR, VAPORIZER, -2.257, 0);
		addReaction(VAPOR, CONDENSOR, 1.0, WATER, CONDENSOR, +2.257, 0);
		
	}

	private static int reactionsSeen = 0;

	private static void addReaction(Elements a, Elements b, double p,
			Elements givesAEl, Elements givesBEl, double enthA, double enthB) {
		addReaction(a, b, p, givesAEl.behaviour,
				givesBEl.behaviour, enthA, enthB);
	}
	
	private static void addReaction(Elements a, Elements b, double p,
			Element givesAEl, Element givesBEl, double enthA, double enthB) {
		addOneReaction(a, b, p, givesAEl, givesBEl, enthA, enthB);
		addOneReaction(b, a, p, givesBEl, givesAEl, enthB, enthA);
	}

	private static void addOneReaction(Elements a, Elements b, double p,
			Element givesAEl, Element givesBEl, double enthA, double enthB) {
		
		if(reactionsSeen == knownReactions*2){
			throw new IllegalStateException("Bump the 'knownReactions', you just added one");
		}
		
		elementA[reactionsSeen] = a;
		elementB[reactionsSeen] = b;
		prob[reactionsSeen] = p;
		givesA[reactionsSeen] = givesAEl;
		givesB[reactionsSeen] = givesBEl;

		enthalpyA[reactionsSeen] = enthA;
		enthalpyB[reactionsSeen] = enthB;

		reactionIndex[a.ordinal() * nrOfElements + b.ordinal()] = reactionsSeen;
		reactionsSeen++;
		
		System.out.println(a+" + "+b+" = "+givesAEl+" + "+givesBEl);
	}

}
