package no.ntnu.swa.a13.landscape;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Landscape {

	public static final int SLICE_WIDTH = 1;

	protected final List<Rectangle> slices;
	

	public List<Rectangle> getSlices() {
		return slices;
	}
	


	public void deform(Vector2 groundZero, float force) {
		
		float exploLeftX = groundZero.x - force;
		float exploRightX = groundZero.x + force;

		int firstSlice = (int) Math.ceil(exploLeftX / SLICE_WIDTH);
		int lastSlice = (int) Math.ceil(exploRightX / SLICE_WIDTH);

		for (int i = firstSlice; i <= lastSlice; i++) {
			
			if(i < 0 || i >= slices.size()) {
				continue;
			}
			
			eatRectangle(groundZero, force, slices.get(i));
		}
	}

	void eatRectangle(Vector2 center, float diameter, Rectangle rect) {
		double p = Math.pow(diameter, 2) - Math.pow((center.x - rect.x), 2);
		
		float newY = (float) Math.ceil(center.y - Math.sqrt(p));

		if (p >= 0 && newY < rect.height) {
			rect.height = newY;
		}
	}

	public Landscape(List<Rectangle> slices) {
		this.slices = slices;
	}
	
	public List<Fixture> getFixtures() {
		return null; //TODO
	}
	
}
