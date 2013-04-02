package no.ntnu.swa.a13;

import no.ntnu.swa.a13.Player.Status;

public class State {
	
	public State(GameStatus status, Player player) {
		super();
		this.status = status;
		this.playerOnTurn = player;
	}

	enum GameStatus {
		ACTIVE, GAME_OVER;
	}
	
	final GameStatus status;

	State next;

	Player playerOnTurn;

	public Player getPlayerOnTurn() {
		return playerOnTurn;
	}

	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		this.next = next;
	}

	public GameStatus getStatus() {
		return status;
	}

	protected void fixSequence() {
		//FIXME test this!!!
		//FIXME document this
		State last = this;
		State current = this;

		do {
			// move to the next one
			current = current.getNext();
			
			// if this player has been destroyed, skip him
			if (current.getPlayerOnTurn().status == Status.DESTROYED) {
				// (make a link from last state to the next one leaving out the current one)
				last.setNext(current.getNext());
			}	
		} while (current != this);
	}

}
