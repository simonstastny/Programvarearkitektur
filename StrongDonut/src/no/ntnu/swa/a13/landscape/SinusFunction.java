package no.ntnu.swa.a13.landscape;

public class SinusFunction implements LandscapeGeneratingFunction {

	@Override
	public float getY(float x) {
		return (float) Math.sin(0.05*x);
	}

}
