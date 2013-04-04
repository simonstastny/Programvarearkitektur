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
	 * @param id id number of player
	 * @param coordinates vector coordinates of players location
	 */
	public Player(int id, Vector2 coordinates) {
		super();
		this.id = id;
		this.coordinates = coordinates;
		
		this.health = Game.HEALTH_MAX;
		this.force = Game.HEALTH_MAX;
		this.angle = Game.HEALTH_MAX;
		
		this.status = PlayerStatus.ALIVE;
	}
	
	/**
	 * 
	 * @param damage point to subtract from health
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
}
