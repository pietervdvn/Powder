package utils;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import elements.Element;
import elements.Elements;

public class Utils {

	public final static double NINF = Double.NEGATIVE_INFINITY;
	public final static double INF = Double.POSITIVE_INFINITY;

	private final static Random r = new Random();
	
	public static Elements[] knownElements(){
		return Elements.values();
	}

	public static boolean p(double d) {
		return r.nextDouble() < d;
	}

	public static int lr() {
		return r.nextBoolean() ? -1 : 1;
	}
	

	public static int lr(double p) {
		if (p(p)) {
			return r.nextBoolean() ? -1 : 1;
		}
		return 0;
	}

	public static double offset(){
		return r.nextGaussian();
	}
	
	public static String histToString(Map<Element, Integer> hist){
		String result = "Histogram:";
		for (Element key : hist.keySet()) {
			result+="\n   "+hist.get(key)+" of "+key;
		}
		return result;
	}
	
	public static Color interpolate(Color one, Color two, double distL, double distR) {
		if(distL < 0){
			System.err.println("WARNING: distL < 0");
			return one;
		}
		if(distR < 0){
			System.err.println("WARNING: distR < 0");
			return two;
		}
		
		int oneR = one.getRed();
		int oneG = one.getGreen();
		int oneB = one.getBlue();
		
		int twoR = two.getRed();
		int twoG = two.getGreen();
		int twoB = two.getBlue();
		
		double percentage = 1.0 - distL/(distL + distR);
		int r =  (int) (oneR * percentage + twoR * (1.0 - percentage));
		int g =(int) (oneG * percentage + twoG * (1.0 - percentage));
		int b = (int) (oneB * percentage + twoB * (1.0 - percentage));
		return new Color(r,g,b);
		
	}
	
}
