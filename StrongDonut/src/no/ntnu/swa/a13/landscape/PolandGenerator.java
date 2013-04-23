package no.ntnu.swa.a13.landscape;

public class PolandGenerator implements LandscapeGenerator {
	
	double rand = 0;

	@Override
	public float f(float x) {
		if (rand == 0) {
			rand = Math.random();
		}
		return (float) (BASELINE + Math.sin(x*rand * 0.05) * 20);
	}

}
