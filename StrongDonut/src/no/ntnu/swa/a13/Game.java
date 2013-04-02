package no.ntnu.swa.a13;

import java.util.Vector;

import no.ntnu.swa.a13.Player.Status;
import no.ntnu.swa.a13.State.GameStatus;

public class Game implements Runnable {
	// constants
	public static final float HEALTH_MAX = 100;
	public static final float FORCE_DEFAULT = 50;
	public static final float ANGLE_DEFAULT = 45;
	
	// fields
	Vector<Player> players;
	
	State currentState;
	
	Score score;
	
	Strategy strategy;

	public Game(int numPlayers, Strategy strategy) {
		this.strategy = strategy;
		
		this.score = new Score();
		
		// generate players
		this.players = new Vector<Player>(numPlayers);
		for(int i = 0; i < numPlayers; i++) {
			players.add(new Player(i, null)); //FIXME coordinates generation
		}
		
		// generate state sequence and take the first one as initial
		this.currentState = strategy.generateStates(players).firstElement();
	}
		
	public void handleEvent(Event event) {
		if(event instanceof DamageEvent) {
			DamageEvent damageEvent = (DamageEvent) event;
			
			damageEvent.getTarget().causeDamage(damageEvent.getDamage());
			
			if(damageEvent.getTarget().status == Status.DESTROYED) {
				currentState.fixSequence();
			}
		}
	}

	@Override
	public void run() {
		// while not GAME_OVER, iterate over players and let them play
		while(currentState.getStatus() != GameStatus.GAME_OVER)	{
			currentState = currentState.getNext();
			
			//FIXME get user action...
			//FIXME fire events..
			
			strategy.checkGameStatus(currentState);
		}
	}
	
	
}
