package gui.rendering;

import static utils.Utils.interpolate;

import java.awt.Color;

public class TemperatureRender extends DotRender {

	// in °C
	private static int[] temperatures = { -275, -10, 0, 10, 25, 50, 75, 100, 1500, 5000 };
	private static Color[] colors = { new Color(201, 0, 237), new Color(50, 0, 75), 
		Color.BLUE,Color.BLACK,Color.CYAN, Color.GREEN,
			Color.YELLOW.darker(), Color.ORANGE, Color.RED, Color.WHITE };
	private static int last = temperatures.length - 1;

	static {
		if (colors.length != temperatures.length) {
			throw new IllegalStateException(
					"Temperatures and colors don't match");
		}
	}
	
	
	public TemperatureRender(int dotWidth, int dotHeight) {
		super(dotWidth, dotHeight);
	}

	@Override
	public Color dotColor(double temp) {
		// temperaturs come in as Kelvin
		temp -= 275;
		if (temp <= temperatures[0]) {
			return colors[0];
		}
		if (temp >= temperatures[last]) {
			return colors[last];
		}

		for (int i = 0; i < colors.length; i++) {
			if (temp == temperatures[i]) {
				return colors[i];
			}
		}

		for (int i = 0; i < colors.length; i++) {
			if (temp > temperatures[i]) {
				continue;
			}
			i--;
			// temperatures between i and i + 1
			// we scale the color in between
			double distL = temp - temperatures[i];
			double distR = temperatures[i + 1] - temp;

			return interpolate(colors[i], colors[i + 1], distL, distR);
		}
		
		return Color.PINK;

	}



}
