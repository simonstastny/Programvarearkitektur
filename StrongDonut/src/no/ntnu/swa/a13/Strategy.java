package no.ntnu.swa.a13;

import java.util.Vector;

import no.ntnu.swa.a13.State.GameStatus;

public abstract class Strategy {

	Vector<State> generateStates(Vector<Player> players) {

		Vector<State> generated = new Vector<State>(players.capacity());

		State last = null;

		// for each player, generate a state
		for (Player player : players) {
			State state = new State(GameStatus.ACTIVE, player);

			state.setNext(last);

			generated.add(state);
			last = state;
		}

		// loop - last one should be succeeded by the first one again
		last.setNext(generated.firstElement());

		return generated;
	}
	
	/**
	 * 
	 * @param state
	 * @return true if this state is the end of the game
	 */
	abstract boolean checkGameStatus(State state);

}
