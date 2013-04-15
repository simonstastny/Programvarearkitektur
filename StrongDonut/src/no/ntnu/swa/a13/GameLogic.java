package no.ntnu.swa.a13;

import java.util.Iterator;
import java.util.Vector;

import no.ntnu.swa.a13.Player.PlayerStatus;
import no.ntnu.swa.a13.util.RingIterator;

public class GameLogic implements Runnable, GameEventListener {
	// constants
	public static final float HEALTH_MAX = 100;
	public static final float FORCE_DEFAULT = 50;
	public static final float ANGLE_DEFAULT = 45;

	enum GameStatus {
		GAME_OVER, ACTIVE;
	}

	private GameStatus status;

	// fields
	Vector<Player> activePlayers;
	Vector<Player> deadPlayers;

	Score score;

	private final Strategy strategy;

	public GameLogic(int numPlayers, Strategy strategy) {
		this.strategy = strategy;

		this.score = new Score();

		// generate players
		this.activePlayers = new Vector<Player>(numPlayers);
		this.deadPlayers = new Vector<Player>(numPlayers);

		for (int i = 0; i < numPlayers; i++) {
			activePlayers.add(new Player(i, null)); // FIXME coordinates generation
		}
	}

	public void handleEvent(Event event) {
		if (event instanceof DamageEvent) {
			DamageEvent damageEvent = (DamageEvent) event;

			Player target = damageEvent.getTarget();

			target.causeDamage(damageEvent.getDamage());

			if (target.getStatus() == PlayerStatus.DESTROYED) {
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
}
