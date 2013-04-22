package no.ntnu.swa.a13;

import com.badlogic.gdx.math.Vector2;

public class Player {
	private PlayerStatus status;

	public enum PlayerStatus {
		DESTROYED, ALIVE;
	}

	private Vector2 coordinates;

	private int id;

	private float force;
	private float angle;

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

		this.status = PlayerStatus.ALIVE;
	}

	/**
	 * 
	 * @param damage
	 *            point to subtract from health
	 */
	void causeDamage(float damage) {
		health = health - damage;

		if (health <= 0) {
			status = PlayerStatus.DESTROYED;
		}
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public Vector2 getCoordinates() {
		return coordinates;
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
