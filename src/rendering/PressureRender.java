package rendering;

import java.awt.Color;
import static utils.Utils.*;

public class PressureRender extends DotRender {

	public PressureRender(int dotWidth, int dotHeight) {
		super(dotWidth, dotHeight);
	}

	@Override
	public Color dotColor(double t) {
		t /= 1000;
		if(Double.isNaN(t)){
			return Color.PINK;
		}
		if(t <= -1.0){
			return Color.BLUE;
		}
		if(t >= 1.0){
			return Color.GREEN;
		}
		if (t < 0) {
			return interpolate(Color.BLUE, Color.BLACK, 1.0 + t, -t);
		} else {
			return interpolate(Color.BLACK, Color.GREEN, Math.min(t, 1.0),
					Math.max(1.0 - t, 0));
		}
	}

}
