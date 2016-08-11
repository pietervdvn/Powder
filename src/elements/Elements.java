package elements;

import java.awt.Color;

import static utils.Utils.*;

public enum Elements {

	BLOCK(Color.gray.darker(), NINF, 0, NINF, 0, null, INF, 0, INF, 0, null,
			-1.0, 0, 0),

	VAPORIZER(Color.gray.darker(), NINF, 0, NINF, 0, null, INF, 0, INF, 0,
			null, -1.0, 0, 0),

	CONDENSOR(Color.gray.darker(), NINF, 0, NINF, 0, null, INF, 0, INF, 0,
			null, -1.0, 0, 0),

	RAIN_MAKER(Color.gray.darker(), NINF, 0, NINF, 0, null, INF, 0, INF, 0, null,
			-1.0, 0, 0),

	AIR(new Color(0, 0, 0, 0), 0, 0.0006, NINF, 0, null, 475, 0.0005, INF, 0,
			null, 1.0, 0.2, 0.1),

	ICE(Color.WHITE, 0, 0.92, NINF, 0, null, 275, 0.917, 273, 0.355, null, 0.5,
			0.9, 0.5),

	VAPOR(Color.LIGHT_GRAY, 275, 0.000169, 275, 2.257, 1700.0, 0.00001,/* WATER */
	null, 425, 0.000015, INF, 0, 0, 0, null, 1.0, 0.2, 0.1, false),

	WATER(Color.BLUE, 275, 1.0, 277, 0.355, 0.0, 0.0, ICE, 375, 0.918, 370,
			2.257, 1700, 0.00001, VAPOR, 0.9, 0.8, 0.6, false),

	SAND(Color.YELLOW.darker(), 275, 2.0, NINF, 0, null, 10000, 1.5, INF, 0,
			null, 0.001, 0.3, 0.2),

	// TODO fix constants
	MAGMA(Color.RED.darker(), 275, 2.0000001, NINF, 0, null, 10000, 1.5, INF,
			0, null, 0.8, 0.8, 0.1),

	MAGMA_0(Color.ORANGE.darker(), 275, 2.0, NINF, 0, null, 10000, 1.50000001,
			INF, 0, null, 0.8, 0.8, 0.1),

	WET_SAND(Color.YELLOW.darker().darker(), 275, 2., 275, 0, null, 375, 1.5,
			350, 0.0000001, null, 0.2, 0.5, 0.4),

	SALT(Color.WHITE, 0, 2.17, NINF, 0, null, 10000, 2.0, INF, 0, null, 0.4,
			0.5, 0.2),

	SALT_ICE(Color.PINK, 0, 0.94, NINF, 0, null, 275, 0.937, 273, 0.355, null,
			0.5, 0.9, 0.5),

	SALT_WATER(new Color(20, 225, 252), 254.0, 1.02, 256, .3557, 0.0, 0.0001,
			SALT_ICE, 375.0, 1.03, 340, 2.257, 1700, 0.001, null, 0.9, 0.8,
			0.6, false),

	VINE(Color.GREEN, 275, 1.0, 277, 0.355, 0.0, 0.0, ICE, 375, 0.918, 370,
			2.257, 1700, 0.00001, VAPOR, -1.0, 0.5, 0.5, false),
			
	;

	static {
		// postfixes for "cannot reference field before initialization"...
		// JAVA...
		ICE.behaviour.heatedState = WATER.behaviour;
		VAPOR.behaviour.cooledState = WATER.behaviour;
		SALT_ICE.behaviour.heatedState = SALT_WATER.behaviour;

		Composites.all.fix();

	}
	public final Element behaviour;

	private Elements(Color color, double lowestTemp, double densityAtLowest,
			double condensPoint, double enthalpyCold, Elements cooledState,
			double highestTemp, double densityAtHighest, double vaporPoint,
			double enthalpyHot, Elements heatedState, double movabiltiy,
			double heatExchangeSame, double heatInertia) {
		this(color, lowestTemp, densityAtLowest, condensPoint, enthalpyCold,
				0.0, 0.0, cooledState, highestTemp, densityAtHighest,
				vaporPoint, enthalpyHot, 0.0, 0.0, heatedState, movabiltiy,
				heatExchangeSame, heatInertia, false);
	}

	private Elements(Color color, double lowestTemp, double densityAtLowest,
			double condensPoint, double enthalpyCold, double pressureCold,
			double pCondens, Elements cooledState, double highestTemp,
			double densityAtHighest, double vaporPoint, double enthalpyHot,
			double pressureHot, double pVapor, Elements heatedState,
			double movabiltiy, double heatExchangeSame, double heatInertia,

			boolean dynamic) {
		Element behaviour = new Element(this, color, lowestTemp,
				densityAtLowest, condensPoint, enthalpyCold, pressureCold,
				pCondens, cooledState == null ? null : cooledState.behaviour,
				highestTemp, densityAtHighest, vaporPoint, enthalpyHot,
				pressureHot, pVapor, heatedState == null ? null
						: heatedState.behaviour, movabiltiy, heatExchangeSame,
				heatInertia, dynamic);
		this.behaviour = behaviour;
	}

	private Elements(Element behaviour) {
		this.behaviour = behaviour;
	}

}
