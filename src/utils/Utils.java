package utils;

import java.util.Random;

public class Utils {

	public final static double NINF = Double.NEGATIVE_INFINITY;
	public final static double INF = Double.POSITIVE_INFINITY;

	private final static Random r = new Random();

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

}
