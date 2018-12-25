package com.yaser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private GamePlay gamePlay;

    /**
     * Init the game 
     * @DateModified 2018
     * @author yaser
     */
    @Before
    public void setUp() {
        gamePlay = new GamePlay(new Player("Player 1"), new Player("Player 2"));
    }

    /**
     * Test the initial values of players
     * 
     * @DateModified 2018
     * @author yaser
     */
    @Test
    public void testPlayersHandAndDeck() {
        //Current player's hand and deck
        assertEquals(gamePlay.getCurrentPlayer().getSizeOfPlayerHand(), 3);
        assertEquals(gamePlay.getCurrentPlayer().getSizeOfPlayerDeck(), 17);
        //Opponent player's hand and deck
        assertEquals(gamePlay.getOpponentPlayer().getSizeOfPlayerHand(), 3);
        assertEquals(gamePlay.getOpponentPlayer().getSizeOfPlayerDeck(), 17);
    }

    /**
     * Tests the beginTurn stages
     * 
     * @DateModified 2018
     * @author yaser
     */
    @Test
    public void testBeginTurn() {
        //at the beginning player hand size is 3
        int sizeOfPlayerHand = gamePlay.getCurrentPlayer().getSizeOfPlayerHand();
        gamePlay.beginTurn();
        //one card is picked
        assertEquals(gamePlay.getCurrentPlayer().getSizeOfPlayerHand(), sizeOfPlayerHand + 1);
        //fillMana is called and player's mana should be > 0
        assertNotEquals(gamePlay.getCurrentPlayer().getMana(), 0);
    }

    /**
     * Tests the endTurn stages and switches the players 
     * @DateModified 2018
     * @author yaser
     */
    @Test
    public void testEndTurn() {
        Player currentPlayer = gamePlay.getCurrentPlayer();
        Player opponentPlayer = gamePlay.getOpponentPlayer();
        gamePlay.endTurn();
        assertSame(currentPlayer, gamePlay.getOpponentPlayer());
        assertSame(opponentPlayer, gamePlay.getCurrentPlayer());
    }

    /**
     * Tests winner of the game 
     * 
     * @DateModified 2018
     * @author yaser
     */
    @Test
    public void testGameWinner() {
        Player currentPlayer = gamePlay.getCurrentPlayer();
        Player opponentPlayer = gamePlay.getOpponentPlayer();
        List<Card> currentPlayerHand = new ArrayList<Card>(Arrays.asList(new Card(9), new Card(5)));
        currentPlayer.setPlayerHand(currentPlayerHand);
        currentPlayer.setMana(10);
        opponentPlayer.setHealth(8);
        currentPlayer.playTurn(opponentPlayer);
        assertThat(gamePlay.checkWinner(), is(currentPlayer));
    }
    
    /**
     * Tests play game scenario and play game should result with a winner 
     * @DateModified 2018
     * @author yaser
     */
    @Test
    public void testPlayGame() {
    	gamePlay.play();
    	assertNotNull(gamePlay.checkWinner());
    }
}
