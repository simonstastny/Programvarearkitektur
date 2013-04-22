package no.ntnu.swa.a13;

import com.badlogic.gdx.math.Vector2;

public abstract class Strategy {

	/**
	 * 
	 * @param game game to be checked
	 * @return true if this game is over
	 */
	abstract void checkGameStatus(GameLogic game);
	
	abstract Vector2 generateCoords(int n, int total);
}
