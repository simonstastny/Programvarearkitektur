package no.ntnu.swa.a13.landscape;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Landscape {

	public static final int SLICE_WIDTH = 10;

	protected final List<SliceShape> slices;

	public List<SliceShape> getBodies() {
		return slices;
	}

	void eatLandscape(Vector2 groundZero, float force) {
		
		float exploLeftX = groundZero.x - force;
		float exploRightX = groundZero.x + force;

		int firstSlice = (int) Math.ceil(exploLeftX / SLICE_WIDTH);
		int lastSlice = (int) Math.ceil(exploRightX / SLICE_WIDTH);

		for (int i = firstSlice; i <= lastSlice; i++) {
			SliceShape slice = slices.get(i);

			eatCorner(groundZero, force, slice.getLeftTop());
			eatCorner(groundZero, force, slice.getRightTop());

			slice.set(new Vector2[] {
					slice.getLeftBottom(),
					slice.getLeftTop(),
					slice.getRightBottom(),
					slice.getRightTop()
					});
		}

	}

	void eatCorner(Vector2 center, float diameter, Vector2 corner) {
		double p = Math.pow(diameter, 2) - Math.pow((center.x - corner.x), 2);

		if (p >= 0) {
			corner.y = (float) Math.ceil(center.y - Math.sqrt(p));
		}
	}

	public Landscape(List<SliceShape> slices) {
		super();
		this.slices = slices;
	}
}
