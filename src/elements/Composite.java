package elements;

public class Composite extends Element {

	public final Elements original;

	/**
	 * A composite degenerates when the carrier vaporizes/freezes; in that case,
	 * the carrier pops up
	 * 
	 * @param carrier
	 * @param carried
	 */
	public Composite(Composites fixers, Elements carrier, Elements carried) {
		this(fixers, carrier.behaviour, carried);
	}

	private Composite(Composites fixers, Element carrier, Elements carried) {
		super(carrier.representation, carrier.color, carrier.lowestTemp,
				carrier.densityAtLowest, carrier.condensPoint,
				carrier.enthalpyCold, carrier.pressureCold, carrier.pCondens, null,
				carrier.highestTemp, carrier.densityAtHighest,
				carrier.vaporPoint, carrier.enthalpyHot, carrier.pressureHot, carrier.pVapor, null,
				carrier.movabiltiy, carrier.heatExchangeSame,
				carrier.heatInertia, carrier.dynamic);
		if(carried == null){
			throw new IllegalStateException("Why would you want a composite not carrying anything?");
		}
		this.cooledState = carryOver(fixers, false, carrier.cooledState,
				carried);
		this.heatedState = carryOver(fixers, true, carrier.heatedState, carried);
		this.original = carried;
	}

	private final Element carryOver(Composites fixers, boolean heated,
			Element carrier, Elements carried) {
		if (carrier == null && carried == null) {
			return null;
		}
		if (carrier == null) {
			return carried.behaviour;
		}
		fixers.addFix(this, heated, carrier.representation, carried);
		return null;
	}

	@Override
	public String toString() {
		return toString(false);
	};

	public String toString(boolean shortNotation) {
		String extra = "";
		if (!shortNotation) {
			extra = " (freezes to " + shortString(cooledState) + "; heats to "
					+ shortString(heatedState) + ")";
		}
		return name() + " carrying " + original.name() + extra;
	}

	private String shortString(Element e) {
		if (e == null) {
			return "nothing";
		}
		if (e instanceof Composite) {
			return ((Composite) e).toString(true);
		}
		return e.toString();
	}

	// SALT_WATER (carrying SALT) becomes SALT_ICE (carrier.cooledState)
	// (carrying SALT) ->
	// heatedState is 'this'

}
