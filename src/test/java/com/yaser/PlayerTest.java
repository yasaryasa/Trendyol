package com.yaser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.yaser.exceptions.InsufficientManaException;

public class PlayerTest {
	private Player player;
	private Player opponentPlayer;
	private static final List<Card> MAX_CARD_LIST = new ArrayList<>(
			Arrays.asList(new Card(3), new Card(7), new Card(9), new Card(5), new Card(8), new Card(6)));

	@Before
	public void setUp() {
		player = new Player("Player 1");
		opponentPlayer = new Player();
		opponentPlayer.setName("Player 2");
		player.initForGame();
		opponentPlayer.initForGame();
	}

	/**
	 * Test initial health of player
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testInitialHealth() {
		assertEquals(player.getHealth(), 30);
	}

	/**
	 * Test initial mana of player
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testInitialMana() {
		assertEquals(player.getMana(), 0);
	}

	/**
	 * Test initial size of deck, when player hand is initialized
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testInitialDeckSize() {
		assertEquals(player.getSizeOfPlayerDeck(), 17);
	}

	/**
	 * Test initial size of hand, according to game rules
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testInitialHandSize() {
		assertEquals(player.getSizeOfPlayerHand(), 3);
	}

	/**
	 * Test mana filling, fillMana guarantees that player mana will be > 0
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testManaIsFilled() {
		player.fillMana();
		assertNotEquals(player.getMana(), 0);
	}

	/**
	 * Test picking one card from deck to hand. 
	 * Picking one card decreases the size of deck, 
	 * on the opposite increases the size of players hand
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testPickOneCardFromDeckToHand() {
		int sizeOfPlayerDeck = player.getSizeOfPlayerDeck();
		int sizeOfPlayerHand = player.getSizeOfPlayerHand();
		player.pickOneCard();
		// remove from player's deck
		assertEquals(player.getSizeOfPlayerDeck(), sizeOfPlayerDeck - 1);
		// increase player's hand
		assertEquals(player.getSizeOfPlayerHand(), sizeOfPlayerHand + 1);
	}

	/**
	 * If player's hand has reached to max size, picked card will be discarded.
	 * Player's deck size decreases and player's hand stays the same
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testMaxHandSizeAndDiscardPickedCard() {
		// set player's hand to max
		setPlayersHandDefault();
		int sizeOfPlayerDeck = player.getSizeOfPlayerDeck();
		int sizeOfPlayerHand = player.getSizeOfPlayerHand();
		player.pickOneCard();
		// remove from deck
		assertEquals(player.getSizeOfPlayerDeck(), sizeOfPlayerDeck - 1);
		// as hand size is equal to max, pickedcard shouldn't be added to hand
		assertEquals(player.getSizeOfPlayerHand(), sizeOfPlayerHand);
	}

	/**
	 * The Current Player picks card wiith the max damage cost to opponent depending on his mana slots.
	 * Tests that player picks the max mana cost card to damage the opponent and finish game earlier
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testMaxDamageCard() {
		List<Card> cardList = Arrays.asList(new Card(3), new Card(7), new Card(9), new Card(0));
		// set player hand as pleased
		player.setPlayerHand(cardList);
		// set player mana to 10
		player.setMana(10);
		assertEquals(9, player.getMaxDamageCardByMana().getManaCost());
		// set player mana to 6
		player.setMana(6);
		assertEquals(3, player.getMaxDamageCardByMana().getManaCost());
		// set player mana to 8
		player.setMana(8);
		assertEquals(7, player.getMaxDamageCardByMana().getManaCost());
		// set player mana to 0
		player.setMana(0);
		assertEquals(0, player.getMaxDamageCardByMana().getManaCost());
	}

	/**
	 * Tests that player has enough mana to play depending on his cards in hand
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testHasEnoughManaToPlay() {
		setPlayersHandDefault();
		// set player mana to 4
		player.setMana(4);
		assertTrue(player.hasEnoughManaToPlay());
	}

	/**
	 * Tests that player doesn't have enough mana to play depending on his cards in hand
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testHasNotEnoughManaToPlay() {
		setPlayersHandDefault();
		// set player mana to 2
		player.setMana(2);
		assertFalse(player.hasEnoughManaToPlay());
	}

	/**
	 * Tests that playing decreases the size of player hand
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testPlayTurnHandSizeDecreased() {
		setPlayersHandDefault();
		// set player mana to 5 as one card should be picked
		player.setMana(5);
		int sizeOfPlayerHand = player.getSizeOfPlayerHand();
		player.playTurn(opponentPlayer);
		assertEquals(player.getSizeOfPlayerHand(), sizeOfPlayerHand - 1);
	}

	/**
	 * When player picks one card from his empty deck, he receives one damage.
	 * Tests that the player with empty hand receives damage when trying to pick a card
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testPickOneCardFromEmptyDeck() {
		List<Card> emptyDeck = Collections.emptyList();
		opponentPlayer.setPlayerDeck(emptyDeck);
		int currentHealth = opponentPlayer.getHealth();
		opponentPlayer.pickOneCard();
		assertEquals(opponentPlayer.getHealth(), currentHealth - 1);
	}

	/**
	 * Tests if player has no enough mana to play the given card
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test(expected = InsufficientManaException.class)
	public void testInsuffientManaException() {
		player.setMana(0);
		player.playTurn(opponentPlayer, new Card(1));
	}

	
	/**
	 * When the player plays a card, the opponent takes damage. 
	 * 
	 * Tests the player gives damage to opponent
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	@Test
	public void testPlayTurnDamagesToOpponent() {
		setPlayersHandDefault();
		// initials
		player.setMana(10);
		player.playTurn(opponentPlayer);
		// as mana set to 10, maxDamageCardByMana is the closest card in hand,
		// which means 9
		assertEquals(opponentPlayer.getHealth(), 21);
		// second turn
		player.setMana(6);
		player.playTurn(opponentPlayer);
		// as mana set to 10, maxDamageCardByMana is the closest card in hand,
		// which means 6
		assertEquals(opponentPlayer.getHealth(), 15);
	}

	/**
	 * To be used in test cases
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	private void setPlayersHandDefault() {
		// set player hand as max size
		player.setPlayerHand(MAX_CARD_LIST);
	}
}
