package no.ntnu.swa.a13;

import java.util.Iterator;
import java.util.Vector;

import no.ntnu.swa.a13.Player.Status;
import no.ntnu.swa.a13.util.RingIterator;

//note from Sindre - this class will be dumbed down/ commented out
//to make it easier to implement the current two-player game
//the work needed to enable more than two players does not seem
//to be within our current timeframe
//*then commeth a period of time in which i try to make use of this*
//and then: after further consideration, this is way to complex for the simple game we are making right now
//it holds mostly information going far above and beyond what we need to make a working game
//if we had the time and manpower this would be vital, but the way to go now is something simple that works

public class GameLogic implements Runnable, GameEventListener {
	// constants - the latter two will never be used in the current game
	public static final float HEALTH_MAX = 100;
	public static final float FORCE_DEFAULT = 50; 
	public static final float ANGLE_DEFAULT = 45;

	enum GameStatus {
		GAME_OVER, ACTIVE;
	}

	private GameStatus status;

	RingIterator<Player> playerIter;
	
	// fields
	Vector<Player> activePlayers;
	Vector<Player> deadPlayers;
	
	Player onTurn;

	Score score;

	private final Strategy strategy;

	public GameLogic(int numPlayers, Strategy strategy) {
		this.strategy = strategy;

		this.score = new Score();

		// generate players
		this.activePlayers = new Vector<Player>(numPlayers);
		this.deadPlayers = new Vector<Player>(numPlayers);

		for (int i = 0; i < numPlayers; i++) {
			activePlayers.add(new Player(i, strategy.generateCoords(i, numPlayers)));
		}
	}

	public void handleEvent(Event event) {
		if (event instanceof DamageEvent) {
			DamageEvent damageEvent = (DamageEvent) event;

			Player target = damageEvent.getTarget();

			target.causeDamage(damageEvent.getDamage());

			if (target.getStatus() == Status.DESTROYED) {
				activePlayers.remove(target);
				deadPlayers.add(target);
			}
		}
	}

	@Override
	public void run() {
		// while not GAME_OVER, iterate over players and let them play

		Iterator<Player> playerIter = new RingIterator<Player>(activePlayers);

		while (status != GameStatus.GAME_OVER) {

			Player player = playerIter.next();

			// FIXME get user action...
			// FIXME fire events..

			strategy.checkGameStatus(this);
		}
	}

	public Vector<Player> getActivePlayers() {
		return activePlayers;
	}

	public Vector<Player> getDeadPlayers() {
		return deadPlayers;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public Player nextPlayer() {
		if(playerIter == null) {
			playerIter = new RingIterator<Player>(activePlayers);
		}
		
		return playerIter.next();
	}
	
	public Player getPlayer() {
		if(playerIter == null) {
			playerIter = new RingIterator<Player>(activePlayers);
		}
		
		return playerIter.get();
	}
}
