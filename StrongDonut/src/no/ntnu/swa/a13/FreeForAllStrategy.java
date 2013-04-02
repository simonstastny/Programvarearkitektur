package no.ntnu.swa.a13;

import no.ntnu.swa.a13.State.GameStatus;

public class FreeForAllStrategy extends Strategy {

	@Override
	boolean checkGameStatus(State state) {
		// if there is only one player alive
		if (state == state.getNext()) {
			state.setNext(new State(GameStatus.GAME_OVER, null));
			return true;
		} else {
			return false;
		}
	}

}
