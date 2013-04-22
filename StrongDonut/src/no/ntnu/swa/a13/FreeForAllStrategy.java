package no.ntnu.swa.a13;

import com.badlogic.gdx.math.Vector2;

import no.ntnu.swa.a13.GameLogic.GameStatus;

public class FreeForAllStrategy extends Strategy {

	@Override
	public void checkGameStatus(GameLogic game) {
		// if there is only one player alive
		if (game.getActivePlayers().size() == 1) {
			game.setStatus(GameStatus.GAME_OVER);
		}
	}
	
	public Vector2 generateCoords(int n, int total) {
		
		if(total == 2) {
			if(n == 0) {
				return new Vector2(MyGdxGame.w/8, MyGdxGame.h);
			} else {
				return new Vector2(MyGdxGame.w*7/8, MyGdxGame.h);
			}
		}
		
		return null;
	}
}
