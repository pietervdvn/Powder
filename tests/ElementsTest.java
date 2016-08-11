import static elements.Elements.AIR;
import static elements.Elements.VAPOR;

import org.junit.Test;

public class ElementsTest {

	@Test
	public void test() {
		System.out.println("Temp\tAir\t\tVapor");
		for (int t = 275; t < 475; t+=25) {
			System.out.println(t+"\t"+AIR.behaviour.weight(t)+"\t"+VAPOR.behaviour.weight(t));
		}
	}

}
