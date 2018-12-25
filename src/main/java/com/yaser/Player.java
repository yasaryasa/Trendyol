package com.yaser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.yaser.exceptions.InsufficientManaException;

/**
 * 
 * @author yaser
 *
 */
public class Player {
	private static final Logger logger = Logger.getLogger(Player.class.getName());
	// Constants
	private static final int MAX_NUM_OF_HAND_CARD = 5;
	private static final int MAX_MANA_SLOTS = 10;
	private static final int STARTING_HAND_SIZE = 3;
	// Message constants
	private static final String HAND_INCREASED_LOG_MSG = "{0} player''s hand increased to : {1}";
	private static final String DECK_PICKED_CARD_MSG = "{0} has picked a card from deck with mana cost : {1}";
	private static final String DROP_CARD_MSG = "{0} has dropped the card with the mana cost : {1} as hand is full. Special Rule 2. Overload!!!!";
	// Random generator
	private Random randomNumber = new Random();
	/**
	 * Default value for health of player
	 */
	private int health = 30;
	/**
	 * Default value for mana of player
	 */
	private int mana = 0;
	/**
	 * Name of player
	 */
	private String name;
	/**
	 * Default deck of player
	 */
	private List<Card> playerDeck = new ArrayList<>(Arrays.asList(new Card(0), new Card(0), new Card(1), new Card(1),
			new Card(2), new Card(2), new Card(2), new Card(3), new Card(3), new Card(3), new Card(3), new Card(4),
			new Card(4), new Card(4), new Card(5), new Card(5), new Card(6), new Card(6), new Card(7), new Card(8)));
	private List<Card> playerHand = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Player() {
	}

	/**
	 * Constructor with name parameter
	 * 
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
	}

	/**
	 * Initializes the player. This method should be called once at the
	 * beginning of the game
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	public void initForGame() {
		for (int i = 0; i < STARTING_HAND_SIZE; i++) {
			pickOneCard();
		}
	}

	/**
	 * Picks a card from players deck with specified rules
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	public void pickOneCard() {
		if (getSizeOfPlayerDeck() == 0) {
			setHealth(getHealth() - 1);
			logger.warning(getName() + " : Special Rule 1 : Bleeding Out!!! ");
		} else {
			// get one card from players deck randomly
			Card pickedCard = getPlayerDeck().get(randomNumber.nextInt(getPlayerDeck().size()));
			// remove card from player's deck
			getPlayerDeck().remove(pickedCard);
			logger.info(MessageFormat.format(DECK_PICKED_CARD_MSG, getName(), pickedCard.getManaCost()));
			// Special Rule 2
			if (getSizeOfPlayerHand() > MAX_NUM_OF_HAND_CARD) {
				logger.warning(MessageFormat.format(DROP_CARD_MSG, getName(), pickedCard.getManaCost()));
			} else {
				getPlayerHand().add(pickedCard);
				logger.info(MessageFormat.format(HAND_INCREASED_LOG_MSG, getName(), getSizeOfPlayerHand()));
			}
		}
	}

	/**
	 * Play turn according to rules against the given opponent to give max
	 * damage
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @param opponentPlayer
	 */
	public void playTurn(Player opponentPlayer) {
		logger.info(getName() + " is playing.");
		Card pickedCard = getMaxDamageCardByMana();
		if (pickedCard != null) {
			// overloaded method is called
			playTurn(opponentPlayer, pickedCard);
		}
	}

	/**
	 * Check if player can make a move or not
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @return
	 */
	public boolean hasEnoughManaToPlay() {
		if (getMaxDamageCardByMana() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the card in the hand with the max damage by player mana
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @return
	 */
	public Card getMaxDamageCardByMana() {
		Card pickedCard = new Card(-1);
		if (!getPlayerHand().isEmpty()) {
			for (Card card : getPlayerHand()) {
				if ((card.getManaCost() > pickedCard.getManaCost()) && card.getManaCost() <= getMana()) {
					pickedCard = card;
				}
			}
		}
		if (pickedCard.getManaCost() > -1) {
			if (pickedCard.getManaCost() == 0) {
				logger.info(getName() + " Special Rule 3. Dud Card!!!");
			}
			return pickedCard;
		}
		return null;
	}

	/**
	 * Plays the {@link Card} which is picked for opponent player
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @param opponent
	 * @param pickedCard
	 */
	public void playTurn(Player opponent, Card pickedCard) {
		// Player has no enough mana
		if (getMana() < pickedCard.getManaCost()) {
			throw new InsufficientManaException();
		}
		// opponent takes damage
		opponent.receiveDamage(pickedCard.getManaCost());
		// remove picked card from hand
		getPlayerHand().remove(pickedCard);
		// decrease mana value
		setMana(getMana() - pickedCard.getManaCost());
		opponent.displayPlayer();
	}

	/**
	 * Decreases Health according to damage cost
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @param damage
	 */
	public void receiveDamage(int damage) {
		setHealth(getHealth() - damage);
		logger.info(getName() + " player's health decreased to : " + getHealth());
	}

	/*Just to display players info*/
	private void displayPlayer() {
		String trimmer = " ----- ";
		StringBuilder sb = new StringBuilder();
		String newLine = "\n";
		sb.append(newLine);
		sb.append(trimmer + getName() + " Health         : " + getHealth() + trimmer).append(newLine);
		sb.append(trimmer + getName() + " Mana           : " + getMana() + trimmer).append(newLine);
		sb.append(trimmer + getName() + " Cards in Hand  : " + getSizeOfPlayerHand() + trimmer).append(newLine);
		sb.append(trimmer + getName() + " Cards in Deck  : " + getSizeOfPlayerDeck() + trimmer).append(newLine);
		logger.info(sb.toString());
	}

	/**
	 * Returns number of cards in players hand
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @return
	 */
	public int getSizeOfPlayerHand() {
		return getPlayerHand().size();
	}

	/**
	 * Returns number of cards in players deck
	 * 
	 * @DateModified 2018
	 * @author yaser
	 * @return
	 */
	public int getSizeOfPlayerDeck() {
		return getPlayerDeck().size();
	}

	/**
	 * Chooses mana up to MAX_MANA_SLOTS
	 * 
	 * @DateModified 2018
	 * @author yaser
	 */
	public void fillMana() {
		mana = randomNumber.nextInt(MAX_MANA_SLOTS - 1) + 1;// guarantees that
															// mana will be > 0
	}

	/** GETTER & SETTERS */
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public List<Card> getPlayerDeck() {
		return playerDeck;
	}

	public void setPlayerDeck(List<Card> playerDeck) {
		this.playerDeck = playerDeck;
	}

	public List<Card> getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(List<Card> playerHand) {
		this.playerHand = playerHand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
