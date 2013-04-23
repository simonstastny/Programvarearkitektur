package no.ntnu.swa.a13;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {
	private Status status;

	public enum Status {
		DESTROYED, ALIVE;
	}

	private Vector2 coordinates;

	private int id;

	private float force;
	private float angle;
	
	private Body catapult;

	private float health;

	/**
	 * 
	 * @param id
	 *            id number of player
	 * @param coordinates
	 *            vector coordinates of players location
	 */
	public Player(int id, Vector2 coordinates) {
		super();
		this.id = id;
		this.coordinates = coordinates;

		this.health = GameLogic.HEALTH_MAX;
		this.force = GameLogic.HEALTH_MAX;
		this.angle = GameLogic.HEALTH_MAX;

		this.status = Status.ALIVE;
	}

	/**
	 * 
	 * @param damage
	 *            point to subtract from health
	 */
	public Status causeDamage(float damage) {
		health = health - damage;
		
		System.out.println("TANK ID " + id + " has health " + health);

		if (health <= 0) {
			status = Status.DESTROYED;
			System.out.println("DESTROYED " + id);
			return Status.DESTROYED;
		}
		
		return Status.ALIVE;
	}

	public Status getStatus() {
		return status;
	}

	public Vector2 getCoordinates() {
		return coordinates;
	}
	
	public void setCatapultBody(Body catapult){
		this.catapult = catapult;
	}
	
	public Body getCatapult(){
		return catapult;
	}

	public float getForce() {
		return force;
	}

	public float getAngle() {
		return angle;
	}

	public void setForce(float force) {
		this.force = force;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	public void setCoordinates(Vector2 coordinates){
		this.coordinates = coordinates;
	}
	
	public int getId() {
		return id;
	}
}
