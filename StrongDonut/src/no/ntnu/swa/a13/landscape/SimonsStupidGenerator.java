package no.ntnu.swa.a13.landscape;

public class SimonsStupidGenerator implements LandscapeGenerator {

	double rand = 0;

	@Override
	public float f(float x) {
		if (rand == 0) {
			rand = Math.random();
		}
		return (float) (Math.sin(0.02 * x) * 30 + Math.cos(0.01 * x) * 20 * rand * Math.sin(rand * 7) * 5 + BASELINE);
	}

}
