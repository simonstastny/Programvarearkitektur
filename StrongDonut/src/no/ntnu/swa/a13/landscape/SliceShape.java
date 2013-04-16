package no.ntnu.swa.a13.landscape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class SliceShape extends PolygonShape {

	Vector2 getLeftTop() {
		Vector2 best = null;
		Vector2 candidate = new Vector2();

		for (int i = 0; i < getVertexCount(); i++) {
			getVertex(i, candidate);

			if (best == null || best.x >= candidate.x && best.y < candidate.y) {
				best = candidate;
				candidate = new Vector2();
			}
		}

		return best;
	}

	Vector2 getRightTop() {
		Vector2 best = null;
		Vector2 candidate = new Vector2();

		for (int i = 0; i < getVertexCount(); i++) {
			getVertex(i, candidate);

			if (best == null || best.x <= candidate.x && best.y < candidate.y) {
				best = candidate;
				candidate = new Vector2();
			}
		}

		return best;
	}
	
	Vector2 getLeftBottom() {
		Vector2 best = null;
		Vector2 candidate = new Vector2();

		for (int i = 0; i < getVertexCount(); i++) {
			getVertex(i, candidate);

			if (best == null || best.x >= candidate.x && best.y > candidate.y) {
				best = candidate;
				candidate = new Vector2();
			}
		}

		return best;
	}

	Vector2 getRightBottom() {
		Vector2 best = null;
		Vector2 candidate = new Vector2();

		for (int i = 0; i < getVertexCount(); i++) {
			getVertex(i, candidate);

			if (best == null || best.x <= candidate.x && best.y > candidate.y) {
				best = candidate;
				candidate = new Vector2();
			}
		}

		return best;
	}

}
