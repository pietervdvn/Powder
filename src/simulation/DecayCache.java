package simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.Utils;

public class DecayCache {

	private final MultiDecay[] reactions;

	public DecayCache(ElementIndexing indexer, DecayReaction[] allReactions) {

		Map<Integer, Set<DecayReaction>> triage = new HashMap<>();
		for (DecayReaction dr : allReactions) {
			if (!triage.containsKey(dr.element)) {
				triage.put(dr.element, new HashSet<>());
			}
			triage.get(dr.element).add(dr);
		}

		reactions = new MultiDecay[indexer.numberOfElements()];
		Set<DecayReaction> empty = new HashSet<>();
		for (Element el : indexer.getAllElements()) {
			reactions[el.id] = new MultiDecay(triage.getOrDefault(el.id, empty));
		}
	}

	public DecayReaction getReactionFor(int id) {
		return reactions[id].selectRandomReaction();
	}

	private static class MultiDecay {

		private final double[] cumulativeProb;
		private final DecayReaction[] reactions;

		public MultiDecay(Set<DecayReaction> reactionsSet) {
			int l = reactionsSet.size() + 1;
			int last = l - 1;
			this.reactions = new DecayReaction[l];

			int i = 0;
			for (DecayReaction decayReaction : reactionsSet) {
				reactions[i] = decayReaction;
				i++;
			}

			cumulativeProb = new double[l];
			if (l > 1) {
				cumulativeProb[0] = reactions[0].prob;
				for (i = 1; i < l-1; i++) {
					cumulativeProb[i] = cumulativeProb[i - 1] + reactions[i].prob;
				}

				if (cumulativeProb[last] > 1.0) {
					throw new IllegalArgumentException(
							"The total probability of " + reactions[0].element + " decaying exceeds one");
				}
			}

			reactions[last] = null;
			cumulativeProb[last] = 1.0;

		}

		/**
		 * Selects a reaction according to it's probability. Will often return
		 * null, if the randomizer is in a wrong mood
		 * 
		 * @return
		 */
		public DecayReaction selectRandomReaction() {
			double toss = Utils.randDouble();
			int i = 0;
			while (cumulativeProb[i] < toss) {
				i++;
			}
			return reactions[i];
		}

	}

}
