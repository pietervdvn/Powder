package simulation;

public class ReactionCache {

	private final int numberOfElements;
	private final Reaction[] reactions;
	
	public ReactionCache(ElementIndexing indexer, Reaction[] loadedReactions) {
		this.numberOfElements = indexer.numberOfElements();
		reactions = new Reaction[numberOfElements * numberOfElements];
		
		for (Reaction r : loadedReactions) {
			addReaction(r);
		}
		
	}
	
	private void addReaction(Reaction r){
		reactions[r.element0*numberOfElements + r.element1] = r;
		reactions[r.element1*numberOfElements + r.element0] = r;

	}
	
	/**
	 * Returns a reaction if one exists, or null
	 */
	public Reaction getReactionFor(int id1, int id2){
		return reactions[id1*numberOfElements + id2];
	}
	
}
