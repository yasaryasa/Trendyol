package com.yaser;

import java.util.logging.Logger;

/**
 * 
 * @author yaser
 *
 */
public class GamePlay {
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(GamePlay.class.getName());
	/**
	 * First player of the game
	 */
    private Player currentPlayer;
    /**
     * Second and opponent player of the game
     */
    private Player opponentPlayer;

    public GamePlay(Player currentPlayer, Player opponentPlayer) {
        this.setCurrentPlayer(currentPlayer);
        this.setOpponentPlayer(opponentPlayer);
        this.getCurrentPlayer().initForGame();
        this.getOpponentPlayer().initForGame();
    }

    /**
     * Makes preparation for game turn
     * 
     * @DateModified 2018
     * @author yaser
     */
    public void beginTurn() {
        // fills the mana slots
        getCurrentPlayer().fillMana();
        // player picks a card to damage opponent
        getCurrentPlayer().pickOneCard();
    }

    /**
     * Ends the turn(If it is called, it means game is not over and switch players)
     * 
     * @DateModified 2018
     * @author yaser
     */
    public void endTurn() {
        changePlayer();
    }

    /**
     * Switches the current and opponent player
     * 
     * @DateModified 2018
     * @author yaser
     */
    private void changePlayer() {
        Player prevPlayer = getCurrentPlayer();
        setCurrentPlayer(getOpponentPlayer());
        setOpponentPlayer(prevPlayer);
    }

    /**
     * Starts and continuous game until one player wins the game
     * @DateModified 2018
     * @author yaser
     */
    public void play() {
        Player winner = null;
        for (; winner == null;) {
            // start preparations
            beginTurn();
            while (getCurrentPlayer().hasEnoughManaToPlay()) {
                getCurrentPlayer().playTurn(getOpponentPlayer());
                // check the winner after each turn
                winner = checkWinner();
                // if opponent has no health, no need to play card again
                if (winner != null) {
                    break;
                }
            }
            // if current player has no enough mana to play, end turn and switch players
            endTurn();
        }
        logger.severe("WINNER : " + winner.getName());
    }

    /**
     * Check and return the winner of game
     * @DateModified 2018
     * @author yaser
     * @return
     */
    public Player checkWinner() {
        if (getCurrentPlayer().getHealth() < 1) {
            return getOpponentPlayer();
        } else if (getOpponentPlayer().getHealth() < 1) {
            return getCurrentPlayer();
        } else {
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(Player opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }
}
