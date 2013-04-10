package no.ntnu.swa.a13;

public abstract class Strategy {

	/**
	 * 
	 * @param game
	 * @return true if this game is over
	 */
	abstract void checkGameStatus(GameLogic game);

}
