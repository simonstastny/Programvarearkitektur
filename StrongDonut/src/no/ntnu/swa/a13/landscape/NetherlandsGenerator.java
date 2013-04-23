package no.ntnu.swa.a13.landscape;

public class NetherlandsGenerator implements LandscapeGenerator {
	
	@Override
	public float f(float x) {
		return (float) (BASELINE - 20);
	}

}
