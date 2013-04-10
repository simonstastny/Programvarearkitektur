package no.ntnu.swa.a13;

import no.ntnu.swa.a13.GameLogic.GameStatus;

public class FreeForAllStrategy extends Strategy {

	@Override
	void checkGameStatus(GameLogic game) {
		// if there is only one player alive
		if(game.getActivePlayers().size() == 1) {
			game.setStatus(GameStatus.GAME_OVER);
		}
	}
}
