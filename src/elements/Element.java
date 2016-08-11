package elements;

import static utils.Utils.INF;
import static utils.Utils.NINF;

import java.awt.Color;

public class Element {
	
	public final int i;

	protected final Elements representation;
	protected final Color color;
	protected final double lowestTemp;
	protected final double densityAtLowest;
	protected final double condensPoint;
	protected final double pCondens;
	protected final double pressureCold;
	protected final double enthalpyCold;
	protected Element cooledState;

	protected double highestTemp;
	protected final double densityAtHighest;
	protected final double vaporPoint;
	protected final double pVapor;
	protected final double pressureHot;
	protected final double enthalpyHot;
	protected Element heatedState;

	protected final double movabiltiy;
	protected final boolean dynamic;

	protected final double weightDiff;
	protected final double tempRange;
	
	protected final double heatExchangeSame;
	protected final double heatInertia;
	
	/**
	 * 
	 * @param representation
	 *            : the enum representing this element
	 * @param color
	 * @param lowestTemp
	 *            : minimum needed temperature
	 * @param densityAtLowest
	 *            : weight at this temperature. Scales lineary
	 * @param condensPoint
	 *            : point at which it might start condensing
	 * @param pCondens: chance it will change to cooledState randomly
	 * @param enthalpyCold
	 *            : how much heat it gives the environment when condensing
	 * @param cooledState
	 *            : changes to this element if cooled
	 * @param highestTemp
	 * @param densityAtHighest
	 * @param vaporPoint
	 * @param enthalpyHot
	 * @param pVapor: chance it will change to vapor randomly
	 * @param heatedState
	 * @param movabiltiy
	 *            : how 'liquid' it is. (0.0 -> 1.0). Use -1.0 if it is stuck
	 * @param heatExchangeSame
	 *            : exchange rate of heat with same material
	 * @param heatInertia
	 *            : exchange rate of heat with different material
	 * @param dynamic
	 *            : shows special behaviour
	 */

	public Element(Elements representation, Color color, double lowestTemp,
			double densityAtLowest, double condensPoint, double enthalpyCold,double pressureCold,
			double pCondens, Element cooledState, double highestTemp,
			double densityAtHighest, double vaporPoint, double enthalpyHot, double pressureHot,
			double pVapor, Element heatedState, double movabiltiy,
			double heatExchangeSame, double heatInertia, boolean dynamic) {
		this.representation = representation;
		this.color = color;
		this.i = representation.ordinal();

		this.lowestTemp = lowestTemp;
		this.densityAtLowest = densityAtLowest;
		this.condensPoint = condensPoint;
		this.enthalpyCold = enthalpyCold;
		this.pressureCold = pressureCold;
		this.pCondens = pCondens;
		this.cooledState = cooledState;

		this.highestTemp = highestTemp;
		this.densityAtHighest = densityAtHighest;
		this.vaporPoint = vaporPoint;
		this.enthalpyHot = enthalpyHot;
		this.pressureHot = pressureHot;
		this.pVapor = pVapor;
		this.heatedState = heatedState;
		this.movabiltiy = movabiltiy;
		this.dynamic = dynamic;
		
		this.heatExchangeSame = heatExchangeSame;
		this.heatInertia = heatInertia;

		weightDiff = densityAtHighest - densityAtLowest;
		tempRange = highestTemp - lowestTemp;

		if ((lowestTemp > condensPoint && condensPoint != NINF)
				|| (highestTemp < vaporPoint && vaporPoint != INF)) {
			throw new IllegalArgumentException(
					"Lowest and condens OR highest and vapor don't match!");
		}

	}

	public String name() {
		return representation.name().substring(0, 1)
				+ representation.name().substring(1).toLowerCase();
	}

	public Color color() {
		return color;
	}

	public double pVapor(double t) {
		if (t < vaporPoint) {
			return pVapor;
		}
		double p = (t - vaporPoint) / (highestTemp - vaporPoint);
		return Math.max(p * p * p, pVapor);
	}

	public double pCondens(double t) {
		if (t > condensPoint) {
			return pCondens;
		}
		double p = (t - condensPoint) / (lowestTemp - condensPoint);
		return Math.max(pCondens, p * p * p);
	}

	public double weight(double temperature) {
		return densityAtLowest + weightDiff * (temperature - lowestTemp)
				/ tempRange;
	}
	

	@Override
	public String toString() {
		return name()+ (heatedState == null ?"": ": heats to "+heatedState.name())
				+(cooledState == null?"":"; cools to "+cooledState.name());
	}

	public Elements getRepresentation() {
		return representation;
	}

	public double getLowestTemp() {
		return lowestTemp;
	}

	public double getWeightAtLowest() {
		return densityAtLowest;
	}

	public double getCondensPoint() {
		return condensPoint;
	}

	public double getEnthalpyCold() {
		return enthalpyCold;
	}

	public Element getCooledState() {
		return cooledState;
	}

	public double getHighestTemp() {
		return highestTemp;
	}

	public double getWeightAtHighest() {
		return densityAtHighest;
	}

	public double getVaporPoint() {
		return vaporPoint;
	}

	public double getEnthalpyHot() {
		return enthalpyHot;
	}

	public Element getHeatedState() {
		return heatedState;
	}

	public double getMovabiltiy() {
		return movabiltiy;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public boolean canBeMoved() {
		return movabiltiy >= 0;
	}

	public double getHeatInertia() {
		return heatInertia;
	}

	public double getHeatExchangeSame() {
		return heatExchangeSame;
	}
	
	public double getPressureCold() {
		return pressureCold;
	}
	
	public double getPressureHot() {
		return pressureHot;
	}

}
