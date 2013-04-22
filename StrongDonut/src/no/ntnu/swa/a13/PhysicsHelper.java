package no.ntnu.swa.a13;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class PhysicsHelper {
	
	public static final String BALL_ID = "BALL";

	public static boolean wasHit(Contact contact) {
		return getBall(contact.getFixtureA(),contact.getFixtureB()) != null;
	}

	public static Fixture getBall(Fixture a, Fixture b) {
		Fixture result = null;
		
		if(a.getBody().getUserData() != null && a.getBody().getUserData().equals(BALL_ID)) {
			result = a; 
		} else if(b.getBody().getUserData() != null && b.getBody().getUserData().equals(BALL_ID)) {
			result = b; 
		}
		
		return result;
	}

}
