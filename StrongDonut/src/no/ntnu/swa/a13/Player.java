package no.ntnu.swa.a13;

import com.badlogic.gdx.math.Vector2;

public class Player {
	Status status;
	
	public enum Status {
		DESTROYED, ALIVE;
	}
	
	Vector2 coordinates;
	
	int id;
		
	float force;
	float angle;
	
	float health;

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
		
		this.status = Status.ALIVE;
	}
	
	/**
	 * 
	 * @param damage point to subtract from health
	 */
	void causeDamage(float damage) {
		health = health - damage;
		
		if (health <= 0) {
			status = Status.DESTROYED;
		}
	}
}
